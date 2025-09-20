package br.com.promanage.view;

import br.com.promanage.dao.TarefaDAO;
import br.com.promanage.model.Projeto;
import br.com.promanage.model.Tarefa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TarefaView extends JPanel {

    private final MainFrame mainFrame;
    private Projeto projeto;

    private JButton botaoIncluir;
    private JButton botaoEditar;
    private JButton botaoExcluir;
    private JButton botaoVoltar;
    private JTable tabelaTarefas;
    private DefaultTableModel tableModel;

    public TarefaView(MainFrame mainFrame, Projeto projeto) {
        this.mainFrame = mainFrame;
        this.projeto = projeto;

        setLayout(new BorderLayout());
        
        JLabel titulo = new JLabel("Tarefas do Projeto: " + projeto.getNome(), SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(titulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Título", "Descrição", "Responsável", "Status", "Início Previsto", "Fim Previsto", "Início Real", "Fim Real"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabelaTarefas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaTarefas);
        add(scrollPane, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        botaoIncluir = new JButton("Incluir Tarefa");
        botaoIncluir.addActionListener(e -> {
            TarefaFormView form = new TarefaFormView(mainFrame, this, projeto);
            form.setVisible(true);
        });

        // Lógica CORRIGIDA para o botão "Editar"
        botaoEditar = new JButton("Editar Tarefa");
        botaoEditar.addActionListener(e -> {
            int linhaSelecionada = tabelaTarefas.getSelectedRow();
            if (linhaSelecionada >= 0) {
                int idTarefa = (int) tabelaTarefas.getValueAt(linhaSelecionada, 0);
                try {
                    Tarefa tarefaParaEditar = new TarefaDAO().buscarPorId(idTarefa);
                    if (tarefaParaEditar != null) {
                         TarefaFormView form = new TarefaFormView(mainFrame, this, tarefaParaEditar);
                         form.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Tarefa não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao buscar a tarefa: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma tarefa para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        botaoExcluir = new JButton("Excluir Tarefa");
        botaoExcluir.addActionListener(e -> {
            int linhaSelecionada = tabelaTarefas.getSelectedRow();
            if (linhaSelecionada >= 0) {
                int idTarefa = (int) tabelaTarefas.getValueAt(linhaSelecionada, 0);
                int opcao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir esta tarefa?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION) {
                    try {
                        new TarefaDAO().deletar(idTarefa);
                        JOptionPane.showMessageDialog(this, "Tarefa excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        carregarTarefas(); // Recarrega a tabela
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir tarefa: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma tarefa para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> {
            mainFrame.mostrarPainel("Projetos");
        });

        painelBotoes.add(botaoIncluir);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoVoltar);
        
        add(painelBotoes, BorderLayout.SOUTH);
        
        carregarTarefas();
    }
    
    public void carregarTarefas() {
        try {
            TarefaDAO tarefaDAO = new TarefaDAO();
            List<Tarefa> tarefas = tarefaDAO.buscarPorProjetoId(projeto.getIdProjeto());
            
            tableModel.setRowCount(0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (Tarefa tarefa : tarefas) {
                tableModel.addRow(new Object[]{
                    tarefa.getIdTarefa(),
                    tarefa.getTitulo(),
                    tarefa.getDescricao(),
                    tarefa.getResponsavel().getNomeCompleto(),
                    tarefa.getStatus(),
                    dateFormat.format(tarefa.getDataInicioPrevista()),
                    dateFormat.format(tarefa.getDataFimPrevista()),
                    tarefa.getDataInicioReal() != null ? dateFormat.format(tarefa.getDataInicioReal()) : "N/A",
                    tarefa.getDataFimReal() != null ? dateFormat.format(tarefa.getDataFimReal()) : "N/A"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar tarefas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}