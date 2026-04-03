package br.rpgguilda.rpg_guilda.service;

import br.rpgguilda.rpg_guilda.dto.AggregationResultDTO;
import br.rpgguilda.rpg_guilda.elasticsearch.document.ProdutoDocument;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.RangeBucket;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para buscas e agregações no Elasticsearch (índice guilda_loja).
 */
@Service
public class ProdutoSearchService {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    // ==========================================
    // PARTE A - Buscas Textuais
    // ==========================================

    public List<ProdutoDocument> buscarPorNome(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("nome").query(termo)))
                .build();
        return executarBusca(query);
    }

    public List<ProdutoDocument> buscarPorDescricao(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("descricao").query(termo)))
                .build();
        return executarBusca(query);
    }

    public List<ProdutoDocument> buscarPorFraseExata(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.matchPhrase(m -> m.field("descricao").query(termo)))
                .build();
        return executarBusca(query);
    }

    public List<ProdutoDocument> buscarFuzzy(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.match(m -> m.field("nome").query(termo).fuzziness("AUTO")))
                .build();
        return executarBusca(query);
    }

    public List<ProdutoDocument> buscarMulticampos(String termo) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.multiMatch(m -> m.fields("nome", "descricao").query(termo)))
                .build();
        return executarBusca(query);
    }

    // ==========================================
    // PARTE B - Buscas com Filtros
    // ==========================================

    public List<ProdutoDocument> buscarComFiltroCategoria(String termo, String categoria) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .must(m -> m.match(match -> match.field("descricao").query(termo)))
                        .filter(f -> f.term(t -> t.field("categoria.keyword").value(categoria)))
                ))
                .build();
        return executarBusca(query);
    }

    public List<ProdutoDocument> buscarPorFaixaPreco(Double min, Double max) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.range(r -> r
                        .field("preco")
                        .gte(co.elastic.clients.json.JsonData.of(min))
                        .lte(co.elastic.clients.json.JsonData.of(max))
                ))
                .build();
        return executarBusca(query);
    }

    public List<ProdutoDocument> buscarAvancada(String categoria, String raridade, Double min, Double max) {
        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> b
                        .filter(f -> f.term(t -> t.field("categoria.keyword").value(categoria)))
                        .filter(f -> f.term(t -> t.field("raridade.keyword").value(raridade)))
                        .filter(f -> f.range(r -> r
                                .field("preco")
                                .gte(co.elastic.clients.json.JsonData.of(min))
                                .lte(co.elastic.clients.json.JsonData.of(max))
                        ))
                ))
                .build();
        return executarBusca(query);
    }

    private List<ProdutoDocument> executarBusca(NativeQuery query) {
        SearchHits<ProdutoDocument> hits = elasticsearchOperations.search(query, ProdutoDocument.class);
        return hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // ==========================================
    // PARTE C - Agregações
    // ==========================================

    public List<AggregationResultDTO> quantidadePorCategoria() {
        try {
            SearchRequest request = SearchRequest.of(s -> s
                    .index("guilda_loja")
                    .size(0)
                    .aggregations("por_categoria", a -> a
                            .terms(t -> t.field("categoria.keyword"))
                    )
            );
            SearchResponse<Void> response = elasticsearchClient.search(request, Void.class);
            Aggregate aggregate = response.aggregations().get("por_categoria");
            
            List<AggregationResultDTO> resultado = new ArrayList<>();
            for (StringTermsBucket bucket : aggregate.sterms().buckets().array()) {
                resultado.add(new AggregationResultDTO(bucket.key().stringValue(), bucket.docCount()));
            }
            return resultado;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao agregar por categoria", e);
        }
    }

    public List<AggregationResultDTO> quantidadePorRaridade() {
        try {
            SearchRequest request = SearchRequest.of(s -> s
                    .index("guilda_loja")
                    .size(0)
                    .aggregations("por_raridade", a -> a
                            .terms(t -> t.field("raridade.keyword"))
                    )
            );
            SearchResponse<Void> response = elasticsearchClient.search(request, Void.class);
            Aggregate aggregate = response.aggregations().get("por_raridade");
            
            List<AggregationResultDTO> resultado = new ArrayList<>();
            for (StringTermsBucket bucket : aggregate.sterms().buckets().array()) {
                resultado.add(new AggregationResultDTO(bucket.key().stringValue(), bucket.docCount()));
            }
            return resultado;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao agregar por raridade", e);
        }
    }

    public Double precoMedio() {
        try {
            SearchRequest request = SearchRequest.of(s -> s
                    .index("guilda_loja")
                    .size(0)
                    .aggregations("preco_medio", a -> a
                            .avg(avg -> avg.field("preco"))
                    )
            );
            SearchResponse<Void> response = elasticsearchClient.search(request, Void.class);
            Aggregate aggregate = response.aggregations().get("preco_medio");
            return aggregate.avg().value();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular preco medio", e);
        }
    }

    public List<AggregationResultDTO> faixasPreco() {
        try {
            SearchRequest request = SearchRequest.of(s -> s
                    .index("guilda_loja")
                    .size(0)
                    .aggregations("faixas_preco", a -> a
                            .range(r -> r
                                    .field("preco")
                                    .ranges(rg -> rg.to("100.0")) // Abaixo de 100
                                    .ranges(rg -> rg.from("100.0").to("300.0")) // De 100 a 300
                                    .ranges(rg -> rg.from("300.0").to("700.0")) // De 300 a 700
                                    .ranges(rg -> rg.from("700.0")) // Acima de 700
                            )
                    )
            );
            SearchResponse<Void> response = elasticsearchClient.search(request, Void.class);
            Aggregate aggregate = response.aggregations().get("faixas_preco");
            
            List<AggregationResultDTO> resultado = new ArrayList<>();
            for (RangeBucket bucket : aggregate.range().buckets().array()) {
                String desc = (bucket.from() != null ? "A partir de " + bucket.from() : "") + 
                              (bucket.to() != null ? " ate " + bucket.to() : " e acima");
                resultado.add(new AggregationResultDTO(desc, bucket.docCount()));
            }
            return resultado;
        } catch (Exception e) {
            throw new RuntimeException("Erro nas faixas de preco", e);
        }
    }
}
