<template>
  <section class="secao-comparativo" id="comparativo">
    <div class="container">
      <div class="header-comparativo">
        <span class="badge">Comparativo Completo</span>
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
            <span class="category-indicator">{{ categorias.find(c => c.id === abaAtiva).nome }}</span>
          </div>
          <div class="plan-header">
            <span class="plan-name">Neri Pro</span>
            <span class="plan-price">R$ 99,90/mês</span>
          </div>
          <div class="plan-header featured">
            <span class="plan-name">Neri Elite</span>
            <span class="plan-price">R$ 199,90/mês</span>
          </div>
        </div>
        
        <div class="table-body">
          <transition-group name="list">
            <div 
              v-for="item in recursos[abaAtiva]" 
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
import { ref } from 'vue';

const abaAtiva = ref('vendas');

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
    { nome: 'Kit de peças e/ou serviços', plus: false, master: true },
  ],
  estoque: [
    { nome: 'Peças', plus: true, master: true },
    { nome: 'Insumos', plus: true, master: true },
    { nome: 'Importação de compras via XML', plus: true, master: true },
    { nome: 'Estoque Mínimo', plus: true, master: true },
    { nome: 'Auditoria', plus: false, master: true },
    { nome: 'Relatório de entrega de materiais', plus: false, master: true },
    { nome: 'Cotação', plus: false, master: true },
    { nome: 'Transferência de estoque', plus: false, master: true },
    { nome: 'Devolução de venda', plus: false, master: true },
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
    { nome: 'D.R.E (demonstrativo de resultado)', plus: false, master: true },
    { nome: 'Emissão de boletos', plus: false, master: true },
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
    { nome: 'Peça Aí', plus: false, master: true },
    { nome: 'PartsLink24', plus: true, master: true },
    { nome: 'Catálogo Fraga', plus: false, master: true },
    { nome: 'Stone', plus: false, master: true },
  ],
  relacionamento: [
    { nome: 'E-mail Marketing', plus: true, master: true },
    { nome: 'Gestão de clientes (aniversário)', plus: true, master: true },
    { nome: 'Cobrança', plus: true, master: true },
    { nome: 'Pesquisa de satisfação', plus: true, master: true },
    { nome: 'Serviço Futuros/Próximas Revisões', plus: true, master: true },
    { nome: 'Área do cliente', plus: false, master: true },
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
    { nome: 'Custo hora', plus: false, master: true },
    { nome: 'Agendamento', plus: false, master: true },
  ]
};
</script>

<style scoped>
.secao-comparativo {
  padding: 100px 0;
  background-color: #fff;
  font-family: 'Inter', system-ui, -apple-system, sans-serif;
}

.header-comparativo {
  text-align: center;
  margin-bottom: 60px;
}

.badge {
  background: #f3f4f6;
  color: #374151;
  padding: 6px 14px;
  border-radius: 99px;
  font-size: 0.85rem;
  font-weight: 600;
  margin-bottom: 1rem;
  display: inline-block;
}

.title {
  font-size: 3rem;
  font-weight: 800;
  color: #111827;
  letter-spacing: -0.02em;
  margin-bottom: 1.25rem;
}

.subtitle {
  color: #4b5563;
  font-size: 1.25rem;
  max-width: 650px;
  margin: 0 auto;
}

/* Tabs Estilo Stripe */
.tabs-container {
  display: flex;
  justify-content: center;
  margin-bottom: 40px;
}

.tabs-wrapper {
  display: flex;
  background: #f9fafb;
  padding: 4px;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
}

.tab-btn {
  padding: 10px 20px;
  border-radius: 8px;
  border: none;
  background: transparent;
  font-weight: 600;
  color: #6b7280;
  cursor: pointer;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  font-size: 0.95rem;
}

.tab-btn:hover {
  color: #111827;
}

.tab-btn.active {
  background: white;
  color: #6366f1;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05), 0 1px 3px rgba(0,0,0,0.1);
}

/* Card Comparativo Moderno */
.comparativo-card {
  background: white;
  border-radius: 24px;
  border: 1px solid #e5e7eb;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.table-header {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  padding: 40px 30px;
  border-bottom: 1px solid #f3f4f6;
  background: #fafafa;
}

.category-indicator {
  font-size: 1.5rem;
  font-weight: 800;
  color: #111827;
  letter-spacing: -0.01em;
}

.plan-header {
  text-align: center;
  display: flex;
  flex-direction: column;
  gap: 4px;
  position: relative;
}

.plan-header.featured {
  color: #6366f1;
}

.plan-name {
  font-size: 1.125rem;
  font-weight: 700;
}

.plan-price {
  font-size: 0.9rem;
  color: #6b7280;
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
  padding: 20px 30px;
  border-bottom: 1px solid #f9fafb;
  align-items: center;
}

.table-row:last-child {
  border-bottom: none;
}

.feature-name {
  color: #374151;
  font-weight: 500;
  font-size: 1rem;
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
  background: #eef2ff;
  color: #6366f1;
}

/* Footer */
.table-footer {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  padding: 30px;
  background: #fafafa;
  border-top: 1px solid #f3f4f6;
}

.footer-action {
  display: flex;
  justify-content: center;
}

.btn-link {
  color: #6b7280;
  font-weight: 600;
  font-size: 0.95rem;
  text-decoration: none;
  transition: color 0.2s;
}

.btn-link:hover {
  color: #111827;
}

.footer-action.featured .btn-link {
  color: #6366f1;
}

.footer-action.featured .btn-link:hover {
  color: #4f46e5;
}

.text-gradient {
  background: linear-gradient(135deg, #6366f1 0%, #a855f7 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
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
