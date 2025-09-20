package br.com.promanage.view;

import br.com.promanage.dao.TarefaDAO;
import br.com.promanage.dao.UsuarioDAO;
import br.com.promanage.model.Projeto;
import br.com.promanage.model.Tarefa;
import br.com.promanage.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TarefaFormView extends JDialog {

    private TarefaView tarefaView;
    private Projeto projeto;
    private Tarefa tarefa;

    private JTextField campoTitulo;
    private JTextArea campoDescricao;
    private JComboBox<Usuario> campoResponsavel;
    private JComboBox<String> campoStatus;
    private JFormattedTextField campoDataInicioPrevista;
    private JFormattedTextField campoDataFimPrevista;
    private JFormattedTextField campoDataInicioReal;
    private JFormattedTextField campoDataFimReal;
    private JButton botaoSalvar;
    private JButton botaoCancelar;

    // Construtor para uma NOVA tarefa
    public TarefaFormView(Frame owner, TarefaView tarefaView, Projeto projeto) {
        super(owner, "Adicionar Nova Tarefa", true);
        this.tarefaView = tarefaView;
        this.projeto = projeto;
        this.tarefa = null;
        setupUI();
    }

    // Construtor para EDITAR uma tarefa existente
    public TarefaFormView(Frame owner, TarefaView tarefaView, Tarefa tarefa) {
        super(owner, "Editar Tarefa", true);
        this.tarefaView = tarefaView;
        this.projeto = tarefa.getProjetoVinculado();
        this.tarefa = tarefa;
        setupUI();
        preencherCampos();
    }

    private void setupUI() {
        setSize(450, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Campos do formulário
        formPanel.add(new JLabel("Título:"));
        campoTitulo = new JTextField();
        formPanel.add(campoTitulo);

        formPanel.add(new JLabel("Descrição:"));
        campoDescricao = new JTextArea(3, 20);
        JScrollPane scrollDescricao = new JScrollPane(campoDescricao);
        formPanel.add(scrollDescricao);

        formPanel.add(new JLabel("Responsável:"));
        campoResponsavel = new JComboBox<>();
        carregarUsuarios();
        formPanel.add(campoResponsavel);

        formPanel.add(new JLabel("Status:"));
        String[] statusOptions = {"A Fazer", "Em Andamento", "Concluído", "Cancelado"};
        campoStatus = new JComboBox<>(statusOptions);
        campoStatus.addActionListener(e -> gerenciarCamposDeDataReal());
        formPanel.add(campoStatus);

        formPanel.add(new JLabel("Início Previsto (dd/MM/yyyy):"));
        try {
            campoDataInicioPrevista = new JFormattedTextField(dateFormat);
        } catch (IllegalArgumentException e) {
            campoDataInicioPrevista = new JFormattedTextField();
        }
        formPanel.add(campoDataInicioPrevista);

        formPanel.add(new JLabel("Fim Previsto (dd/MM/yyyy):"));
        try {
            campoDataFimPrevista = new JFormattedTextField(dateFormat);
        } catch (IllegalArgumentException e) {
            campoDataFimPrevista = new JFormattedTextField();
        }
        formPanel.add(campoDataFimPrevista);

        formPanel.add(new JLabel("Início Real (dd/MM/yyyy):"));
        try {
            campoDataInicioReal = new JFormattedTextField(dateFormat);
        } catch (IllegalArgumentException e) {
            campoDataInicioReal = new JFormattedTextField();
        }
        campoDataInicioReal.setEnabled(false);
        formPanel.add(campoDataInicioReal);

        formPanel.add(new JLabel("Fim Real (dd/MM/yyyy):"));
        try {
            campoDataFimReal = new JFormattedTextField(dateFormat);
        } catch (IllegalArgumentException e) {
            campoDataFimReal = new JFormattedTextField();
        }
        campoDataFimReal.setEnabled(false);
        formPanel.add(campoDataFimReal);

        add(formPanel, BorderLayout.CENTER);

        // Painel para os botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botaoSalvar = new JButton("Salvar");
        botaoSalvar.addActionListener(e -> salvarTarefa());
        botaoCancelar = new JButton("Cancelar");
        botaoCancelar.addActionListener(e -> dispose());
        buttonPanel.add(botaoSalvar);
        buttonPanel.add(botaoCancelar);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void preencherCampos() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        campoTitulo.setText(tarefa.getTitulo());
        campoDescricao.setText(tarefa.getDescricao());
        campoResponsavel.setSelectedItem(tarefa.getResponsavel());
        campoStatus.setSelectedItem(tarefa.getStatus());
        campoDataInicioPrevista.setText(dateFormat.format(tarefa.getDataInicioPrevista()));
        campoDataFimPrevista.setText(dateFormat.format(tarefa.getDataFimPrevista()));
        
        if (tarefa.getDataInicioReal() != null) {
            campoDataInicioReal.setText(dateFormat.format(tarefa.getDataInicioReal()));
        }
        if (tarefa.getDataFimReal() != null) {
            campoDataFimReal.setText(dateFormat.format(tarefa.getDataFimReal()));
        }
        
        gerenciarCamposDeDataReal();
    }

    private void gerenciarCamposDeDataReal() {
        String statusSelecionado = (String) campoStatus.getSelectedItem();
        boolean enable = "Em Andamento".equals(statusSelecionado) || "Concluído".equals(statusSelecionado);
        campoDataInicioReal.setEnabled(enable);
        
        boolean enableFim = "Concluído".equals(statusSelecionado);
        campoDataFimReal.setEnabled(enableFim);
        
        if (!enable) {
            campoDataInicioReal.setText("");
            campoDataFimReal.setText("");
        } else if (!enableFim) {
             campoDataFimReal.setText("");
        }
    }
    
    private void carregarUsuarios() {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> usuarios = usuarioDAO.buscarTodos();
            for (Usuario u : usuarios) {
                campoResponsavel.addItem(u);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void salvarTarefa() {
        try {
            TarefaDAO tarefaDAO = new TarefaDAO();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            if (this.tarefa == null) {
                this.tarefa = new Tarefa();
                this.tarefa.setProjetoVinculado(this.projeto);
            }

            this.tarefa.setTitulo(campoTitulo.getText());
            this.tarefa.setDescricao(campoDescricao.getText());
            this.tarefa.setResponsavel((Usuario) campoResponsavel.getSelectedItem());
            this.tarefa.setStatus((String) campoStatus.getSelectedItem());
            this.tarefa.setDataInicioPrevista(dateFormat.parse(campoDataInicioPrevista.getText()));
            this.tarefa.setDataFimPrevista(dateFormat.parse(campoDataFimPrevista.getText()));

            if (campoDataInicioReal.isEnabled() && !campoDataInicioReal.getText().isEmpty()) {
                this.tarefa.setDataInicioReal(dateFormat.parse(campoDataInicioReal.getText()));
            } else {
                this.tarefa.setDataInicioReal(null);
            }
            if (campoDataFimReal.isEnabled() && !campoDataFimReal.getText().isEmpty()) {
                this.tarefa.setDataFimReal(dateFormat.parse(campoDataFimReal.getText()));
            } else {
                this.tarefa.setDataFimReal(null);
            }

            if (this.tarefa.getIdTarefa() == 0) {
                tarefaDAO.salvar(this.tarefa);
                JOptionPane.showMessageDialog(this, "Tarefa salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                tarefaDAO.atualizar(this.tarefa);
                JOptionPane.showMessageDialog(this, "Tarefa atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            tarefaView.carregarTarefas(); // Recarrega a tabela principal
            dispose();
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Erro de formato de data. Use o formato dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar a tarefa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}