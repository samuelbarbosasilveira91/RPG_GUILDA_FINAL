package br.rpgguilda.rpg_guilda.elasticsearch.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Documento Elasticsearch mapeando o índice "guilda_loja".
 *
 * Representa um produto do marketplace da guilda com campos:
 * nome, descricao, categoria, raridade e preco.
 *
 * createIndex = false pois o índice já foi criado e populado
 * pelo time de infraestrutura.
 */
@Document(indexName = "guilda_loja", createIndex = false)
public class ProdutoDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String nome;

    @Field(type = FieldType.Text)
    private String descricao;

    @Field(type = FieldType.Text)
    private String categoria;

    @Field(type = FieldType.Text)
    private String raridade;

    @Field(type = FieldType.Double)
    private Double preco;

    // Construtor padrão
    public ProdutoDocument() {}

    // GETTERS E SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getRaridade() {
        return raridade;
    }

    public void setRaridade(String raridade) {
        this.raridade = raridade;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
