package br.com.promanage.view;

import br.com.promanage.dao.EquipeDAO;
import br.com.promanage.dao.ProjetoDAO;
import br.com.promanage.dao.UsuarioDAO;
import br.com.promanage.model.Equipe;
import br.com.promanage.model.Projeto;
import br.com.promanage.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProjetoFormView extends JDialog {

    private final MainFrame mainFrame;
    private final ProjetoView projetoView; // Referência para a tela principal de projetos
    private Projeto projeto;

    private JTextField campoNome;
    private JTextArea campoDescricao;
    private JFormattedTextField campoDataInicio;
    private JFormattedTextField campoDataTermino;
    private JComboBox<String> campoStatus;
    private JComboBox<Usuario> campoGerente;
    private JComboBox<Equipe> campoEquipe;
    private JButton botaoSalvar;
    private JButton botaoCancelar;

    // Construtor para NOVO projeto
    public ProjetoFormView(MainFrame mainFrame, ProjetoView projetoView) {
        this(mainFrame, projetoView, null);
    }

    // Construtor para EDIÇÃO de projeto
    public ProjetoFormView(MainFrame mainFrame, ProjetoView projetoView, Projeto projeto) {
        super(mainFrame, "Cadastro de Projeto", true);
        this.mainFrame = mainFrame;
        this.projetoView = projetoView;
        this.projeto = projeto;
        
        // Configurações do diálogo
        setSize(400, 500);
        setLocationRelativeTo(mainFrame);
        setLayout(new BorderLayout(10, 10));

        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campos do formulário
        formPanel.add(new JLabel("Nome:"));
        campoNome = new JTextField();
        formPanel.add(campoNome);

        formPanel.add(new JLabel("Descrição:"));
        campoDescricao = new JTextArea(3, 20);
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        formPanel.add(scrollDescricao);

        formPanel.add(new JLabel("Data Início (dd/MM/yyyy):"));
        try {
            campoDataInicio = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
            campoDataInicio.setColumns(10);
        } catch (IllegalArgumentException e) {
            campoDataInicio = new JFormattedTextField();
        }
        formPanel.add(campoDataInicio);

        formPanel.add(new JLabel("Data Término (dd/MM/yyyy):"));
        try {
            campoDataTermino = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
            campoDataTermino.setColumns(10);
        } catch (IllegalArgumentException e) {
            campoDataTermino = new JFormattedTextField();
        }
        formPanel.add(campoDataTermino);

        formPanel.add(new JLabel("Status:"));
        String[] statusOptions = {"Em Andamento", "Concluído", "Pendente", "Cancelado"};
        campoStatus = new JComboBox<>(statusOptions);
        formPanel.add(campoStatus);

        formPanel.add(new JLabel("Gerente:"));
        campoGerente = new JComboBox<>();
        carregarUsuarios();
        formPanel.add(campoGerente);

        formPanel.add(new JLabel("Equipe:"));
        campoEquipe = new JComboBox<>();
        carregarEquipes();
        formPanel.add(campoEquipe);
        
        add(formPanel, BorderLayout.CENTER);

        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botaoSalvar = new JButton("Salvar");
        botaoSalvar.addActionListener(e -> salvarProjeto());
        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(e -> dispose());
        buttonPanel.add(botaoSalvar);
        buttonPanel.add(botaoCancelar);
        add(buttonPanel, BorderLayout.SOUTH);
        
        if (this.projeto != null) {
            preencherCampos();
            setTitle("Editar Projeto");
        } else {
            setTitle("Novo Projeto");
        }
    }

    private void carregarUsuarios() {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> usuarios = usuarioDAO.buscarTodos();
            for (Usuario u : usuarios) {
                campoGerente.addItem(u);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar gerentes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarEquipes() {
        try {
            EquipeDAO equipeDAO = new EquipeDAO();
            List<Equipe> equipes = equipeDAO.buscarTodos();
            for (Equipe eq : equipes) {
                campoEquipe.addItem(eq);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar equipes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void preencherCampos() {
        campoNome.setText(projeto.getNome());
        campoDescricao.setText(projeto.getDescricao());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        campoDataInicio.setText(dateFormat.format(projeto.getDataInicio()));
        campoDataTermino.setText(dateFormat.format(projeto.getDataTerminoPrevista()));
        campoStatus.setSelectedItem(projeto.getStatus());
        campoGerente.setSelectedItem(projeto.getGerenteResponsavel());
        campoEquipe.setSelectedItem(projeto.getEquipeResponsavel());
    }

    private void salvarProjeto() {
        try {
            ProjetoDAO projetoDAO = new ProjetoDAO();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            // Cria ou atualiza o objeto Projeto
            if (this.projeto == null) {
                this.projeto = new Projeto();
            }
            
            this.projeto.setNome(campoNome.getText());
            this.projeto.setDescricao(campoDescricao.getText());
            this.projeto.setDataInicio(dateFormat.parse(campoDataInicio.getText()));
            this.projeto.setDataTerminoPrevista(dateFormat.parse(campoDataTermino.getText()));
            this.projeto.setStatus((String) campoStatus.getSelectedItem());
            this.projeto.setGerenteResponsavel((Usuario) campoGerente.getSelectedItem());
            this.projeto.setEquipeResponsavel((Equipe) campoEquipe.getSelectedItem());
            
            if (this.projeto.getIdProjeto() == 0) { // É um novo projeto
                projetoDAO.salvar(this.projeto);
                JOptionPane.showMessageDialog(this, "Projeto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else { // É uma atualização
                projetoDAO.atualizar(this.projeto);
                JOptionPane.showMessageDialog(this, "Projeto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Recarrega a tabela na tela principal
            projetoView.carregarProjetos();
            dispose(); // Fecha o diálogo
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato de data. Use o formato dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o projeto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}