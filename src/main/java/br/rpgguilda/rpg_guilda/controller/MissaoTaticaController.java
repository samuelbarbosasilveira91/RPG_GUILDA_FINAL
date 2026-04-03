package br.rpgguilda.rpg_guilda.controller;

import br.rpgguilda.rpg_guilda.operacoes.entity.MvPainelTaticoMissao;
import br.rpgguilda.rpg_guilda.service.MissaoTaticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para disponibilizar o ranking das missões táticas.
 *
 * Endpoint: GET /missoes/top15dias
 * Retorna as 10 missões mais relevantes dos últimos 15 dias,
 * ordenadas por índice de prontidão decrescente.
 */
@RestController
@RequestMapping("/missoes")
public class MissaoTaticaController {

    @Autowired
    private MissaoTaticaService service;

    /**
     * Endpoint que retorna o ranking tático das missões.
     *
     * @return Top 10 missões dos últimos 15 dias ordenadas por índice de prontidão
     */
    @GetMapping("/top15dias")
    public ResponseEntity<List<MvPainelTaticoMissao>> topMissoesTaticas() {
        List<MvPainelTaticoMissao> missoes = service.buscarTop10UltimosDias();
        return ResponseEntity.ok(missoes);
    }
}
