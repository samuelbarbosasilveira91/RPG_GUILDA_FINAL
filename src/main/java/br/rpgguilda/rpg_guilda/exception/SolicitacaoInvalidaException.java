package br.rpgguilda.rpg_guilda.exception;

import java.util.List;

public class SolicitacaoInvalidaException extends RuntimeException {

    private final List<String> detalhes;

    public SolicitacaoInvalidaException(List<String> detalhes) {
        super("Solicitação inválida");
        this.detalhes = detalhes;
    }

    public List<String> getDetalhes() {
        return detalhes;
    }
}
