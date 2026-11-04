# ADR-001: Adoção de React + React DOM + Vite no Frontend

- **Status**: Aceito
- **Data**: 04/11/2026
- **Decisores**: Equipe de Desenvolvimento (Joelson, Tayana, Lucas, César, Homero, Nicolas, Morgana)
- **Área**: Frontend

---

## 1. Contexto

A aplicação **Agenda do Salão-Escola** requer uma interface web responsiva, interativa e performática para agendamento de horários, visualização de grades diárias por profissional e cancelamento de horários sem recarregamento de página.

Historicamente, ferramentas legadas de build baseadas em Webpack (como Create React App) tornaram-se lentas na inicialização, geram bundles volumosos e foram descontinuadas pela comunidade oficial do React. Havia a necessidade de escolher uma stack moderna, com rápida inicialização de ambiente local, Hot Module Replacement (HMR) ágil e compilação otimizada para implantação na nuvem (Azure App Service).

---

## 2. Decisão

Adotar a combinação de **React** (versão 18+), **React DOM** e **Vite** como tooling de build e desenvolvimento para a Single Page Application (SPA).

- **React**: Biblioteca declarativa baseada em componentes funcionais e hooks, permitindo construção modular de interfaces de usuário.
- **React DOM**: Renderizador do React direcionado para o Virtual DOM em navegadores.
- **Vite**: Build tool baseada em Rollup e esbuild, utilizando ESM (ECMAScript Modules) nativo do navegador durante o desenvolvimento e gerando bundles estáticos minificados e otimizados para produção.

---

## 3. Justificativa

1. **Velocidade de Desenvolvimento**: O Vite oferece inicialização praticamente instantânea do servidor de desenvolvimento e HMR sub-segundo, aumentando significativamente a produtividade da equipe nas Sprints do desafio.
2. **Ecossistema e Manutenibilidade**: React possui a maior comunidade, documentação abrangente e fácil integração com bibliotecas de formulários, validações e estilização.
3. **Facilidade de Build e Deploy**: O Vite gera uma pasta estática `dist/` leve e autocontida, perfeitamente compatível com hospedagem estática ou acoplada em contêineres e App Services na Azure.
4. **Alinhamento com Padrões Modernos**: Suporte nativo a TypeScript / JavaScript moderno (ESNext), JSX e Tree-Shaking automático.

---

## 4. Consequências

### Positivas
- Ciclo de feedback instantâneo no desenvolvimento frontend.
- Empacotamento enxuto com split de código automático.
- Configuração simples via `vite.config.js` / `vite.config.ts`.
- Curva de aprendizado acessível para todos os integrantes do grupo.

### Negativas / Mitigações
- **SPA Client-side**: A renderização é 100% no cliente, demandando boas práticas no gerenciamento de estados de carregamento (loading states) e tratamento de erros de rede ao comunicar-se com a API Spring Boot.
- **Roteamento SPA em Servidores de Produção**: Requer regra de rewrite (ex: `web.config` na Azure ou fallback no proxy reverso) para redirecionar todas as rotas para `index.html`.

