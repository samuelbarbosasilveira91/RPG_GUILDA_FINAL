package br.rpgguilda.rpg_guilda.dto;

import br.rpgguilda.rpg_guilda.model.Classe;

public class AtualizarAventureiroDTO {

    private String nome;
    private Classe classe;
    private int nivel;

    public AtualizarAventureiroDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}
