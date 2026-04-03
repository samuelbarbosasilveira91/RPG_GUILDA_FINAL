package br.rpgguilda.rpg_guilda.service;

import br.rpgguilda.rpg_guilda.operacoes.entity.MvPainelTaticoMissao;
import br.rpgguilda.rpg_guilda.operacoes.repository.MvPainelTaticoMissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsável pela regra de negócio da consulta de missões táticas.
 *
 * QUESTÃO 1:
 * - Calcula a data limite dos últimos 15 dias
 * - Consulta o repository filtrando por ultima_atualizacao
 * - Ordena por indice_prontidao DESC
 * - Retorna somente os 10 primeiros registros
 *
 * QUESTÃO 2 - Estratégia de Cache:
 * Utiliza Spring Cache (@Cacheable) para armazenar o resultado da consulta
 * em memória, evitando que a query pesada seja executada a cada chamada HTTP.
 *
 * O cache "topMissoesTaticas" é limpo automaticamente a cada 5 minutos
 * por meio de @Scheduled + @CacheEvict, garantindo que os dados sejam
 * atualizados periodicamente sem sobrecarregar o banco.
 *
 * Essa abordagem:
 * - Reduz drasticamente o número de consultas ao banco
 * - Melhora o tempo de resposta do endpoint (retorno imediato após cache hit)
 * - Mantém a consistência aceitável com refresh automático a cada 5 minutos
 */
@Service
public class MissaoTaticaService {

    @Autowired
    private MvPainelTaticoMissaoRepository repository;

    /**
     * Retorna as 10 missões mais relevantes dos últimos 15 dias,
     * ordenadas por índice de prontidão decrescente.
     *
     * O resultado é cacheado para evitar consultas repetidas ao banco.
     *
     * @return Lista com até 10 missões táticas
     */
    @Cacheable("topMissoesTaticas")
    public List<MvPainelTaticoMissao> buscarTop10UltimosDias() {
        LocalDateTime limite = LocalDateTime.now().minusDays(15);
        return repository.findByUltimaAtualizacaoAfterOrderByIndiceProntidaoDesc(
                limite, PageRequest.of(0, 10));
    }

    /**
     * Limpa o cache a cada 5 minutos (300.000 ms).
     * Isso garante que os dados do banco sejam consultados novamente
     * após o período, mantendo a consistência.
     */
    @Scheduled(fixedRate = 300000)
    @CacheEvict(value = "topMissoesTaticas", allEntries = true)
    public void limparCacheMissoesTaticas() {
        // O Spring Cache faz o evict automaticamente ao detectar a anotação.
        // Este método é chamado a cada 5 minutos pelo scheduler.
    }
}
