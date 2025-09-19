package br.com.promanage.view;

import br.com.promanage.dao.UsuarioDAO;
import br.com.promanage.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginView extends JFrame {

    private JLabel labelLogin;
    private JTextField campoLogin;
    private JLabel labelSenha;
    private JPasswordField campoSenha;
    private JButton botaoLogin;

    public LoginView() {
        // Configurações básicas da janela
        setTitle("Sistema ProManage - Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criação dos componentes
        labelLogin = new JLabel("Login:");
        campoLogin = new JTextField(20);
        labelSenha = new JLabel("Senha:");
        campoSenha = new JPasswordField(20);
        botaoLogin = new JButton("Entrar");

        // Adiciona a lógica ao botão de login
        botaoLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chama o método para lidar com o login
                lidarComLogin();
            }
        });

        // Criação de um painel para organizar os componentes
        JPanel painel = new JPanel(new GridLayout(3, 2, 10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Adiciona os componentes ao painel
        painel.add(labelLogin);
        painel.add(campoLogin);
        painel.add(labelSenha);
        painel.add(campoSenha);
        painel.add(new JLabel());
        painel.add(botaoLogin);

        // Adiciona o painel à janela
        add(painel);
        setVisible(true);
    }
    
    private void lidarComLogin() {
        String login = campoLogin.getText();
        String senha = new String(campoSenha.getPassword());

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        try {
            // CORREÇÃO: Chamada para o método 'autenticar' na DAO
            Usuario usuarioAutenticado = usuarioDAO.autenticar(login, senha);
            
            if (usuarioAutenticado != null) {
                // Login bem-sucedido: abre a nova tela e fecha a atual
                JOptionPane.showMessageDialog(this, "Bem-vindo(a), " + usuarioAutenticado.getNomeCompleto() + "!");
                
                // Abre a nova janela principal, que conterá o painel de controle
                new MainFrame().setVisible(true); 
                
                // Fecha a janela de login
                this.dispose(); 
                
            } else {
                // Login falhou
                JOptionPane.showMessageDialog(this, "Login ou senha incorretos.", "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView());
    }
}