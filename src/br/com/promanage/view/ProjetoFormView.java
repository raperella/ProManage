package br.com.promanage.view;

import br.com.promanage.dao.ProjetoDAO;
import br.com.promanage.dao.UsuarioDAO;
import br.com.promanage.dao.EquipeDAO;
import br.com.promanage.model.Equipe;
import br.com.promanage.model.Projeto;
import br.com.promanage.model.Usuario;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ProjetoFormView extends JDialog {

    private JTextField campoNome;
    private JTextField campoDescricao;
    private JDateChooser campoDataInicio;
    private JDateChooser campoDataTerminoPrevista; // Nome da variável ajustado
    private JComboBox<String> comboStatus;
    private JComboBox<Usuario> comboGerente;
    private JComboBox<Equipe> comboEquipe;
    private JButton botaoSalvar;
    private JButton botaoCancelar;

    private Projeto projetoParaEdicao;

    public ProjetoFormView(JFrame parent) {
        super(parent, "Novo Projeto", true);
        setSize(450, 450);
        setLocationRelativeTo(parent);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; painel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; campoNome = new JTextField(20); painel.add(campoNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; campoDescricao = new JTextField(20); painel.add(campoDescricao, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; painel.add(new JLabel("Data Início:"), gbc);
        gbc.gridx = 1; campoDataInicio = new JDateChooser(); painel.add(campoDataInicio, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; painel.add(new JLabel("Data Término Prevista:"), gbc);
        gbc.gridx = 1; campoDataTerminoPrevista = new JDateChooser(); painel.add(campoDataTerminoPrevista, gbc);

        gbc.gridx = 0; gbc.gridy = 4; painel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; comboStatus = new JComboBox<>(new String[]{"Em Andamento", "Concluído", "Atrasado"});
        painel.add(comboStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 5; painel.add(new JLabel("Gerente Responsável:"), gbc);
        gbc.gridx = 1; 
        try {
            comboGerente = new JComboBox<>(new UsuarioDAO().buscarGerentes().toArray(new Usuario[0]));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar gerentes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        painel.add(comboGerente, gbc);

        gbc.gridx = 0; gbc.gridy = 6; painel.add(new JLabel("Equipe Responsável:"), gbc);
        gbc.gridx = 1; comboEquipe = new JComboBox<>();
        painel.add(comboEquipe, gbc);

        comboGerente.addActionListener(e -> {
            Usuario gerenteSelecionado = (Usuario) comboGerente.getSelectedItem();
            if (gerenteSelecionado != null) {
                carregarEquipesPorGerente(gerenteSelecionado.getIdUsuario());
            }
        });

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoSalvar = new JButton("Salvar");
        botaoCancelar = new JButton("Cancelar");
        painelBotoes.add(botaoSalvar);
        painelBotoes.add(botaoCancelar);

        botaoSalvar.addActionListener(e -> salvarProjeto());
        botaoCancelar.addActionListener(e -> dispose());

        add(painel, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
        
        Usuario gerenteInicial = (Usuario) comboGerente.getSelectedItem();
        if (gerenteInicial != null) {
            carregarEquipesPorGerente(gerenteInicial.getIdUsuario());
        }
    }

    // Construtor para edição
    public ProjetoFormView(JFrame parent, Projeto projeto) {
        this(parent);
        this.projetoParaEdicao = projeto;
        
        campoNome.setText(projeto.getNome());
        campoDescricao.setText(projeto.getDescricao());
        campoDataInicio.setDate(projeto.getDataInicio());
        campoDataTerminoPrevista.setDate(projeto.getDataTerminoPrevista());
        comboStatus.setSelectedItem(projeto.getStatus());
        
        comboGerente.setSelectedItem(projeto.getGerenteResponsavel());
        comboEquipe.setSelectedItem(projeto.getEquipeResponsavel());
        
        setTitle("Editar Projeto");
        botaoSalvar.setText("Atualizar");
    }

    private void salvarProjeto() {
        try {
            ProjetoDAO dao = new ProjetoDAO();
            
            if (projetoParaEdicao != null) {
                projetoParaEdicao.setNome(campoNome.getText());
                projetoParaEdicao.setDescricao(campoDescricao.getText());
                projetoParaEdicao.setDataInicio(campoDataInicio.getDate());
                projetoParaEdicao.setDataTerminoPrevista(campoDataTerminoPrevista.getDate());
                projetoParaEdicao.setStatus((String) comboStatus.getSelectedItem());
                projetoParaEdicao.setGerenteResponsavel((Usuario) comboGerente.getSelectedItem());
                projetoParaEdicao.setEquipeResponsavel((Equipe) comboEquipe.getSelectedItem());
                
                dao.atualizar(projetoParaEdicao);
                JOptionPane.showMessageDialog(this, "Projeto atualizado com sucesso!");
            } else {
                Projeto novoProjeto = new Projeto();
                novoProjeto.setNome(campoNome.getText());
                novoProjeto.setDescricao(campoDescricao.getText());
                novoProjeto.setDataInicio(campoDataInicio.getDate());
                novoProjeto.setDataTerminoPrevista(campoDataTerminoPrevista.getDate());
                novoProjeto.setStatus((String) comboStatus.getSelectedItem());
                novoProjeto.setGerenteResponsavel((Usuario) comboGerente.getSelectedItem());
                novoProjeto.setEquipeResponsavel((Equipe) comboEquipe.getSelectedItem());
                
                dao.salvar(novoProjeto);
                JOptionPane.showMessageDialog(this, "Projeto salvo com sucesso!");
            }

            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar projeto: Verifique os dados e a conexão.", "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void carregarEquipesPorGerente(int idGerente) {
        try {
            List<Equipe> equipes = new EquipeDAO().buscarPorGerenteId(idGerente);
            
            comboEquipe.removeAllItems();
            for (Equipe equipe : equipes) {
                comboEquipe.addItem(equipe);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar equipes do gerente.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}