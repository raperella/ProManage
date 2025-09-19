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

    public void salvar(Equipe equipe) throws SQLException {
        String sql = "INSERT INTO equipes (nome, descricao, gerente_responsavel_id) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, equipe.getNome());
            stmt.setString(2, equipe.getDescricao());
            stmt.setInt(3, equipe.getGerenteResponsavel().getIdUsuario());
            stmt.executeUpdate();
        }
    }
    
    public void atualizar(Equipe equipe) throws SQLException {
        String sql = "UPDATE equipes SET nome = ?, descricao = ?, gerente_responsavel_id = ? WHERE id_equipe = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, equipe.getNome());
            stmt.setString(2, equipe.getDescricao());
            stmt.setInt(3, equipe.getGerenteResponsavel().getIdUsuario());
            stmt.setInt(4, equipe.getIdEquipe());
            stmt.executeUpdate();
        }
    }
    
    public void excluir(int idEquipe) throws SQLException {
        String sql = "DELETE FROM equipes WHERE id_equipe = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEquipe);
            stmt.executeUpdate();
        }
    }

    public List<Equipe> buscarTodos() throws SQLException {
        String sql = "SELECT e.*, u.nome_completo AS nome_gerente, u.id_usuario AS id_gerente FROM equipes e JOIN usuarios u ON e.gerente_responsavel_id = u.id_usuario";
        List<Equipe> equipes = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Equipe equipe = new Equipe();
                equipe.setIdEquipe(rs.getInt("id_equipe"));
                equipe.setNome(rs.getString("nome"));
                equipe.setDescricao(rs.getString("descricao"));

                Usuario gerente = new Usuario();
                gerente.setIdUsuario(rs.getInt("id_gerente"));
                gerente.setNomeCompleto(rs.getString("nome_gerente"));
                equipe.setGerenteResponsavel(gerente);

                equipes.add(equipe);
            }
        }
        return equipes;
    }
}