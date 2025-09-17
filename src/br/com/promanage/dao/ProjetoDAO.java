package br.com.promanage.dao;

import br.com.promanage.model.Equipe;
import br.com.promanage.model.Projeto;
import br.com.promanage.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ProjetoDAO {

    // Método para salvar um novo projeto
    public void salvar(Projeto projeto) throws SQLException {
        String sql = "INSERT INTO projetos (nome, descricao, data_inicio, data_termino_prevista, status, gerente_responsavel_id, equipe_responsavel_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projeto.getNome());
            stmt.setString(2, projeto.getDescricao());
            stmt.setDate(3, new java.sql.Date(projeto.getDataInicio().getTime()));
            stmt.setDate(4, new java.sql.Date(projeto.getDataTerminoPrevista().getTime()));
            stmt.setString(5, projeto.getStatus());
            stmt.setInt(6, projeto.getGerenteResponsavel().getIdUsuario());
            stmt.setInt(7, projeto.getEquipeResponsavel().getIdEquipe());

            stmt.executeUpdate();
            System.out.println("Projeto salvo com sucesso!");
        }
    }

    // Método para atualizar um projeto existente
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
            System.out.println("Projeto atualizado com sucesso!");
        }
    }

    // Método para buscar todos os projetos, incluindo Gerente e Equipe
    public List<Projeto> buscarTodos() throws SQLException {
        String sql = "SELECT p.*, u.nome_completo AS nome_gerente, e.nome AS nome_equipe FROM projetos p " +
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

    // Método para buscar um projeto por ID, incluindo Gerente e Equipe
    public Projeto buscarPorId(int idProjeto) throws SQLException {
        String sql = "SELECT p.*, u.nome_completo AS nome_gerente, e.nome AS nome_equipe FROM projetos p " +
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
    
    public Map<String, Long> contarProjetosPorEquipe() throws SQLException {
        String sql = "SELECT e.nome, COUNT(p.id_projeto) AS total FROM projetos p " +
                     "JOIN equipes e ON p.equipe_responsavel_id = e.id_equipe " +
                     "GROUP BY e.nome";

        Map<String, Long> contagemPorEquipe = new HashMap<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nomeEquipe = rs.getString("nome");
                Long total = rs.getLong("total");
                contagemPorEquipe.put(nomeEquipe, total);
            }
        }
        return contagemPorEquipe;
    }

    public Map<String, Long> contarProjetosAtrasadosPorEquipe() throws SQLException {
        String sql = "SELECT e.nome, COUNT(p.id_projeto) AS total FROM projetos p " +
                     "JOIN equipes e ON p.equipe_responsavel_id = e.id_equipe " +
                     "WHERE p.status = 'Atrasado' " +
                     "GROUP BY e.nome";

        Map<String, Long> contagemAtrasados = new HashMap<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String nomeEquipe = rs.getString("nome");
                Long total = rs.getLong("total");
                contagemAtrasados.put(nomeEquipe, total);
            }
        }
        return contagemAtrasados;
    }

    public List<Projeto> buscarProjetosVencendo() throws SQLException {
        String sql = "SELECT p.*, u.nome_completo AS nome_gerente, e.nome AS nome_equipe " +
                     "FROM projetos p " +
                     "JOIN usuarios u ON p.gerente_responsavel_id = u.id_usuario " +
                     "JOIN equipes e ON p.equipe_responsavel_id = e.id_equipe " +
                     "WHERE p.data_termino_prevista BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY)";

        List<Projeto> projetosVencendo = new ArrayList<>();

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

                projetosVencendo.add(projeto);
            }
        }
        return projetosVencendo;
    }
}