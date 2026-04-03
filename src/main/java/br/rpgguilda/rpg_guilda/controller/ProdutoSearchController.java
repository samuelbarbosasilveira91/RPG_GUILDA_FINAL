package br.rpgguilda.rpg_guilda.controller;

import br.rpgguilda.rpg_guilda.dto.AggregationResultDTO;
import br.rpgguilda.rpg_guilda.elasticsearch.document.ProdutoDocument;
import br.rpgguilda.rpg_guilda.service.ProdutoSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para consumir a busca do Marketplace (Elasticsearch).
 */
@RestController
@RequestMapping("/produtos")
public class ProdutoSearchController {

    @Autowired
    private ProdutoSearchService searchService;

    // ==========================================
    // PARTE A - Buscas Textuais
    // ==========================================

    @GetMapping("/busca/nome")
    public ResponseEntity<List<ProdutoDocument>> buscarPorNome(@RequestParam String termo) {
        return ResponseEntity.ok(searchService.buscarPorNome(termo));
    }

    @GetMapping("/busca/descricao")
    public ResponseEntity<List<ProdutoDocument>> buscarPorDescricao(@RequestParam String termo) {
        return ResponseEntity.ok(searchService.buscarPorDescricao(termo));
    }

    @GetMapping("/busca/frase")
    public ResponseEntity<List<ProdutoDocument>> buscarPorFraseExata(@RequestParam String termo) {
        return ResponseEntity.ok(searchService.buscarPorFraseExata(termo));
    }

    @GetMapping("/busca/fuzzy")
    public ResponseEntity<List<ProdutoDocument>> buscarFuzzy(@RequestParam String termo) {
        return ResponseEntity.ok(searchService.buscarFuzzy(termo));
    }

    @GetMapping("/busca/multicampos")
    public ResponseEntity<List<ProdutoDocument>> buscarMulticampos(@RequestParam String termo) {
        return ResponseEntity.ok(searchService.buscarMulticampos(termo));
    }

    // ==========================================
    // PARTE B - Buscas com Filtros
    // ==========================================

    @GetMapping("/busca/com-filtro")
    public ResponseEntity<List<ProdutoDocument>> buscarComFiltroCategoria(
            @RequestParam String termo, 
            @RequestParam String categoria) {
        return ResponseEntity.ok(searchService.buscarComFiltroCategoria(termo, categoria));
    }

    @GetMapping("/busca/faixa-preco")
    public ResponseEntity<List<ProdutoDocument>> buscarPorFaixaPreco(
            @RequestParam Double min, 
            @RequestParam Double max) {
        return ResponseEntity.ok(searchService.buscarPorFaixaPreco(min, max));
    }

    @GetMapping("/busca/avancada")
    public ResponseEntity<List<ProdutoDocument>> buscarAvancada(
            @RequestParam String categoria,
            @RequestParam String raridade,
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(searchService.buscarAvancada(categoria, raridade, min, max));
    }

    // ==========================================
    // PARTE C - Agregações
    // ==========================================

    @GetMapping("/agregacoes/por-categoria")
    public ResponseEntity<List<AggregationResultDTO>> quantidadePorCategoria() {
        return ResponseEntity.ok(searchService.quantidadePorCategoria());
    }

    @GetMapping("/agregacoes/por-raridade")
    public ResponseEntity<List<AggregationResultDTO>> quantidadePorRaridade() {
        return ResponseEntity.ok(searchService.quantidadePorRaridade());
    }

    @GetMapping("/agregacoes/preco-medio")
    public ResponseEntity<Double> precoMedio() {
        return ResponseEntity.ok(searchService.precoMedio());
    }

    @GetMapping("/agregacoes/faixas-preco")
    public ResponseEntity<List<AggregationResultDTO>> faixasPreco() {
        return ResponseEntity.ok(searchService.faixasPreco());
    }
}
