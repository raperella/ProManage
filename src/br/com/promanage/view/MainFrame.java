package br.com.promanage.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        setTitle("ProManage - Sistema de Gestão de Projetos");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Painel principal que irá trocar os conteúdos
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Cria a barra de menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menuProjetos = new JMenu("Projetos");
        JMenu menuEquipes = new JMenu("Equipes");
        JMenu menuTarefas = new JMenu("Tarefas");

        // Cria os itens de menu
        JMenuItem itemGerenciarProjetos = new JMenuItem("Gerenciar Projetos");
        JMenuItem itemGerenciarEquipes = new JMenuItem("Gerenciar Equipes");
        JMenuItem itemGerenciarTarefas = new JMenuItem("Gerenciar Tarefas");

        // Adiciona os itens aos menus
        menuProjetos.add(itemGerenciarProjetos);
        menuEquipes.add(itemGerenciarEquipes);
        menuTarefas.add(itemGerenciarTarefas);

        // Adiciona os menus à barra de menu
        menuBar.add(menuProjetos);
        menuBar.add(menuEquipes);
        menuBar.add(menuTarefas);
        
        setJMenuBar(menuBar);

        // Adiciona o painel de conteúdo à janela principal
        add(contentPanel, BorderLayout.CENTER);

        // Adiciona os painéis de navegação ao CardLayout
        contentPanel.add(new DashboardView(), "Dashboard");
        contentPanel.add(new ProjetoView(this), "Projetos");
        contentPanel.add(new EquipeView(this), "Equipes");

        // Exibe o painel de controle assim que a janela é criada
        cardLayout.show(contentPanel, "Dashboard");

        // Adiciona a lógica de navegação aos itens do menu
        itemGerenciarProjetos.addActionListener(e -> mostrarPainel("Projetos"));
        itemGerenciarEquipes.addActionListener(e -> mostrarPainel("Equipes"));
        itemGerenciarTarefas.addActionListener(e -> mostrarPainel("Tarefas"));

        setVisible(true);
    }
    
    // Método para alternar entre os painéis
    public void mostrarPainel(String nomePainel) {
        cardLayout.show(contentPanel, nomePainel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}