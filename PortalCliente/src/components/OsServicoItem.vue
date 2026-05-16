<template>
  <div class="servico-item">
    <div class="servico-icon">
      <i data-lucide="wrench"></i>
    </div>
    <div class="servico-content">
      <p class="servico-descricao">{{ servico.descricao }}</p>
      <div class="servico-meta">
        <span v-if="servico.tempo" class="servico-tempo">
          <i data-lucide="clock"></i>
          {{ servico.tempo }}h
        </span>
        <span class="servico-valor">{{ formatCurrency(servico.valor) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';

defineProps({
  servico: {
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
.servico-item {
  display: flex;
  gap: 1rem;
  padding: 1rem;
  background: var(--gray-50);
  border-radius: var(--radius);
  border-left: 3px solid var(--primary);
  transition: var(--transition);
}

.servico-item:hover {
  background: var(--white);
  box-shadow: var(--shadow-sm);
}

.servico-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-sm);
  background: var(--primary);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.servico-icon i {
  width: 20px;
  height: 20px;
  color: var(--white);
}

.servico-content {
  flex: 1;
}

.servico-descricao {
  font-weight: 500;
  color: var(--gray-900);
  margin-bottom: 0.5rem;
}

.servico-meta {
  display: flex;
  align-items: center;
  gap: 1rem;
  font-size: 0.875rem;
}

.servico-tempo {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  color: var(--gray-600);
}

.servico-tempo i {
  width: 14px;
  height: 14px;
}

.servico-valor {
  color: var(--primary);
  font-weight: 600;
  margin-left: auto;
}
</style>
