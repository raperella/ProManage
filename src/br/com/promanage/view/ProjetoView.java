package br.com.promanage.view;

import br.com.promanage.dao.ProjetoDAO;
import br.com.promanage.model.Projeto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ProjetoView extends JPanel {

    private JButton botaoAdicionar;
    private JButton botaoEditar;
    private JButton botaoExcluir;
    private JButton botaoVoltar;
    private JTable tabelaProjetos;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;

    private MainFrame mainFrame;

    public ProjetoView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // Painel principal com BorderLayout
        setLayout(new BorderLayout());

        // Configuração da tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nome", "Descrição", "Data Início", "Data Término", "Status", "Gerente", "Equipe"}, 0);
        tabelaProjetos = new JTable(tableModel);
        scrollPane = new JScrollPane(tabelaProjetos);
        add(scrollPane, BorderLayout.CENTER);

        // Painel para os botões na parte inferior
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        botaoAdicionar = new JButton("Adicionar Novo");
        botaoAdicionar.addActionListener(e -> {
            ProjetoFormView form = new ProjetoFormView(mainFrame, this);
            form.setVisible(true);
        });

        botaoEditar = new JButton("Editar Selecionado");
        botaoEditar.addActionListener(e -> {
            int linhaSelecionada = tabelaProjetos.getSelectedRow();
            if (linhaSelecionada >= 0) {
                int idProjeto = (int) tabelaProjetos.getValueAt(linhaSelecionada, 0);
                try {
                    Projeto projeto = new ProjetoDAO().buscarPorId(idProjeto);
                    if (projeto != null) {
                        ProjetoFormView form = new ProjetoFormView(mainFrame, this, projeto);
                        form.setVisible(true);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao buscar projeto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um projeto para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        botaoExcluir = new JButton("Excluir Selecionado");
        botaoExcluir.addActionListener(e -> {
            int linhaSelecionada = tabelaProjetos.getSelectedRow();
            if (linhaSelecionada >= 0) {
                int opcao = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este projeto?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION) {
                    int idProjeto = (int) tabelaProjetos.getValueAt(linhaSelecionada, 0);
                    try {
                        new ProjetoDAO().deletar(idProjeto);
                        JOptionPane.showMessageDialog(this, "Projeto excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        carregarProjetos();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir projeto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um projeto para excluir.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        botaoVoltar = new JButton("Voltar");
        botaoVoltar.addActionListener(e -> {
            mainFrame.mostrarPainel("Dashboard");
        });

        painelBotoes.add(botaoAdicionar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoVoltar); // Adiciona o novo botão

        add(painelBotoes, BorderLayout.SOUTH);
        
        carregarProjetos();
    }

    public void carregarProjetos() {
        try {
            ProjetoDAO projetoDAO = new ProjetoDAO();
            List<Projeto> projetos = projetoDAO.buscarTodos();
            
            tableModel.setRowCount(0);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (Projeto projeto : projetos) {
                tableModel.addRow(new Object[]{
                    projeto.getIdProjeto(),
                    projeto.getNome(),
                    projeto.getDescricao(),
                    dateFormat.format(projeto.getDataInicio()),
                    dateFormat.format(projeto.getDataTerminoPrevista()),
                    projeto.getStatus(),
                    projeto.getGerenteResponsavel().getNomeCompleto(),
                    projeto.getEquipeResponsavel().getNome()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar projetos: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}