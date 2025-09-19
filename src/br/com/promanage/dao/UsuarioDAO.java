package br.com.promanage.dao;

import br.com.promanage.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    // Método que autentica o usuário com base no login e senha
    public Usuario autenticar(String login, String senha) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        Usuario usuario = null;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            stmt.setString(2, senha);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNomeCompleto(rs.getString("nome_completo"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setCargo(rs.getString("cargo"));
                }
            }
        }
        return usuario;
    }
    
    public List<Usuario> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
             
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNomeCompleto(rs.getString("nome_completo"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCargo(rs.getString("cargo"));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
}