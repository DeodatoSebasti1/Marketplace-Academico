
```mermaid
gantt
    title Projeto Mercado Académico — Gráfico de Gantt
    dateFormat  YYYY-MM-DD
    axisFormat  %d/%m

    section Fase 1 — Ideia e Planeamento
    Análise do problema e contexto académico        :done,    f1a, 2025-09-01, 10d
    Definição da ideia do projeto                   :done,    f1b, 2025-09-05, 8d
    Definição do público-alvo                       :done,    f1c, 2025-09-08, 7d
    Levantamento de requisitos                     :done,    f1d, 2025-09-10, 12d
    Planeamento inicial do projeto                 :done,    f1e, 2025-09-15, 10d
    Estrutura base frontend Android                :done,    f1f, 2025-09-18, 10d
    Estrutura base backend (Spring Boot)           :done,    f1g, 2025-09-18, 10d
    Configuração do repositório GitHub              :done,    f1h, 2025-09-20, 5d
    1ª Entrega                                     :milestone, m1, 2025-09-28, 1d

    section Fase 2 — Modelação e Desenvolvimento Inicial
    Modelação da base de dados (ER)                 :done,    f2a, 2025-09-29, 12d
    Criação das entidades backend                  :done,    f2b, 2025-10-05, 15d
    Configuração da API REST                       :done,    f2c, 2025-10-08, 15d
    Integração inicial frontend-backend            :done,    f2d, 2025-10-15, 14d
    Implementação da funcionalidade criar produto  :done,    f2e, 2025-10-20, 15d
    Testes da funcionalidade de produtos           :done,    f2f, 2025-10-28, 10d
    Ajustes e correções iniciais                   :done,    f2g, 2025-11-01, 8d
    Documentação intermédia                        :done,    f2h, 2025-10-25, 15d
    2ª Entrega                                     :milestone, m2, 2025-11-09, 1d

    section Fase 3 — Desenvolvimento Funcional Completo
    Autenticação e gestão de utilizadores           :done,    f3a, 2025-11-10, 12d
    Gestão de produtos (editar/remover)             :done,    f3b, 2025-11-15, 15d
    Implementação de favoritos                     :done,    f3c, 2025-11-20, 12d
    Sistema de propostas                           :done,    f3d, 2025-11-22, 15d
    Histórico de compras e vendas                  :done,    f3e, 2025-11-28, 12d
    Sistema de uploads de imagens                  :done,    f3f, 2025-12-01, 10d
    Integração de pagamentos (Stripe)               :done,    f3g, 2025-12-02, 10d

    section Fase 4 — Testes, Documentação e Entrega
    Testes funcionais completos                    :done,    f4a, 2025-12-05, 7d
    Correção de erros                              :done,    f4b, 2025-12-07, 5d
    Atualização do diagrama de classes             :done,    f4c, 2025-12-08, 5d
    Atualização do modelo ER                       :done,    f4d, 2025-12-08, 5d
    Documentação REST                              :done,    f4e, 2025-12-09, 5d
    Manual do Utilizador                           :done,    f4f, 2025-12-09, 5d
    Preparação do relatório final                  :done,    f4g, 2025-12-10, 4d
    3ª Entrega Final                               :milestone, m3, 2025-12-14, 1d
