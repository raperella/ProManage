package br.com.promanage.model;

import java.util.Date;

public class Tarefa {

    private int idTarefa;
    private String titulo;
    private String descricao;
    private Projeto projetoVinculado;
    private Usuario responsavel;
    private String status;
    private Date dataInicioPrevista;
    private Date dataFimPrevista;
    private Date dataInicioReal;
    private Date dataFimReal;

    public Tarefa() {
    }

    public int getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(int idTarefa) {
        this.idTarefa = idTarefa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Projeto getProjetoVinculado() {
        return projetoVinculado;
    }

    public void setProjetoVinculado(Projeto projetoVinculado) {
        this.projetoVinculado = projetoVinculado;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDataInicioPrevista() {
        return dataInicioPrevista;
    }

    public void setDataInicioPrevista(Date dataInicioPrevista) {
        this.dataInicioPrevista = dataInicioPrevista;
    }

    public Date getDataFimPrevista() {
        return dataFimPrevista;
    }

    public void setDataFimPrevista(Date dataFimPrevista) {
        this.dataFimPrevista = dataFimPrevista;
    }

    public Date getDataInicioReal() {
        return dataInicioReal;
    }

    public void setDataInicioReal(Date dataInicioReal) {
        this.dataInicioReal = dataInicioReal;
    }

    public Date getDataFimReal() {
        return dataFimReal;
    }

    public void setDataFimReal(Date dataFimReal) {
        this.dataFimReal = dataFimReal;
    }
}