package br.com.promanage.dao;

import br.com.promanage.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Método para salvar um novo usuário no banco de dados
    public void salvar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome_completo, cpf, email, cargo, login, senha, perfil) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNomeCompleto());
            stmt.setString(2, usuario.getCpf());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getCargo());
            stmt.setString(5, usuario.getLogin());
            stmt.setString(6, usuario.getSenha());
            stmt.setString(7, usuario.getPerfil());
            
            stmt.executeUpdate();
            System.out.println("Usuário salvo com sucesso!");
        }
    }

    // Método para buscar um usuário pelo login
    public Usuario buscarPorLogin(String login) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE login = ?";
        Usuario usuario = null;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("id_usuario"));
                    usuario.setNomeCompleto(rs.getString("nome_completo"));
                    usuario.setCpf(rs.getString("cpf"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setCargo(rs.getString("cargo"));
                    usuario.setLogin(rs.getString("login"));
                    usuario.setSenha(rs.getString("senha"));
                    usuario.setPerfil(rs.getString("perfil"));
                }
            }
        }
        return usuario;
    }

    // Método para buscar todos os usuários do banco de dados
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
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCargo(rs.getString("cargo"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPerfil(rs.getString("perfil"));
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }
    
    
    public List<Usuario> buscarGerentes() throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE perfil = 'Gerente'";
        List<Usuario> gerentes = new ArrayList<>();

        try (Connection conn = Conexao.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
           ResultSet rs = stmt.executeQuery()) {

          while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setNomeCompleto(rs.getString("nome_completo"));
                usuario.setCpf(rs.getString("cpf"));
                usuario.setEmail(rs.getString("email"));
                usuario.setCargo(rs.getString("cargo"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setPerfil(rs.getString("perfil"));
                gerentes.add(usuario);
            }
        }
        return gerentes;
    }
}