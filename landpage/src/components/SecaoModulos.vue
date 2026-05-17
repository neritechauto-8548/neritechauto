<template>
  <section id="modulos" class="modules section-surface section-surface--alt section-spacer">
    <div class="container">
      <header class="section-header aos-init">
        <span class="section-label">Módulos</span>
        <h2 class="section-title">Tudo que sua oficina precisa em um só lugar</h2>
        <p class="section-subtitle">
          Módulos integrados para operação, financeiro, estoque e relacionamento com o cliente — sem sistemas paralelos.
        </p>
      </header>

      <div class="modules-grid">
        <article
          v-for="(mod, i) in modules"
          :key="mod.title"
          class="module-card premium-card aos-init"
          :class="`aos-delay-${(i % 4) + 1}`"
        >
          <div class="module-card__icon">{{ mod.icon }}</div>
          <h3>{{ mod.title }}</h3>
          <p>{{ mod.desc }}</p>
          <ul>
            <li v-for="item in mod.items" :key="item">{{ item }}</li>
          </ul>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
const modules = [
  {
    icon: '📋',
    title: 'Ordens de Serviço',
    desc: 'Do orçamento à entrega com controle total do fluxo.',
    items: ['OS digital com checklist', 'Kanban de produção', 'Histórico por placa'],
  },
  {
    icon: '💰',
    title: 'Financeiro',
    desc: 'Fluxo de caixa conectado à operação da oficina.',
    items: ['Contas a pagar e receber', 'Faturamento por OS', 'Relatórios gerenciais'],
  },
  {
    icon: '📦',
    title: 'Estoque',
    desc: 'Controle de peças sem surpresas na reposição.',
    items: ['Alertas de estoque mínimo', 'Baixa automática na OS', 'Cadastro por código'],
  },
  {
    icon: '👥',
    title: 'Clientes & CRM',
    desc: 'Relacionamento profissional que gera retorno.',
    items: ['Cadastro de veículos', 'Lembretes de revisão', 'Pesquisa de satisfação'],
  },
  {
    icon: '📄',
    title: 'Fiscal',
    desc: 'Emissão de notas integrada ao faturamento.',
    items: ['NF-e de peças', 'NFS-e de serviços', 'NF de devolução'],
  },
  {
    icon: '📱',
    title: 'Portal do Cliente',
    desc: 'Transparência que aumenta confiança e aprovação.',
    items: ['Acompanhamento do serviço', 'Aprovação online', 'Histórico de visitas'],
  },
];
</script>

<style scoped>
.modules-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.25rem;
}

.module-card {
  padding: 1.75rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.module-card__icon {
  font-size: 1.5rem;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--primary-light);
  border-radius: 12px;
}

.module-card h3 {
  font-size: 1.0625rem;
  font-weight: 800;
  font-family: var(--font-heading);
}

.module-card > p {
  font-size: 0.9rem;
  color: var(--text-muted);
  line-height: 1.55;
}

.module-card ul {
  margin-top: auto;
  padding-top: 0.75rem;
  border-top: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.module-card li {
  font-size: 0.8125rem;
  color: var(--text-main);
  font-weight: 500;
  padding-left: 14px;
  position: relative;
}

.module-card li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 8px;
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: var(--primary);
}

@media (max-width: 1024px) {
  .modules-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 560px) {
  .modules-grid { grid-template-columns: 1fr; }
}
</style>
