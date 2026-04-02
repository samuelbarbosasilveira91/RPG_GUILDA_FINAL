package br.rpgguilda.rpg_guilda.dto;

import br.rpgguilda.rpg_guilda.model.Aventureiro;
import br.rpgguilda.rpg_guilda.model.Classe;

public class AventureiroResumoDTO {

    private Long id;
    private String nome;
    private Classe classe;
    private int nivel;
    private boolean ativo;

    public AventureiroResumoDTO() {
    }

    public AventureiroResumoDTO(Aventureiro a) {
        this.id = a.getId();
        this.nome = a.getNome();
        this.classe = a.getClasse();
        this.nivel = a.getNivel();
        this.ativo = a.isAtivo();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
