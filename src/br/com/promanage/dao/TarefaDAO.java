package br.com.promanage.dao;

import br.com.promanage.model.Projeto;
import br.com.promanage.model.Tarefa;
import br.com.promanage.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    // Método para salvar uma nova tarefa no banco de dados
    public void salvar(Tarefa tarefa) throws SQLException {
        String sql = "INSERT INTO tarefas (titulo, descricao, projeto_id, responsavel_id, status, data_inicio_prevista, data_fim_prevista, data_inicio_real, data_fim_real) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getTitulo());
            stmt.setString(2, tarefa.getDescricao());
            stmt.setInt(3, tarefa.getProjetoVinculado().getIdProjeto());
            stmt.setInt(4, tarefa.getResponsavel().getIdUsuario());
            stmt.setString(5, tarefa.getStatus());
            stmt.setDate(6, new java.sql.Date(tarefa.getDataInicioPrevista().getTime()));
            stmt.setDate(7, new java.sql.Date(tarefa.getDataFimPrevista().getTime()));
            stmt.setDate(8, tarefa.getDataInicioReal() != null ? new java.sql.Date(tarefa.getDataInicioReal().getTime()) : null);
            stmt.setDate(9, tarefa.getDataFimReal() != null ? new java.sql.Date(tarefa.getDataFimReal().getTime()) : null);
            
            stmt.executeUpdate();
            System.out.println("Tarefa salva com sucesso!");
        }
    }
    
    // Método para buscar todas as tarefas de um projeto específico
    public List<Tarefa> buscarPorProjetoId(int projetoId) throws SQLException {
        String sql = "SELECT t.*, p.nome as nome_projeto, u.nome_completo as nome_responsavel FROM tarefas t " +
                     "JOIN projetos p ON t.projeto_id = p.id_projeto " +
                     "JOIN usuarios u ON t.responsavel_id = u.id_usuario " +
                     "WHERE t.projeto_id = ?";
        List<Tarefa> tarefas = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, projetoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tarefa tarefa = new Tarefa();
                    tarefa.setIdTarefa(rs.getInt("id_tarefa"));
                    tarefa.setTitulo(rs.getString("titulo"));
                    tarefa.setDescricao(rs.getString("descricao"));
                    tarefa.setStatus(rs.getString("status"));
                    tarefa.setDataInicioPrevista(rs.getDate("data_inicio_prevista"));
                    tarefa.setDataFimPrevista(rs.getDate("data_fim_prevista"));
                    tarefa.setDataInicioReal(rs.getDate("data_inicio_real"));
                    tarefa.setDataFimReal(rs.getDate("data_fim_real"));
                    
                    // Constrói os objetos de relacionamento
                    Projeto projeto = new Projeto();
                    projeto.setIdProjeto(rs.getInt("projeto_id"));
                    projeto.setNome(rs.getString("nome_projeto"));
                    tarefa.setProjetoVinculado(projeto);
                    
                    Usuario responsavel = new Usuario();
                    responsavel.setIdUsuario(rs.getInt("responsavel_id"));
                    responsavel.setNomeCompleto(rs.getString("nome_responsavel"));
                    tarefa.setResponsavel(responsavel);
                    
                    tarefas.add(tarefa);
                }
            }
        }
        return tarefas;
    }
}