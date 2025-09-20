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
    private final ProjetoView projetoView;
    private Projeto projeto;

    private JTextField campoNome;
    private JTextArea campoDescricao;
    private JFormattedTextField campoDataInicio;
    private JFormattedTextField campoDataTerminoPrevista; // Novo nome da variável
    private JFormattedTextField campoDataTerminoReal;    // Novo campo
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
        
        setSize(400, 500);
        setLocationRelativeTo(mainFrame);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
        
        // Campo 'Data Término Prevista' renomeado
        formPanel.add(new JLabel("Data Término Prevista (dd/MM/yyyy):"));
        try {
            campoDataTerminoPrevista = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
            campoDataTerminoPrevista.setColumns(10);
        } catch (IllegalArgumentException e) {
            campoDataTerminoPrevista = new JFormattedTextField();
        }
        formPanel.add(campoDataTerminoPrevista);
        
        // Novo campo 'Data Término Real'
        formPanel.add(new JLabel("Data Término Real (dd/MM/yyyy):"));
        try {
            campoDataTerminoReal = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
            campoDataTerminoReal.setColumns(10);
        } catch (IllegalArgumentException e) {
            campoDataTerminoReal = new JFormattedTextField();
        }
        campoDataTerminoReal.setEditable(false); // Por padrão, não editável
        formPanel.add(campoDataTerminoReal);

        formPanel.add(new JLabel("Status:"));
        String[] statusOptions = {"Em Andamento", "Concluído", "Pendente", "Cancelado"};
        campoStatus = new JComboBox<>(statusOptions);
        // Adiciona um listener para habilitar o campo de 'Data Término Real'
        campoStatus.addActionListener(e -> {
            boolean isConcluido = "Concluído".equals(campoStatus.getSelectedItem());
            campoDataTerminoReal.setEditable(isConcluido);
            if (!isConcluido) {
                campoDataTerminoReal.setText("");
            }
        });
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
        
        // Preenche o campo de Data Término Prevista
        campoDataTerminoPrevista.setText(dateFormat.format(projeto.getDataTerminoPrevista()));
        campoDataTerminoPrevista.setEditable(false); // Desabilita edição
        
        // Preenche o campo de Data Término Real, se existir
        if (projeto.getDataTermino() != null) {
            campoDataTerminoReal.setText(dateFormat.format(projeto.getDataTermino()));
        }
        
        campoStatus.setSelectedItem(projeto.getStatus());
        boolean isConcluido = "Concluído".equals(projeto.getStatus());
        campoDataTerminoReal.setEditable(isConcluido);
        
        campoGerente.setSelectedItem(projeto.getGerenteResponsavel());
        campoEquipe.setSelectedItem(projeto.getEquipeResponsavel());
    }

    private void salvarProjeto() {
        try {
            ProjetoDAO projetoDAO = new ProjetoDAO();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            if (this.projeto == null) {
                this.projeto = new Projeto();
            }
            
            this.projeto.setNome(campoNome.getText());
            this.projeto.setDescricao(campoDescricao.getText());
            this.projeto.setDataInicio(dateFormat.parse(campoDataInicio.getText()));
            
            // Salva a 'Data Término Prevista'
            this.projeto.setDataTerminoPrevista(dateFormat.parse(campoDataTerminoPrevista.getText()));
            
            // Salva a 'Data Término Real' apenas se o campo estiver preenchido
            if (campoDataTerminoReal.isEditable() && !campoDataTerminoReal.getText().isEmpty()) {
                this.projeto.setDataTermino(dateFormat.parse(campoDataTerminoReal.getText()));
            } else {
                this.projeto.setDataTermino(null);
            }
            
            this.projeto.setStatus((String) campoStatus.getSelectedItem());
            this.projeto.setGerenteResponsavel((Usuario) campoGerente.getSelectedItem());
            this.projeto.setEquipeResponsavel((Equipe) campoEquipe.getSelectedItem());
            
            if (this.projeto.getIdProjeto() == 0) { // Novo projeto
                projetoDAO.salvar(this.projeto);
                JOptionPane.showMessageDialog(this, "Projeto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else { // Atualização
                projetoDAO.atualizar(this.projeto);
                JOptionPane.showMessageDialog(this, "Projeto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            projetoView.carregarProjetos();
            dispose();
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato de data. Use o formato dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o projeto: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}