package br.com.promanage.view;

import br.com.promanage.dao.ProjetoDAO;
import br.com.promanage.model.Projeto;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

public class DashboardView extends JPanel {

    private ProjetoDAO projetoDAO;
    private JButton botaoAtualizar;

    public DashboardView() {
        this.projetoDAO = new ProjetoDAO();
        setLayout(new GridLayout(2, 2, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        botaoAtualizar = new JButton("Atualizar Dados");
        
        botaoAtualizar.addActionListener(e -> renderizarDashboard());

        renderizarDashboard();
    }

    private void renderizarDashboard() {
        removeAll();
        
        try {
            // 1. Gráfico de Projetos por Equipe (Gráfico de Pizza)
            Map<String, Long> projetosPorEquipe = projetoDAO.contarProjetosPorEquipe();
            DefaultPieDataset datasetProjetos = new DefaultPieDataset();
            for (Map.Entry<String, Long> entry : projetosPorEquipe.entrySet()) {
                datasetProjetos.setValue(entry.getKey(), entry.getValue());
            }
            JFreeChart pieChart = ChartFactory.createPieChart("Projetos por Equipe", datasetProjetos, true, true, false);
            ChartPanel pieChartPanel = new ChartPanel(pieChart);
            pieChartPanel.setBorder(BorderFactory.createTitledBorder("Visão Geral de Projetos"));
            add(pieChartPanel);

            // 2. Gráfico de Projetos Atrasados (Gráfico de Barras)
            Map<String, Long> projetosAtrasados = projetoDAO.contarProjetosAtrasadosPorEquipe();
            DefaultCategoryDataset datasetAtrasados = new DefaultCategoryDataset();
            for (Map.Entry<String, Long> entry : projetosAtrasados.entrySet()) {
                datasetAtrasados.addValue(entry.getValue(), "Projetos Atrasados", entry.getKey());
            }
            JFreeChart barChart = ChartFactory.createBarChart("Projetos Atrasados por Equipe", "Equipe", "Quantidade", datasetAtrasados);
            ChartPanel barChartPanel = new ChartPanel(barChart);
            barChartPanel.setBorder(BorderFactory.createTitledBorder("Projetos Atrasados"));
            add(barChartPanel);

            // 3. Lista de Projetos a Vencer (Tabela)
            List<Projeto> projetosVencendo = projetoDAO.buscarProjetosVencendo();
            String[] colunas = {"ID", "Nome", "Equipe", "Data de Término"};
            DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (Projeto projeto : projetosVencendo) {
                Object[] linha = {
                    projeto.getIdProjeto(),
                    projeto.getNome(),
                    projeto.getEquipeResponsavel().getNome(),
                    dateFormat.format(projeto.getDataTerminoPrevista())
                };
                tableModel.addRow(linha);
            }

            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createTitledBorder("Projetos Vencendo em 7 Dias"));
            add(scrollPane);
            
            // Adiciona um painel com um FlowLayout para centralizar e redimensionar o botão
            JPanel painelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
            painelBotao.add(botaoAtualizar);
            add(painelBotao);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do dashboard: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            add(new JLabel("Erro ao carregar dados. Verifique a conexão com o banco."));
        }

        revalidate();
        repaint();
    }
}