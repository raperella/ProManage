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
    private JTable tabelaProjetos;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;

    private MainFrame mainFrame;

    // Construtor que recebe uma referência à MainFrame
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
            ProjetoFormView form = new ProjetoFormView(mainFrame);
            form.setVisible(true);
            carregarProjetos();
        });
        botaoEditar = new JButton("Editar Selecionado");
        botaoExcluir = new JButton("Excluir Selecionado");

        painelBotoes.add(botaoAdicionar);
        painelBotoes.add(botaoEditar);
        painelBotoes.add(botaoExcluir);

        add(painelBotoes, BorderLayout.SOUTH);
        
        carregarProjetos();
    }

    private void carregarProjetos() {
        try {
            ProjetoDAO projetoDAO = new ProjetoDAO();
            List<Projeto> projetos = projetoDAO.buscarTodos();
            
            // Limpa a tabela antes de carregar novos dados
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