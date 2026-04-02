package br.rpgguilda.rpg_guilda.operacoes;

import br.rpgguilda.rpg_guilda.audit.entity.Organizacao;
import br.rpgguilda.rpg_guilda.audit.entity.Usuario;
import br.rpgguilda.rpg_guilda.audit.repository.OrganizacaoJpaRepository;
import br.rpgguilda.rpg_guilda.audit.repository.UsuarioJpaRepository;
import br.rpgguilda.rpg_guilda.operacoes.entity.*;
import br.rpgguilda.rpg_guilda.operacoes.enums.*;
import br.rpgguilda.rpg_guilda.operacoes.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DisplayName("Testes de Integração - Schema Operacoes (Domínio Aventura)")
public class OperacoesSchemaIntegrationTest {

    @Autowired private AventureiroJpaRepository aventureiroRepo;
    @Autowired private CompanheiroJpaRepository companheiroRepo;
    @Autowired private MissaoJpaRepository missaoRepo;
    @Autowired private ParticipacaoMissaoJpaRepository participacaoRepo;
    @Autowired private OrganizacaoJpaRepository orgRepo;
    @Autowired private UsuarioJpaRepository usuarioRepo;

    private Organizacao org;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        org = orgRepo.findById(1L).orElseThrow();
        usuario = usuarioRepo.findById(1L).orElseThrow();
    }

    @Test
    @DisplayName("Deve persistir aventureiro com organização e usuário")
    void devePersistirAventureiro() {
        AventureiroEntity a = new AventureiroEntity();
        a.setOrganizacao(org);
        a.setUsuarioCadastro(usuario);
        a.setNome("Thorin");
        a.setClasse(ClasseAventureiro.GUERREIRO);
        a.setNivel(10);
        a.setAtivo(true);
        a.setDataCriacao(LocalDateTime.now());
        a.setDataAtualizacao(LocalDateTime.now());

        a = aventureiroRepo.saveAndFlush(a);
        assertNotNull(a.getId());
        assertEquals("Guilda do Norte", a.getOrganizacao().getNome());
    }

    @Test
    @DisplayName("Deve persistir companheiro vinculado a aventureiro")
    void devePersistirCompanheiro() {
        AventureiroEntity a = criarAventureiro("Gandalf", ClasseAventureiro.MAGO, 20);
        CompanheiroEntity c = new CompanheiroEntity();
        c.setAventureiro(a);
        c.setNome("Shadowfax");
        c.setEspecie(EspecieCompanheiro.LOBO);
        c.setIndiceLealdade(95);
        c = companheiroRepo.saveAndFlush(c);

        assertEquals(a.getId(), c.getAventureiroId());
    }

    @Test
    @DisplayName("Deve persistir missão")
    void devePersistirMissao() {
        MissaoEntity m = new MissaoEntity();
        m.setOrganizacao(org);
        m.setTitulo("Reconquistar a Montanha Solitária");
        m.setNivelPerigo(NivelPerigo.CRITICO);
        m.setStatus(StatusMissao.PLANEJADA);
        m.setDataCriacao(LocalDateTime.now());
        m = missaoRepo.saveAndFlush(m);

        assertNotNull(m.getId());
    }

    @Test
    @DisplayName("Deve registrar participação em missão")
    void deveRegistrarParticipacao() {
        AventureiroEntity a = criarAventureiro("Legolas", ClasseAventureiro.ARQUEIRO, 15);
        MissaoEntity m = criarMissao("Defesa de Helm's Deep");

        ParticipacaoMissaoEntity p = new ParticipacaoMissaoEntity(m, a);
        p.setPapel(PapelMissao.COMBATENTE);
        p.setRecompensaOuro(new BigDecimal("500.00"));
        p.setDestaque(true);
        p.setDataRegistro(LocalDateTime.now());
        p = participacaoRepo.saveAndFlush(p);

        assertEquals(m.getId(), p.getId().getMissaoId());
        assertEquals(a.getId(), p.getId().getAventureiroId());
    }

    @Test
    @DisplayName("Deve remover aventureiro e companheiro (cascade)")
    void deveRemoverComCascade() {
        AventureiroEntity a = criarAventureiro("Ranger", ClasseAventureiro.ARQUEIRO, 5);
        CompanheiroEntity c = new CompanheiroEntity();
        c.setAventureiro(a);
        c.setNome("Falcão");
        c.setEspecie(EspecieCompanheiro.FALCAO);
        c.setIndiceLealdade(80);
        a.setCompanheiro(c);
        a = aventureiroRepo.saveAndFlush(a);

        Long id = a.getId();
        assertTrue(companheiroRepo.findById(id).isPresent());

        aventureiroRepo.deleteById(id);
        aventureiroRepo.flush();

        assertFalse(aventureiroRepo.findById(id).isPresent());
        assertFalse(companheiroRepo.findById(id).isPresent());
    }

    @Test
    @DisplayName("Deve navegar cross-schema: operacoes → audit")
    void deveNavegarCrossSchema() {
        AventureiroEntity a = criarAventureiro("Cross Test", ClasseAventureiro.BARDO, 1);
        assertEquals("Guilda do Norte", a.getOrganizacao().getNome());
        assertEquals("Root Norte", a.getUsuarioCadastro().getNome());
    }

    private AventureiroEntity criarAventureiro(String nome, ClasseAventureiro classe, int nivel) {
        AventureiroEntity a = new AventureiroEntity();
        a.setOrganizacao(org);
        a.setUsuarioCadastro(usuario);
        a.setNome(nome);
        a.setClasse(classe);
        a.setNivel(nivel);
        a.setAtivo(true);
        a.setDataCriacao(LocalDateTime.now());
        a.setDataAtualizacao(LocalDateTime.now());
        return aventureiroRepo.saveAndFlush(a);
    }

    private MissaoEntity criarMissao(String titulo) {
        MissaoEntity m = new MissaoEntity();
        m.setOrganizacao(org);
        m.setTitulo(titulo);
        m.setNivelPerigo(NivelPerigo.ALTO);
        m.setStatus(StatusMissao.EM_ANDAMENTO);
        m.setDataCriacao(LocalDateTime.now());
        return missaoRepo.saveAndFlush(m);
    }
}
