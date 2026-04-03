package br.rpgguilda.rpg_guilda.dto;

public class AggregationResultDTO {
    private String chave;
    private Long quantidade;

    public AggregationResultDTO(String chave, Long quantidade) {
        this.chave = chave;
        this.quantidade = quantidade;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }
}
