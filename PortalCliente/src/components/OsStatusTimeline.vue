<template>
  <div class="timeline">
    <div
      v-for="(step, index) in steps"
      :key="index"
      :class="['timeline-step', { active: isStepActive(index), completed: isStepCompleted(index) }]"
    >
      <div class="timeline-icon">
        <i :data-lucide="step.icon"></i>
      </div>
      <div class="timeline-content">
        <p class="timeline-title">{{ step.title }}</p>
        <p class="timeline-date" v-if="step.date">{{ formatDate(step.date) }}</p>
      </div>
      <div class="timeline-line" v-if="index < steps.length - 1"></div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue';

const props = defineProps({
  currentStatus: {
    type: String,
    required: true,
  },
  historico: {
    type: Array,
    default: () => [],
  },
});

const steps = computed(() => {
  const allSteps = [
    { 
      key: 'AGUARDANDO_APROVACAO', 
      title: 'Aguardando Aprovação', 
      icon: 'clock',
      date: getDateForStatus('AGUARDANDO_APROVACAO')
    },
    { 
      key: 'EM_EXECUCAO', 
      title: 'Em Execução', 
      icon: 'wrench',
      date: getDateForStatus('EM_EXECUCAO')
    },
    { 
      key: 'AGUARDANDO_PECAS', 
      title: 'Aguardando Peças', 
      icon: 'package',
      date: getDateForStatus('AGUARDANDO_PECAS')
    },
    { 
      key: 'FINALIZADO', 
      title: 'Finalizado', 
      icon: 'check-circle',
      date: getDateForStatus('FINALIZADO')
    },
    { 
      key: 'ENTREGUE', 
      title: 'Entregue', 
      icon: 'truck',
      date: getDateForStatus('ENTREGUE')
    },
  ];
  
  return allSteps;
});

const statusOrder = ['AGUARDANDO_APROVACAO', 'EM_EXECUCAO', 'AGUARDANDO_PECAS', 'FINALIZADO', 'ENTREGUE'];

const currentStatusIndex = computed(() => {
  return statusOrder.indexOf(props.currentStatus);
});

const isStepActive = (index) => {
  return index === currentStatusIndex.value;
};

const isStepCompleted = (index) => {
  return index < currentStatusIndex.value;
};

const getDateForStatus = (status) => {
  const item = props.historico.find(h => h.status === status);
  return item ? item.data : null;
};

const formatDate = (date) => {
  if (!date) return '';
  return new Date(date).toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};

onMounted(() => {
  if (window.lucide) {
    window.lucide.createIcons();
  }
});
</script>

<style scoped>
.timeline {
  display: flex;
  flex-direction: column;
  gap: 0;
  position: relative;
}

.timeline-step {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  position: relative;
  padding-bottom: 2rem;
}

.timeline-step:last-child {
  padding-bottom: 0;
}

.timeline-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--gray-200);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: var(--transition);
  position: relative;
  z-index: 2;
}

.timeline-icon i {
  width: 20px;
  height: 20px;
  color: var(--gray-400);
  transition: var(--transition);
}

.timeline-step.completed .timeline-icon {
  background: var(--status-completed);
}

.timeline-step.completed .timeline-icon i {
  color: var(--white);
}

.timeline-step.active .timeline-icon {
  background: var(--primary);
  box-shadow: 0 0 0 4px rgba(0, 92, 255, 0.2);
}

.timeline-step.active .timeline-icon i {
  color: var(--white);
}

.timeline-content {
  flex: 1;
  padding-top: 0.5rem;
}

.timeline-title {
  font-weight: 600;
  color: var(--gray-900);
  margin-bottom: 0.25rem;
}

.timeline-step:not(.active):not(.completed) .timeline-title {
  color: var(--gray-400);
}

.timeline-date {
  font-size: 0.875rem;
  color: var(--gray-500);
  margin: 0;
}

.timeline-line {
  position: absolute;
  left: 20px;
  top: 40px;
  width: 2px;
  height: calc(100% - 40px);
  background: var(--gray-200);
  z-index: 1;
}

.timeline-step.completed .timeline-line {
  background: var(--status-completed);
}

.timeline-step.active .timeline-line {
  background: linear-gradient(to bottom, var(--status-completed) 0%, var(--gray-200) 100%);
}

/* Responsivo */
@media (max-width: 768px) {
  .timeline-icon {
    width: 36px;
    height: 36px;
  }
  
  .timeline-icon i {
    width: 18px;
    height: 18px;
  }
  
  .timeline-line {
    left: 18px;
  }
}
</style>

<style scoped>
</style>
