package br.com.promanage.model;

import java.util.Date;

public class Projeto {

    private int idProjeto;
    private String nome;
    private String descricao;
    private Date dataInicio;
    private Date dataTerminoPrevista;
    private String status;
    private Usuario gerenteResponsavel;
    private Equipe equipeResponsavel;

    public Projeto() {
    }

    public Projeto(String nome, String descricao, Date dataInicio, Date dataTerminoPrevista, String status, Usuario gerenteResponsavel) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataTerminoPrevista = dataTerminoPrevista;
        this.status = status;
        this.gerenteResponsavel = gerenteResponsavel;
    }

    public int getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(int idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setEquipeResponsavel(Equipe equipeResponsavel) {
        this.equipeResponsavel = equipeResponsavel;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public Equipe getEquipeResponsavel() {
        return equipeResponsavel;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTerminoPrevista() {
        return dataTerminoPrevista;
    }

    public void setDataTerminoPrevista(Date dataTerminoPrevista) {
        this.dataTerminoPrevista = dataTerminoPrevista;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Usuario getGerenteResponsavel() {
        return gerenteResponsavel;
    }

    public void setGerenteResponsavel(Usuario gerenteResponsavel) {
        this.gerenteResponsavel = gerenteResponsavel;
    }
}