package br.rpgguilda.rpg_guilda.operacoes;

import br.rpgguilda.rpg_guilda.audit.entity.Organizacao;
import br.rpgguilda.rpg_guilda.audit.entity.Usuario;
import br.rpgguilda.rpg_guilda.audit.repository.OrganizacaoJpaRepository;
import br.rpgguilda.rpg_guilda.audit.repository.UsuarioJpaRepository;
import br.rpgguilda.rpg_guilda.operacoes.entity.*;
import br.rpgguilda.rpg_guilda.operacoes.enums.*;
import br.rpgguilda.rpg_guilda.operacoes.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Testes de Integração - Consultas e Relatórios")
public class ConsultasRelatoriosIntegrationTest {

    @Autowired private AventureiroJpaRepository aventureiroRepo;
    @Autowired private MissaoJpaRepository missaoRepo;
    @Autowired private ParticipacaoMissaoJpaRepository participacaoRepo;
    @Autowired private OrganizacaoJpaRepository orgRepo;
    @Autowired private UsuarioJpaRepository usuarioRepo;

    private Organizacao org;
    private Usuario usuario;
    private AventureiroEntity a1, a2, a3;
    private MissaoEntity m1, m2;

    @BeforeEach
    void setUp() {
        org = orgRepo.findById(1L).orElseThrow();
        usuario = usuarioRepo.findById(1L).orElseThrow();

        // Limpar dados anteriores (por segurança, já que o @Transactional devia lidar, mas garante estado limpo)
        participacaoRepo.deleteAll();
        missaoRepo.deleteAll();
        aventureiroRepo.deleteAll();

        // 1. Criar Aventureiros
        a1 = criarAventureiro("Aragorn", ClasseAventureiro.GUERREIRO, 15, true);
        a2 = criarAventureiro("Legolas", ClasseAventureiro.ARQUEIRO, 15, true);
        a3 = criarAventureiro("Saruman", ClasseAventureiro.MAGO, 20, false);

        // 2. Criar Missões
        m1 = criarMissao("Defesa de Gondor", NivelPerigo.CRITICO, StatusMissao.CONCLUIDA);
        m2 = criarMissao("Busca no Condado", NivelPerigo.BAIXO, StatusMissao.EM_ANDAMENTO);

        // 3. Criar Participações
        // a1 e a2 na missao m1
        registrarParticipacao(m1, a1, PapelMissao.LIDER, new BigDecimal("1000.00"), true);
        registrarParticipacao(m1, a2, PapelMissao.COMBATENTE, new BigDecimal("800.00"), false);
        // a1 na missao m2
        registrarParticipacao(m2, a1, PapelMissao.EXPLORADOR, new BigDecimal("200.00"), false);
    }

    @Test
    @DisplayName("Aventureiro - Listagem com Filtros")
    void testAventureiroListagemFiltros() {
        Page<AventureiroEntity> guerreiros = aventureiroRepo.findComFiltros(true, ClasseAventureiro.GUERREIRO, 10, PageRequest.of(0, 10, Sort.by("nome")));
        assertEquals(1, guerreiros.getTotalElements());
        assertEquals("Aragorn", guerreiros.getContent().get(0).getNome());
        
        Page<AventureiroEntity> nivelAlto = aventureiroRepo.findComFiltros(null, null, 15, PageRequest.of(0, 10));
        assertEquals(3, nivelAlto.getTotalElements());
        
        Page<AventureiroEntity> inativos = aventureiroRepo.findComFiltros(false, null, null, PageRequest.of(0, 10));
        assertEquals(1, inativos.getTotalElements());
        assertEquals("Saruman", inativos.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Aventureiro - Busca por Nome")
    void testAventureiroBuscaNome() {
        Page<AventureiroEntity> page = aventureiroRepo.findByNomeContainingIgnoreCase("ar", PageRequest.of(0, 10, Sort.by("nivel").descending()));
        assertEquals(2, page.getTotalElements()); // Aragorn e Saruman
        assertEquals("Saruman", page.getContent().get(0).getNome()); // nivel 20 descende Aragorn (15)
    }

    @Test
    @DisplayName("Aventureiro - Visualização Completa")
    void testAventureiroVisualizacaoCompleta() {
        Optional<AventureiroEntity> opt = aventureiroRepo.findByIdWithCompanheiro(a1.getId());
        assertTrue(opt.isPresent());
        AventureiroEntity a = opt.get();
        assertEquals("Aragorn", a.getNome());
        assertNull(a.getCompanheiro()); // Aragon não tem companheiro no setup
        
        Long totalParticipacoes = participacaoRepo.countByAventureiroId(a.getId());
        assertEquals(2L, totalParticipacoes);
        
        MissaoEntity ultima = participacaoRepo.findUltimaMissaoByAventureiroId(a.getId());
        assertNotNull(ultima);
    }

    @Test
    @DisplayName("Missão - Listagem com Filtros")
    void testMissaoListagemFiltros() {
        LocalDateTime inicioRange = LocalDateTime.now().minusDays(1);
        LocalDateTime fimRange = LocalDateTime.now().plusDays(1);
        
        Page<MissaoEntity> concluidas = missaoRepo.findComFiltros(StatusMissao.CONCLUIDA, null, inicioRange, fimRange, PageRequest.of(0, 10));
        assertEquals(1, concluidas.getTotalElements());
        assertEquals("Defesa de Gondor", concluidas.getContent().get(0).getTitulo());
    }

    @Test
    @DisplayName("Missão - Detalhamento Completo")
    void testMissaoDetalhamento() {
        Optional<MissaoEntity> opt = missaoRepo.findByIdWithParticipacoes(m1.getId());
        assertTrue(opt.isPresent());
        MissaoEntity m = opt.get();
        assertEquals(2, m.getParticipacoes().size());
        
        ParticipacaoMissaoEntity pAragorn = m.getParticipacoes().stream().filter(p -> p.getAventureiro().getNome().equals("Aragorn")).findFirst().get();
        assertEquals(PapelMissao.LIDER, pAragorn.getPapel());
        assertTrue(pAragorn.getDestaque());
        assertEquals(0, new BigDecimal("1000.00").compareTo(pAragorn.getRecompensaOuro()));
    }

    @Test
    @DisplayName("Relatório - Ranking de Participação")
    void testRankingParticipacao() {
        List<RankingParticipacaoProjection> ranking = participacaoRepo.getRankingParticipacao(null, null, null);
        assertEquals(2, ranking.size()); // Aragorn, Legolas
        
        RankingParticipacaoProjection primeiro = ranking.get(0);
        assertEquals("Aragorn", primeiro.getNome());
        assertEquals(2L, primeiro.getTotalParticipacoes());
        assertEquals(0, new BigDecimal("1200.00").compareTo(primeiro.getSomaRecompensas()));
        assertEquals(1L, primeiro.getQuantidadeDestaques());
        
        RankingParticipacaoProjection segundo = ranking.get(1);
        assertEquals("Legolas", segundo.getNome());
        assertEquals(1L, segundo.getTotalParticipacoes());
        assertEquals(0, new BigDecimal("800.00").compareTo(segundo.getSomaRecompensas()));
        assertEquals(0L, segundo.getQuantidadeDestaques());
    }

    @Test
    @DisplayName("Relatório - Missões com Métricas")
    void testRelatorioMissoesMetricas() {
        List<MissaoMetricaProjection> metricas = missaoRepo.getRelatorioMissoesMetricas(null, null);
        assertEquals(2, metricas.size());
        
        MissaoMetricaProjection mDefesa = metricas.stream().filter(m -> m.getTitulo().equals("Defesa de Gondor")).findFirst().get();
        assertEquals(StatusMissao.CONCLUIDA, mDefesa.getStatus());
        assertEquals(NivelPerigo.CRITICO, mDefesa.getNivelPerigo());
        assertEquals(2L, mDefesa.getQuantidadeParticipantes());
        assertEquals(new BigDecimal("1800.00"), mDefesa.getTotalRecompensas());
        
        MissaoMetricaProjection mBusca = metricas.stream().filter(m -> m.getTitulo().equals("Busca no Condado")).findFirst().get();
        assertEquals(1L, mBusca.getQuantidadeParticipantes());
        assertEquals(new BigDecimal("200.00"), mBusca.getTotalRecompensas());
    }

    private AventureiroEntity criarAventureiro(String nome, ClasseAventureiro classe, int nivel, boolean ativo) {
        AventureiroEntity a = new AventureiroEntity();
        a.setOrganizacao(org);
        a.setUsuarioCadastro(usuario);
        a.setNome(nome);
        a.setClasse(classe);
        a.setNivel(nivel);
        a.setAtivo(ativo);
        a.setDataCriacao(LocalDateTime.now());
        a.setDataAtualizacao(LocalDateTime.now());
        return aventureiroRepo.saveAndFlush(a);
    }

    private MissaoEntity criarMissao(String titulo, NivelPerigo perigo, StatusMissao status) {
        MissaoEntity m = new MissaoEntity();
        m.setOrganizacao(org);
        m.setTitulo(titulo);
        m.setNivelPerigo(perigo);
        m.setStatus(status);
        m.setDataCriacao(LocalDateTime.now());
        m.setDataInicio(LocalDateTime.now());
        m.setDataFim(LocalDateTime.now().plusDays(2));
        return missaoRepo.saveAndFlush(m);
    }
    
    private void registrarParticipacao(MissaoEntity m, AventureiroEntity a, PapelMissao papel, BigDecimal recompensa, boolean destaque) {
        ParticipacaoMissaoEntity p = new ParticipacaoMissaoEntity(m, a);
        p.setPapel(papel);
        p.setRecompensaOuro(recompensa);
        p.setDestaque(destaque);
        p.setDataRegistro(LocalDateTime.now());
        participacaoRepo.saveAndFlush(p);
    }
}
