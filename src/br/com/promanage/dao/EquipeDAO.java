package br.com.promanage.dao;

import br.com.promanage.model.Equipe;
import br.com.promanage.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipeDAO {

    // Método para salvar uma nova equipe e seus membros
    public void salvar(Equipe equipe) throws SQLException {
        // SQL para inserir na tabela de equipes
        String sqlEquipe = "INSERT INTO equipes (nome, descricao, gerente_responsavel_id) VALUES (?, ?, ?)";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtEquipe = conn.prepareStatement(sqlEquipe, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // 1. Salva a equipe
            stmtEquipe.setString(1, equipe.getNome());
            stmtEquipe.setString(2, equipe.getDescricao());
            stmtEquipe.setInt(3, equipe.getGerenteResponsavel().getIdUsuario());
            stmtEquipe.executeUpdate();
            
            // Pega o ID gerado da nova equipe
            try (ResultSet generatedKeys = stmtEquipe.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    equipe.setIdEquipe(generatedKeys.getInt(1));
                }
            }
            
            // 2. Salva os membros da equipe
            if (equipe.getMembros() != null && !equipe.getMembros().isEmpty()) {
                salvarMembros(conn, equipe);
            }

            System.out.println("Equipe e membros salvos com sucesso!");
        }
    }
    
    // Método auxiliar para salvar os membros
    private void salvarMembros(Connection conn, Equipe equipe) throws SQLException {
        String sqlMembro = "INSERT INTO equipe_membros (id_equipe, id_usuario) VALUES (?, ?)";

        try (PreparedStatement stmtMembros = conn.prepareStatement(sqlMembro)) {
            for (Usuario membro : equipe.getMembros()) {
                stmtMembros.setInt(1, equipe.getIdEquipe());
                stmtMembros.setInt(2, membro.getIdUsuario());
                stmtMembros.executeUpdate();
            }
        }
    }
    
    // Método para buscar todos os membros de uma equipe pelo ID
    public List<Usuario> buscarMembrosPorEquipeId(int idEquipe) throws SQLException {
        String sql = "SELECT u.* FROM usuarios u " +
                     "JOIN equipe_membros em ON u.id_usuario = em.id_usuario " +
                     "WHERE em.id_equipe = ?";
        List<Usuario> membros = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idEquipe);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNomeCompleto(rs.getString("nome_completo"));
                    usuario.setLogin(rs.getString("login"));
                    // Você pode adicionar mais atributos do usuário se precisar
                    membros.add(usuario);
                }
            }
        }
        return membros;
    }
    
    
// Método para buscar todas as equipes
    public List<Equipe> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM equipes";
        List<Equipe> equipes = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
        
            while (rs.next()) {
                Equipe equipe = new Equipe();
                equipe.setIdEquipe(rs.getInt("id_equipe"));
                equipe.setNome(rs.getString("nome"));
                equipe.setDescricao(rs.getString("descricao"));
                equipes.add(equipe);
            }
        }
        return equipes;
    }

// Método para buscar equipes com base no ID do gerente
    public List<Equipe> buscarPorGerenteId(int idGerente) throws SQLException {
        String sql = "SELECT * FROM equipes WHERE gerente_responsavel_id = ?";
        List<Equipe> equipes = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idGerente);
        
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Equipe equipe = new Equipe();
                    equipe.setIdEquipe(rs.getInt("id_equipe"));
                    equipe.setNome(rs.getString("nome"));
                    equipe.setDescricao(rs.getString("descricao"));
                    equipes.add(equipe);
                }
            }
        }
        return equipes;
    }
}