package br.com.promanage.dao;

import br.com.promanage.model.Equipe;
import br.com.promanage.model.Projeto;
import br.com.promanage.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.time.LocalDate;

public class ProjetoDAO {

    public void salvar(Projeto projeto) throws SQLException {
        String sql = "INSERT INTO projetos (nome, descricao, data_inicio, data_termino_prevista, status, gerente_responsavel_id, equipe_responsavel_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, new java.sql.Date(projeto.getDataInicio().getTime()));
            stmt.setDate(4, new java.sql.Date(projeto.getDataTerminoPrevista().getTime()));
            stmt.setString(5, projeto.getStatus());
            stmt.setInt(6, projeto.getGerenteResponsavel().getIdUsuario());
            stmt.setInt(7, projeto.getEquipeResponsavel().getIdEquipe());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    projeto.setIdProjeto(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void atualizar(Projeto projeto) throws SQLException {
        String sql = "UPDATE projetos SET nome=?, descricao=?, data_inicio=?, data_termino_prevista=?, status=?, gerente_responsavel_id=?, equipe_responsavel_id=? WHERE id_projeto=?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, new java.sql.Date(projeto.getDataInicio().getTime()));
            stmt.setDate(4, new java.sql.Date(projeto.getDataTerminoPrevista().getTime()));
            stmt.setString(5, projeto.getStatus());
            stmt.setInt(6, projeto.getGerenteResponsavel().getIdUsuario());
            stmt.setInt(7, projeto.getEquipeResponsavel().getIdEquipe());
            stmt.setInt(8, projeto.getIdProjeto());

            stmt.executeUpdate();
        }
    }
    
    public void deletar(int idProjeto) throws SQLException {
        String sql = "DELETE FROM projetos WHERE id_projeto = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProjeto);
            stmt.executeUpdate();
        }
    }

    public Projeto buscarPorId(int idProjeto) throws SQLException {
        String sql = "SELECT p.*, u.nome_completo AS nome_gerente, e.nome AS nome_equipe " +
                     "FROM projetos p " +
                     "JOIN usuarios u ON p.gerente_responsavel_id = u.id_usuario " +
                     "JOIN equipes e ON p.equipe_responsavel_id = e.id_equipe " +
                     "WHERE p.id_projeto = ?";
        Projeto projeto = null;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProjeto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    projeto = new Projeto();
                    projeto.setIdProjeto(rs.getInt("id_projeto"));
                    projeto.setNome(rs.getString("nome"));
                    projeto.setDescricao(rs.getString("descricao"));
                    projeto.setDataInicio(rs.getDate("data_inicio"));
                    projeto.setDataTerminoPrevista(rs.getDate("data_termino_prevista"));
                    projeto.setStatus(rs.getString("status"));

                    Usuario gerente = new Usuario();
                    gerente.setIdUsuario(rs.getInt("gerente_responsavel_id"));
                    gerente.setNomeCompleto(rs.getString("nome_gerente"));
                    projeto.setGerenteResponsavel(gerente);

                    Equipe equipe = new Equipe();
                    equipe.setIdEquipe(rs.getInt("equipe_responsavel_id"));
                    equipe.setNome(rs.getString("nome_equipe"));
                    projeto.setEquipeResponsavel(equipe);
                }
            }
        }
        return projeto;
    }

    public List<Projeto> buscarTodos() throws SQLException {
        String sql = "SELECT p.*, u.nome_completo AS nome_gerente, e.nome AS nome_equipe " +
                     "FROM projetos p " +
                     "JOIN usuarios u ON p.gerente_responsavel_id = u.id_usuario " +
                     "JOIN equipes e ON p.equipe_responsavel_id = e.id_equipe";
        List<Projeto> projetos = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Projeto projeto = new Projeto();
                projeto.setIdProjeto(rs.getInt("id_projeto"));
                projeto.setNome(rs.getString("nome"));
                projeto.setDescricao(rs.getString("descricao"));
                projeto.setDataInicio(rs.getDate("data_inicio"));
                projeto.setDataTerminoPrevista(rs.getDate("data_termino_prevista"));
                projeto.setStatus(rs.getString("status"));

                Usuario gerente = new Usuario();
                gerente.setIdUsuario(rs.getInt("gerente_responsavel_id"));
                gerente.setNomeCompleto(rs.getString("nome_gerente"));
                projeto.setGerenteResponsavel(gerente);

                Equipe equipe = new Equipe();
                equipe.setIdEquipe(rs.getInt("equipe_responsavel_id"));
                equipe.setNome(rs.getString("nome_equipe"));
                projeto.setEquipeResponsavel(equipe);

                projetos.add(projeto);
            }
        }
        return projetos;
    }
    
    /**
     * Métodos para o Dashboard
     */
     
    public Map<String, Long> contarProjetosPorEquipe() throws SQLException {
        String sql = "SELECT e.nome, COUNT(p.id_projeto) AS total_projetos FROM projetos p JOIN equipes e ON p.equipe_responsavel_id = e.id_equipe GROUP BY e.nome";
        Map<String, Long> projetosPorEquipe = new HashMap<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                projetosPorEquipe.put(rs.getString("nome"), rs.getLong("total_projetos"));
            }
        }
        return projetosPorEquipe;
    }

    public Map<String, Long> contarProjetosAtrasadosPorEquipe() throws SQLException {
        String sql = "SELECT e.nome, COUNT(p.id_projeto) AS total_atrasados FROM projetos p JOIN equipes e ON p.equipe_responsavel_id = e.id_equipe WHERE p.data_termino_prevista < CURRENT_DATE AND p.status != 'Concluído' GROUP BY e.nome";
        Map<String, Long> projetosAtrasados = new HashMap<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                projetosAtrasados.put(rs.getString("nome"), rs.getLong("total_atrasados"));
            }
        }
        return projetosAtrasados;
    }

    public List<Projeto> buscarProjetosVencendo() throws SQLException {
        String sql = "SELECT p.*, e.nome AS nome_equipe FROM projetos p JOIN equipes e ON p.equipe_responsavel_id = e.id_equipe WHERE p.data_termino_prevista <= DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY) AND p.status != 'Concluído' ORDER BY p.data_termino_prevista ASC";
        List<Projeto> projetos = new ArrayList<>();
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Projeto projeto = new Projeto();
                projeto.setIdProjeto(rs.getInt("id_projeto"));
                projeto.setNome(rs.getString("nome"));
                projeto.setDescricao(rs.getString("descricao"));
                projeto.setDataInicio(rs.getDate("data_inicio"));
                projeto.setDataTerminoPrevista(rs.getDate("data_termino_prevista"));
                projeto.setStatus(rs.getString("status"));
                
                Equipe equipe = new Equipe();
                equipe.setIdEquipe(rs.getInt("equipe_responsavel_id"));
                equipe.setNome(rs.getString("nome_equipe"));
                projeto.setEquipeResponsavel(equipe);

                projetos.add(projeto);
            }
        }
        return projetos;
    }
}