package br.com.promanage.model;

import java.util.List;
import java.util.ArrayList;

public class Equipe {

    private int idEquipe;
    private String nome;
    private String descricao;
    private Usuario gerenteResponsavel;
    private List<Usuario> membros;

    public Equipe() {
        this.membros = new ArrayList<>();
    }

    public int getIdEquipe() {
        return idEquipe;
    }

    public void setIdEquipe(int idEquipe) {
        this.idEquipe = idEquipe;
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
    
    @Override
    public String toString() {
        return this.nome;
    } 

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Usuario getGerenteResponsavel() {
        return gerenteResponsavel;
    }

    public void setGerenteResponsavel(Usuario gerenteResponsavel) {
        this.gerenteResponsavel = gerenteResponsavel;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }

    public void addMembro(Usuario usuario) {
        this.membros.add(usuario);
    }
}