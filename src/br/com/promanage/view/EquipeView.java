package br.com.promanage.view;

import br.com.promanage.dao.EquipeDAO;
import br.com.promanage.dao.UsuarioDAO;
import br.com.promanage.model.Equipe;
import br.com.promanage.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class EquipeView extends JPanel {

    private MainFrame mainFrame;
    private JTextField campoNome;
    private JTextField campoDescricao;
    private JComboBox<Usuario> comboBoxGerente;
    private JButton botaoSalvar;
    private JButton botaoAtualizar;
    private JButton botaoExcluir;
    private JTable tabelaEquipes;
    private DefaultTableModel tableModel;

    private EquipeDAO equipeDAO;

    public EquipeView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.equipeDAO = new EquipeDAO();
        setLayout(new BorderLayout(10, 10));

        // Painel do Formulário
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Gerenciar Equipes"));

        campoNome = new JTextField(20);
        campoDescricao = new JTextField(20);
        comboBoxGerente = new JComboBox<>();

        formPanel.add(new JLabel("Nome da Equipe:"));
        formPanel.add(campoNome);
        formPanel.add(new JLabel("Descrição:"));
        formPanel.add(campoDescricao);
        formPanel.add(new JLabel("Gerente Responsável:"));
        formPanel.add(comboBoxGerente);

        // Preencher ComboBox de Gerentes
        preencherComboBoxGerentes();

        // Painel dos Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        botaoSalvar = new JButton("Salvar");
        botaoAtualizar = new JButton("Atualizar");
        botaoExcluir = new JButton("Excluir");

        buttonPanel.add(botaoSalvar);
        buttonPanel.add(botaoAtualizar);
        buttonPanel.add(botaoExcluir);

        // Tabela de Equipes
        String[] colunas = {"ID", "Nome", "Descrição", "Gerente"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabelaEquipes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaEquipes);

        // Adicionar Ações aos Botões
        botaoSalvar.addActionListener(e -> salvarEquipe());
        botaoAtualizar.addActionListener(e -> atualizarEquipe());
        botaoExcluir.addActionListener(e -> excluirEquipe());
        
        tabelaEquipes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabelaEquipes.getSelectedRow() != -1) {
                preencherCamposComSelecao();
            }
        });

        // Adicionar painéis à tela principal
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        
        carregarEquipes();
    }
    
    private void preencherComboBoxGerentes() {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            List<Usuario> usuarios = usuarioDAO.buscarTodos();
            for (Usuario usuario : usuarios) {
                comboBoxGerente.addItem(usuario);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar gerentes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void salvarEquipe() {
        if (campoNome.getText().isEmpty() || comboBoxGerente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Nome e Gerente são obrigatórios.", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Equipe equipe = new Equipe();
            equipe.setNome(campoNome.getText());
            equipe.setDescricao(campoDescricao.getText());
            equipe.setGerenteResponsavel((Usuario) comboBoxGerente.getSelectedItem());
            
            equipeDAO.salvar(equipe);
            JOptionPane.showMessageDialog(this, "Equipe salva com sucesso!");
            carregarEquipes();
            limparCampos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar equipe: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void atualizarEquipe() {
        int linhaSelecionada = tabelaEquipes.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma equipe para atualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(linhaSelecionada, 0);
            Equipe equipe = new Equipe();
            equipe.setIdEquipe(id);
            equipe.setNome(campoNome.getText());
            equipe.setDescricao(campoDescricao.getText());
            equipe.setGerenteResponsavel((Usuario) comboBoxGerente.getSelectedItem());
            
            equipeDAO.atualizar(equipe);
            JOptionPane.showMessageDialog(this, "Equipe atualizada com sucesso!");
            carregarEquipes();
            limparCampos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar equipe: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void excluirEquipe() {
        int linhaSelecionada = tabelaEquipes.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma equipe para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esta equipe?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                int id = (int) tableModel.getValueAt(linhaSelecionada, 0);
                equipeDAO.excluir(id);
                JOptionPane.showMessageDialog(this, "Equipe excluída com sucesso!");
                carregarEquipes();
                limparCampos();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir equipe: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void carregarEquipes() {
        tableModel.setRowCount(0); // Limpa a tabela
        try {
            List<Equipe> equipes = equipeDAO.buscarTodos();
            for (Equipe equipe : equipes) {
                Object[] rowData = {
                    equipe.getIdEquipe(),
                    equipe.getNome(),
                    equipe.getDescricao(),
                    equipe.getGerenteResponsavel().getNomeCompleto()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar equipes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void preencherCamposComSelecao() {
        int linhaSelecionada = tabelaEquipes.getSelectedRow();
        if (linhaSelecionada != -1) {
            campoNome.setText(tableModel.getValueAt(linhaSelecionada, 1).toString());
            campoDescricao.setText(tableModel.getValueAt(linhaSelecionada, 2).toString());
            
            String nomeGerente = tableModel.getValueAt(linhaSelecionada, 3).toString();
            for (int i = 0; i < comboBoxGerente.getItemCount(); i++) {
                Usuario gerente = comboBoxGerente.getItemAt(i);
                if (gerente.getNomeCompleto().equals(nomeGerente)) {
                    comboBoxGerente.setSelectedItem(gerente);
                    break;
                }
            }
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        campoDescricao.setText("");
        comboBoxGerente.setSelectedIndex(-1);
    }
}