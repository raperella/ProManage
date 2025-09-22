# EduManage - Sistema de Gerenciamento de Tarefas

## Descrição do Projeto

O **EduManage** é uma aplicação desktop desenvolvida em Java para auxiliar no gerenciamento de projetos e tarefas. Ele permite que usuários criem e gerenciem projetos, atribuam tarefas a outros membros da equipe e acompanhem o progresso de cada atividade através de diferentes status. O sistema inclui uma visualização personalizada, onde cada usuário pode ver as tarefas que lhe foram designadas.

## Funcionalidades

- **Gerenciamento de Projetos**: Crie, visualize e gerencie todos os projetos da sua equipe.
- **Gerenciamento de Tarefas**: Adicione, edite e organize tarefas dentro de cada projeto.
- **Atribuição de Responsáveis**: Atribua tarefas a usuários específicos.
- **Controle de Status**: Altere o status de tarefas para "A Fazer", "Em Andamento", "Concluído" ou "Cancelado".
- **Acompanhamento de Prazos**: Defina datas de início e fim previstas e reais para monitorar o progresso.
- **Visão Pessoal de Tarefas**: Uma tela dedicada para cada usuário, mostrando apenas as tarefas sob sua responsabilidade.
- **Persistência de Dados**: Todas as informações de projetos, tarefas e usuários são salvas em um banco de dados.

## Tecnologias Utilizadas

- **Linguagem**: Java
- **Interface Gráfica**: Java Swing
- **Acesso a Dados**: JDBC (Java Database Connectivity)
- **Padrão de Projeto**: MVC (Model-View-Controller)

## Como Executar o Projeto 

Siga os passos abaixo para clonar e rodar a aplicação em seu ambiente de desenvolvimento.

### Pré-requisitos
- JDK 11 ou superior
- Uma IDE Java (como IntelliJ IDEA, Eclipse ou NetBeans)
- Um banco de dados relacional (ex: MySQL, PostgreSQL)

## Estrutura do Projeto

A aplicação segue uma estrutura baseada no padrão MVC:

- `src/main/java/br/com.ProManage/`
  - `dao/`: Contém as classes de acesso a dados (`TarefaDAO`, `UsuarioDAO`, `Conexao`, etc.).
  - `model/`: Contém as classes de modelo (`Tarefa`, `Usuario`, `Projeto`).
  - `view/`: Contém as classes da interface gráfica (`TarefaView`, `MinhasTarefasView`, `MainFrame`, etc.).
  - `interfaces/`: Contém as interfaces de serviço, como a `Refreshable` (caso você opte por usar esta solução).

## Autor

- Equipe ProManage
