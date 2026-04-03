package br.rpgguilda.rpg_guilda.elasticsearch.repository;

import br.rpgguilda.rpg_guilda.elasticsearch.document.ProdutoDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório Spring Data Elasticsearch para o ProdutoDocument.
 */
@Repository
public interface ProdutoSearchRepository extends ElasticsearchRepository<ProdutoDocument, String> {
}
