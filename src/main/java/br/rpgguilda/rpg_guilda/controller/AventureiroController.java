package br.rpgguilda.rpg_guilda.controller;

import br.rpgguilda.rpg_guilda.dto.AtualizarAventureiroDTO;
import br.rpgguilda.rpg_guilda.dto.AventureiroResumoDTO;
import br.rpgguilda.rpg_guilda.dto.RegistrarAventureiroDTO;
import br.rpgguilda.rpg_guilda.model.Aventureiro;
import br.rpgguilda.rpg_guilda.model.Classe;
import br.rpgguilda.rpg_guilda.model.Companheiro;
import br.rpgguilda.rpg_guilda.service.AventureiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aventureiros")
public class AventureiroController {

    @Autowired
    private AventureiroService service;

    @PostMapping
    public ResponseEntity<Aventureiro> criar(@RequestBody RegistrarAventureiroDTO dto) {
        Aventureiro novo = service.registrar(dto);
        return ResponseEntity.status(201).body(novo);
    }

    @GetMapping
    public ResponseEntity<List<AventureiroResumoDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Classe classe,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(required = false) Integer nivelMinimo
    ) {
        List<AventureiroResumoDTO> pagina = service.listarResumo(page, size, classe, ativo, nivelMinimo);
        int total = service.totalFiltrado(classe, ativo, nivelMinimo);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(total));
        headers.add("X-Page", String.valueOf(page));
        headers.add("X-Size", String.valueOf(size));
        headers.add("X-Total-Pages", String.valueOf((int) Math.ceil((double) total / size)));

        return ResponseEntity.ok().headers(headers).body(pagina);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aventureiro> buscarPorId(@PathVariable Long id) {
        Aventureiro aventureiro = service.buscarPorId(id);
        return ResponseEntity.ok(aventureiro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aventureiro> atualizar(@PathVariable Long id, @RequestBody AtualizarAventureiroDTO dto) {
        Aventureiro atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Aventureiro> desativar(@PathVariable Long id) {
        Aventureiro aventureiro = service.desativar(id);
        return ResponseEntity.ok(aventureiro);
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Aventureiro> reativar(@PathVariable Long id) {
        Aventureiro aventureiro = service.reativar(id);
        return ResponseEntity.ok(aventureiro);
    }

    @PutMapping("/{id}/companheiro")
    public ResponseEntity<Aventureiro> definirCompanheiro(@PathVariable Long id, @RequestBody Companheiro companheiro) {
        Aventureiro aventureiro = service.definirOuSubstituirCompanheiro(id, companheiro);
        return ResponseEntity.ok(aventureiro);
    }

    @DeleteMapping("/{id}/companheiro")
    public ResponseEntity<Aventureiro> removerCompanheiro(@PathVariable Long id) {
        Aventureiro aventureiro = service.removerCompanheiro(id);
        return ResponseEntity.ok(aventureiro);
    }
}
