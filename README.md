# Projeto de Loto Facil

Este repositório contém o código para um sistema de gerenciamento de tickets da Loto Facil. Abaixo, você encontrará a situação atual do código e as áreas onde podemos retomar o desenvolvimento.

## Situação do Código

### 1. Módulo Principal
- **mainJFX**: ✅ Totalmente funcional.

### 2. Banco de Dados
- **DataBase**: 🔄 Em progresso
  - Parte mais crua está funcional, mas pode ser melhorada. Consideramos separar mais as classes para uma melhor organização.
  
- **FileManager**: ✅ Funcional
  - Chama o método de criar o arquivo `purchases.txt` e seus gerenciamentos.

- **TicketPricing**: ✅ Funcional
  - Chama apenas os valores dos tickets. Sugestão de mudança de localização.

### 4. Telas
- **LoginScreen**: ✅ Funcional
  - Necessita apenas de uma revisão de estilos.

- **MainScreen**: 🔄 Em progresso
  - Tela funcional, mas métodos ainda precisam ser criados e objetos definidos.

- **PaymentSelectionScreen**: 🔄 Em progresso
  - Tela não finalizada.

- **PurchaseHistoryScreen**: 🔄 Em progresso
  - Tela funcional, mas pode sofrer modificações no futuro.

- **RegisterScreen**: ✅ Funcional
  - Tela está ok, mas haverá modificações em estilo e métodos.

- **TermosScreen**: 🔄 Em progresso
  - Falta a inclusão dos termos.

- **TicketPurchaseScreen**: ✅ Quase totalmente funcional
  - Necessita de revisão na estilização.

- **TicketSummaryScreen**: 🔄 Em progresso
  - Necessita finalizar seus métodos principais e estilizacao.

## Contribuições

Sinta-se à vontade para contribuir com melhorias, correções de bugs ou novas funcionalidades. Para contribuir, siga estas etapas:

1. **Fork este repositório**:
   - Clique no botão "Fork" no canto superior direito do repositório para criar uma cópia em sua conta do GitHub.

2. **Clone o repositório**:
   - No terminal, execute:
     ```bash
     git clone https://github.com/seu-usuario/LotoFacil.git
     ```
   - Substitua `seu-usuario` pelo seu nome de usuário do GitHub.

3. **Crie uma nova branch**:
   - Execute:
     ```bash
     git checkout -b feature/nome-da-sua-feature
     ```

4. **Faça suas alterações e comite**:
   - Após fazer as alterações, execute:
     ```bash
     git commit -m 'Adiciona uma nova feature'
     ```

5. **Envie para o repositório remoto**:
   - Execute:
     ```bash
     git push origin feature/nome-da-sua-feature
     ```

6. **Abra um pull request**:
   - Vá para a página do repositório original e clique em "Compare & pull request" para abrir um pull request.


