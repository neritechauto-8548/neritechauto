<template>
  <section class="secao-comparativo" id="comparativo">
    <div class="container">
      <div class="header-comparativo aos-init">
        <span class="section-label">Comparativo</span>
        <h2 class="title">Escolha o plano ideal para sua <span class="text-gradient">oficina</span></h2>
        <p class="subtitle">Transparência total: compare cada funcionalidade antes de decidir.</p>
      </div>

      <!-- Abas de Categorias - Estilo Stripe -->
      <div class="tabs-container">
        <div class="tabs-wrapper">
          <button 
            v-for="cat in categorias" 
            :key="cat.id"
            @click="abaAtiva = cat.id"
            :class="['tab-btn', { active: abaAtiva === cat.id }]"
          >
            {{ cat.nome }}
          </button>
        </div>
      </div>

      <!-- Tabela Comparativa - Estilo Stripe (Moderno/Glassmorphism) -->
      <div class="comparativo-card">
        <div class="table-header">
          <div class="feature-label">
            <span class="category-indicator">{{ nomeAbaAtiva }}</span>
          </div>
          <div class="plan-header">
            <span class="plan-name">NeriTech Pro</span>
            <span class="plan-price">R$ 79,90/mês</span>
          </div>
          <div class="plan-header featured">
            <span class="plan-name">NeriTech Ultra</span>
            <span class="plan-price">R$ 230,00/mês</span>
          </div>
        </div>
        
        <div class="table-body">
          <transition-group name="list">
            <div 
              v-for="item in recursosAtivos" 
              :key="item.nome"
              class="table-row"
            >
              <div class="feature-name">
                {{ item.nome }}
                <div v-if="item.desc" class="feature-desc">{{ item.desc }}</div>
              </div>
              <div class="check-cell">
                <div v-if="item.plus" class="status-icon success">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><path d="M20 6L9 17l-5-5" /></svg>
                </div>
                <div v-else class="status-icon empty">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 12H6" /></svg>
                </div>
              </div>
              <div class="check-cell featured">
                <div v-if="item.master" class="status-icon success">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3"><path d="M20 6L9 17l-5-5" /></svg>
                </div>
                <div v-else class="status-icon empty">
                  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M18 12H6" /></svg>
                </div>
              </div>
            </div>
          </transition-group>
        </div>
        
        <div class="table-footer">
          <div class="footer-spacer"></div>
          <div class="footer-action">
            <router-link to="/teste-gratis" class="btn-link">Começar Pro →</router-link>
          </div>
          <div class="footer-action featured">
            <router-link to="/teste-gratis" class="btn-link">Começar Elite →</router-link>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed } from 'vue';

const categorias = [
  { id: 'cadastro', nome: 'Cadastro' },
  { id: 'estoque', nome: 'Estoque' },
  { id: 'financeiro', nome: 'Financeiro' },
  { id: 'fiscal', nome: 'Fiscal' },
  { id: 'integracoes', nome: 'Integrações' },
  { id: 'relacionamento', nome: 'Relacionamento' },
  { id: 'vendas', nome: 'Vendas e Pátio' },
];

const recursos = {
  cadastro: [
    { nome: 'Clientes (PF e PJ), funcionários, fornecedores e seguradoras', plus: true, master: true },
    { nome: 'Peças e Serviços', plus: true, master: true },
    { nome: 'Insumos', plus: true, master: true },
    { nome: 'Integração de busca do veículo pela placa', plus: true, master: true },
    { nome: 'Integração de busca de cliente pelo CPF/CNPJ', plus: true, master: true },
    { nome: 'Kit de peças e/ou serviços', plus: true, master: true },
  ],
  estoque: [
    { nome: 'Peças', plus: true, master: true },
    { nome: 'Insumos', plus: true, master: true },
    { nome: 'Importação de compras via XML', plus: true, master: true },
    { nome: 'Estoque Mínimo', plus: true, master: true },
    { nome: 'Auditoria', plus: true, master: true },
    { nome: 'Relatório de entrega de materiais', plus: true, master: true },
    { nome: 'Cotação', plus: true, master: true },
    { nome: 'Transferência de estoque', plus: true, master: true },
    { nome: 'Devolução de venda', plus: true, master: true },
  ],
  financeiro: [
    { nome: 'Movimento de caixa', plus: true, master: true },
    { nome: 'Contas a pagar e receber', plus: true, master: true },
    { nome: 'Calendário de contas a pagar e receber', plus: true, master: true },
    { nome: 'Dashboard Financeiro', plus: true, master: true },
    { nome: 'Dashboard Gerencial', plus: true, master: true },
    { nome: 'Balancete', plus: true, master: true },
    { nome: 'Fluxo de caixa', plus: true, master: true },
    { nome: 'Custo da oficina', plus: true, master: true },
    { nome: 'D.R.E (demonstrativo de resultado)', plus: true, master: true },
    { nome: 'Emissão de boletos', plus: true, master: true },
    { nome: 'Lançamento bancário', plus: true, master: true },
  ],
  fiscal: [
    { nome: 'NF de peças', plus: false, master: true },
    { nome: 'NF de serviços', plus: false, master: true },
    { nome: 'NF de consumidor', plus: false, master: true },
    { nome: 'NF Conjugada', plus: false, master: true },
    { nome: 'NF de Devolução/Garantia', plus: false, master: true },
    { nome: 'SAT/MF-e', plus: false, master: true },
    { nome: 'Sintegra', plus: false, master: true },
    { nome: 'Inutilização de faixa de numeração', plus: false, master: true },
    { nome: 'Busca de compras na SEFAZ', plus: false, master: true },
    { nome: 'Manifestação', plus: false, master: true },
  ],
  integracoes: [
    { nome: 'Sistemas de Orçamento (Cília, Soma, Audatex e I360)', plus: true, master: true },
    { nome: 'Peça Aí', plus: true, master: true },
    { nome: 'PartsLink24', plus: true, master: true },
    { nome: 'Catálogo Fraga', plus: true, master: true },
    { nome: 'Stone', plus: true, master: true },
  ],
  relacionamento: [
    { nome: 'E-mail Marketing', plus: true, master: true },
    { nome: 'Gestão de clientes (aniversário)', plus: true, master: true },
    { nome: 'Cobrança', plus: true, master: true },
    { nome: 'Pesquisa de satisfação', plus: true, master: true },
    { nome: 'Serviço Futuros/Próximas Revisões', plus: true, master: true },
    { nome: 'Área do cliente', plus: true, master: true },
  ],
  vendas: [
    { nome: 'Orçamentos - Envio via whatsapp e e-mail', plus: true, master: true },
    { nome: 'Geração e controle das ordens de serviço', plus: true, master: true },
    { nome: 'Histórico de veículos', plus: true, master: true },
    { nome: 'Checklist personalizável', plus: true, master: true },
    { nome: 'Chat interno', plus: true, master: true },
    { nome: 'Calendário de OS\'s', plus: true, master: true },
    { nome: 'Vendas diretas (balcão)', plus: true, master: true },
    { nome: 'Comissão', plus: true, master: true },
    { nome: 'Relatórios de orçamentos', plus: true, master: true },
    { nome: 'Dashboard de orçamentos', plus: true, master: true },
    { nome: 'Custo hora', plus: true, master: true },
    { nome: 'Agendamento', plus: true, master: true },
  ]
};

const abaAtiva = ref('vendas');

const nomeAbaAtiva = computed(() => {
  const cat = categorias.find(c => c.id === abaAtiva.value);
  return cat ? cat.nome : '';
});

const recursosAtivos = computed(() => recursos[abaAtiva.value] || []);
</script>

<style scoped>
.secao-comparativo {
  padding: 4rem 0;
  background: transparent;
}

.header-comparativo {
  text-align: center;
  margin-bottom: 3rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.title {
  font-size: clamp(1.875rem, 4vw, 2.75rem);
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.035em;
  margin-bottom: 1rem;
  font-family: var(--font-heading);
}

.subtitle {
  color: var(--text-muted);
  font-size: 1.0625rem;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.65;
}

/* Tabs Estilo Stripe */
.tabs-container {
  display: flex;
  justify-content: center;
  margin-bottom: 40px;
}

.tabs-wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 6px;
  background: var(--surface-blue-50);
  padding: 6px;
  border-radius: var(--radius-lg);
  border: 1px solid var(--border);
}

.tab-btn {
  padding: 10px 16px;
  border-radius: var(--radius-md);
  border: none;
  background: transparent;
  font-weight: 600;
  color: var(--text-muted);
  cursor: pointer;
  transition: all var(--transition-base);
  font-size: 0.875rem;
  font-family: var(--font-body);
}

.tab-btn:hover {
  color: var(--midnight-navy);
}

.tab-btn.active {
  background: white;
  color: var(--primary);
  box-shadow: var(--shadow-sm);
}

/* Card Comparativo Moderno */
.comparativo-card {
  background: white;
  border-radius: var(--radius-xl);
  border: 1px solid var(--border);
  box-shadow: var(--shadow-lg);
  overflow: hidden;
}

.table-header {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  padding: 2rem 1.5rem;
  border-bottom: 1px solid var(--border);
  background: var(--surface-blue-50);
}

.category-indicator {
  font-size: 1.25rem;
  font-weight: 800;
  color: var(--midnight-navy);
  letter-spacing: -0.02em;
  font-family: var(--font-heading);
}

.plan-header {
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 4px;
  position: relative;
}

.plan-header.featured {
  color: var(--primary);
}

.plan-name {
  font-size: 1.125rem;
  font-weight: 700;
  color: var(--midnight-navy);
}

.plan-header.featured .plan-name {
  color: var(--primary);
}

.plan-price {
  font-size: 0.875rem;
  color: var(--text-muted);
  font-weight: 500;
}

.best-value {
  position: absolute;
  top: -30px;
  left: 50%;
  transform: translateX(-50%);
  background: #6366f1;
  color: white;
  font-size: 0.7rem;
  font-weight: 700;
  padding: 4px 12px;
  border-radius: 99px;
  text-transform: uppercase;
  white-space: nowrap;
}

/* Table Body */
.table-body {
  padding: 10px 0;
}

.table-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  padding: 1rem 1.5rem;
  border-bottom: 1px solid var(--border);
  align-items: center;
}

.table-row:last-child {
  border-bottom: none;
}

.feature-name {
  color: var(--text-main);
  font-weight: 500;
  font-size: 0.9375rem;
}

.check-cell {
  display: flex;
  justify-content: center;
}

.status-icon {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
}

.status-icon.success {
  background: #ecfdf5;
  color: #10b981;
}

.status-icon.empty {
  color: #d1d5db;
}

.check-cell.featured .status-icon.success {
  background: var(--primary-light);
  color: var(--primary);
}

/* Footer */
.table-footer {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  padding: 1.5rem;
  background: var(--surface-blue-50);
  border-top: 1px solid var(--border);
}

.footer-action {
  display: flex;
  justify-content: center;
}

.btn-link {
  color: var(--text-muted);
  font-weight: 600;
  font-size: 0.9375rem;
  text-decoration: none;
  transition: color var(--transition-fast);
}

.btn-link:hover {
  color: var(--midnight-navy);
}

.footer-action.featured .btn-link {
  color: var(--primary);
}

.footer-action.featured .btn-link:hover {
  color: var(--primary-dark);
}

/* Animations */
.list-enter-active, .list-leave-active {
  transition: all 0.3s ease;
}
.list-enter-from, .list-leave-to {
  opacity: 0;
  transform: translateY(10px);
}

@media (max-width: 768px) {
  .table-header, .table-row, .table-footer {
    grid-template-columns: 1fr 80px 80px;
    padding: 15px;
  }
  .plan-name { font-size: 0.9rem; }
  .plan-price { display: none; }
  .category-indicator { font-size: 1.1rem; }
  .title { font-size: 2rem; }
}
</style>
