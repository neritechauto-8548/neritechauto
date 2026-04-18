<template>
  <div class="peca-item">
    <div class="peca-icon">
      <i data-lucide="package"></i>
    </div>
    <div class="peca-content">
      <p class="peca-nome">{{ peca.nome }}</p>
      <div class="peca-meta">
        <span class="peca-quantidade">
          Qtd: {{ peca.quantidade }}
        </span>
        <span class="peca-valor-unitario">
          {{ formatCurrency(peca.valorUnitario) }} un.
        </span>
        <span class="peca-valor-total">
          {{ formatCurrency(peca.valorTotal || peca.valorUnitario * peca.quantidade) }}
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';

defineProps({
  peca: {
    type: Object,
    required: true,
  },
});

const formatCurrency = (value) => {
  if (!value) return 'R$ 0,00';
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value);
};

onMounted(() => {
  if (window.lucide) {
    window.lucide.createIcons();
  }
});
</script>

<style scoped>
.peca-item {
  display: flex;
  gap: 1rem;
  padding: 1rem;
  background: var(--gray-50);
  border-radius: var(--radius);
  border-left: 3px solid var(--status-waiting);
  transition: var(--transition);
}

.peca-item:hover {
  background: var(--white);
  box-shadow: var(--shadow-sm);
}

.peca-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-sm);
  background: var(--status-waiting);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.peca-icon i {
  width: 20px;
  height: 20px;
  color: var(--white);
}

.peca-content {
  flex: 1;
}

.peca-nome {
  font-weight: 500;
  color: var(--gray-900);
  margin-bottom: 0.5rem;
}

.peca-meta {
  display: flex;
  align-items: center;
  gap: 1rem;
  font-size: 0.875rem;
  flex-wrap: wrap;
}

.peca-quantidade {
  color: var(--gray-600);
  font-weight: 500;
}

.peca-valor-unitario {
  color: var(--gray-500);
}

.peca-valor-total {
  color: var(--status-waiting);
  font-weight: 600;
  margin-left: auto;
}

@media (max-width: 768px) {
  .peca-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.25rem;
  }
  
  .peca-valor-total {
    margin-left: 0;
  }
}
</style>
