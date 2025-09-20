package br.com.promanage.view;

import br.com.promanage.model.Projeto;

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
//        JMenu menuTarefas = new JMenu("Tarefas");

        // Cria os itens de menu
        JMenuItem itemGerenciarProjetos = new JMenuItem("Gerenciar Projetos");
        JMenuItem itemGerenciarEquipes = new JMenuItem("Gerenciar Equipes");
//        JMenuItem itemGerenciarTarefas = new JMenuItem("Gerenciar Tarefas");

        // Adiciona os itens aos menus
        menuProjetos.add(itemGerenciarProjetos);
        menuEquipes.add(itemGerenciarEquipes);
//        menuTarefas.add(itemGerenciarTarefas);

        // Adiciona os menus à barra de menu
        menuBar.add(menuProjetos);
        menuBar.add(menuEquipes);
//        menuBar.add(menuTarefas);
        
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
//        itemGerenciarTarefas.addActionListener(e -> mostrarPainel("Tarefas"));

        setVisible(true);
    }
    
    // Método para alternar entre os painéis (existente)
    public void mostrarPainel(String nomePainel) {
        cardLayout.show(contentPanel, nomePainel);
    }

    // NOVO MÉTODO: Alterna para a tela de tarefas passando o projeto
    public void mostrarPainel(String nomePainel, Projeto projeto) {
        if ("Tarefas".equals(nomePainel)) {
            // Cria uma nova instância da tela de tarefas
            TarefaView tarefasView = new TarefaView(this, projeto);
            
            // Remove o painel antigo de tarefas (se existir) para evitar duplicação
            if (contentPanel.getComponentCount() > 0) {
                // Percorre os componentes para encontrar o painel de tarefas e removê-lo
                for (Component comp : contentPanel.getComponents()) {
                    if (comp instanceof TarefaView) {
                        contentPanel.remove(comp);
                        break;
                    }
                }
            }
            
            // Adiciona a nova tela de tarefas
            contentPanel.add(tarefasView, nomePainel);
            cardLayout.show(contentPanel, nomePainel);
        } else {
            // Usa o método original para os outros painéis
            mostrarPainel(nomePainel);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}