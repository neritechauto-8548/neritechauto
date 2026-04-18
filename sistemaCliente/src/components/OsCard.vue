<template>
  <!-- List Item Mode (Stripe Dashboard Table Row) -->
  <div v-if="isListItem" 
       @click="handleClick"
       class="group flex flex-col md:grid md:grid-cols-12 items-center gap-4 px-6 py-4 cursor-pointer transition-all duration-150 border-b border-border-default last:border-0 hover:bg-gray-50/80">
    <div class="col-span-2 w-full md:w-auto">
      <span class="text-[14px] font-bold text-midnight-navy group-hover:text-stripe-indigo transition-colors tracking-tight-stripe">
        #{{ ordem.numeroOS || ordem.numeroOs }}
      </span>
    </div>
    <div class="col-span-4 w-full md:w-auto">
      <div class="flex flex-col">
        <span class="text-[14px] font-bold text-midnight-navy truncate">{{ ordem.nomeVeiculo || ordem.veiculo }}</span>
        <span class="text-[12px] font-semibold text-text-muted truncate">{{ ordem.placaVeiculo || ordem.placa || 'Sem placa' }}</span>
      </div>
    </div>
    <div class="col-span-3 w-full md:w-auto">
      <span :style="ordem.statusCor ? { backgroundColor: ordem.statusCor + '20', color: ordem.statusCor } : {}"
            :class="['badge-stripe inline-flex items-center px-2 py-0.5 rounded-md text-[11px] font-bold uppercase tracking-wider', !ordem.statusCor ? getStatusBadgeClasses(ordem.status) : '']">
        {{ ordem.statusNome || getStatusLabel(ordem.status) }}
      </span>
    </div>
    <div class="col-span-2 w-full md:w-auto">
      <span class="text-[13px] font-semibold text-text-muted">{{ formatDate(ordem.dataAbertura || ordem.dataEntrada) }}</span>
    </div>
    <div class="col-span-1 w-full md:w-auto text-right md:-mr-2">
      <div class="w-8 h-8 rounded-md flex items-center justify-center text-text-light group-hover:text-midnight-navy group-hover:bg-gray-100 transition-all">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="9 18 15 12 9 6"/></svg>
      </div>
    </div>
  </div>

  <!-- Card Mode (Stripe Insight/Search Card) -->
  <div v-else 
       @click="handleClick"
       class="group bg-white rounded-xl border border-border-default/60 shadow-stripe-sm hover:shadow-premium hover:-translate-y-0.5 transition-all duration-300 cursor-pointer overflow-hidden max-w-full">
    <div class="p-6">
      <div class="flex items-center justify-between mb-8">
        <div class="bg-gray-50 p-2.5 rounded-lg group-hover:bg-stripe-indigo/5 transition-colors">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 text-text-light group-hover:text-stripe-indigo transition-colors" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/></svg>
        </div>
        <span :style="ordem.statusCor ? { backgroundColor: ordem.statusCor + '20', color: ordem.statusCor } : {}"
              :class="['badge-stripe px-2.5 py-0.5 rounded-md text-[10px] font-bold uppercase tracking-widest', !ordem.statusCor ? getStatusBadgeClasses(ordem.status) : '']">
          {{ ordem.statusNome || getStatusLabel(ordem.status) }}
        </span>
      </div>

      <div class="mb-6">
        <p class="text-[11px] font-bold text-text-muted uppercase tracking-widest mb-1.5 opacity-70">Detalhes da O.S.</p>
        <h3 class="text-lg font-bold text-midnight-navy tracking-tight-stripe leading-tight mb-1">
          #{{ ordem.numeroOS || ordem.numeroOs }} — {{ ordem.nomeVeiculo || ordem.veiculo }}
        </h3>
        <p class="text-sm font-semibold text-text-muted">Entrada em {{ formatDate(ordem.dataAbertura || ordem.dataEntrada) }}</p>
      </div>

      <div class="flex items-center justify-between mt-auto pt-6 border-t border-gray-50">
        <div class="flex flex-col">
          <span class="text-[11px] font-bold text-text-muted uppercase tracking-[0.1em] mb-0.5 opacity-60">Faturamento</span>
          <span class="text-[17px] font-bold text-midnight-navy group-hover:text-stripe-indigo transition-colors">{{ formatCurrency(ordem.valorTotal) }}</span>
        </div>
        <div class="w-8 h-8 rounded-full flex items-center justify-center border border-border-default text-text-light group-hover:border-stripe-indigo group-hover:text-stripe-indigo transition-all">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="9 18 15 12 9 6"/></svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const props = defineProps({
  ordem: {
    type: Object,
    required: true,
  },
  showDetails: {
    type: Boolean,
    default: true,
  },
  isListItem: {
    type: Boolean,
    default: false,
  },
});

const router = useRouter();

const handleClick = () => {
  if (props.showDetails && props.ordem.id) {
    console.log('Navigating to OS:', props.ordem.id);
    router.push(`/os/${props.ordem.id}`);
  } else {
    console.warn('Click ignored: showDetails=', props.showDetails, 'id=', props.ordem.id);
  }
};

const getStatusBadgeClasses = (status) => {
  const statusMap = {
    'AGUARDANDO_APROVACAO': 'bg-amber-50 text-accent-amber border-amber-100/50',
    'EM_EXECUCAO': 'bg-blue-50 text-stripe-indigo border-blue-100/50',
    'AGUARDANDO_PECAS': 'bg-purple-50 text-purple-600 border-purple-100/50',
    'FINALIZADO': 'bg-emerald-50 text-accent-emerald border-emerald-100/50',
    'ENTREGUE': 'bg-gray-50 text-text-muted border-gray-100',
    'CANCELADO': 'bg-red-50 text-accent-red border-red-100/50',
  };
  return statusMap[status] || 'bg-gray-50 text-text-light border-gray-100';
};

const getStatusLabel = (status) => {
  const statusLabels = {
    'AGUARDANDO_APROVACAO': 'Pendente',
    'EM_EXECUCAO': 'Processando',
    'AGUARDANDO_PECAS': 'Aguard. Peças',
    'FINALIZADO': 'Concluído',
    'ENTREGUE': 'Arquivado',
    'CANCELADO': 'Cancelado',
  };
  return statusLabels[status] || status;
};

const formatDate = (date) => {
  if (!date) return '-';
  const d = new Date(date);
  return d.toLocaleDateString('pt-BR', { day: '2-digit', month: 'short', year: 'numeric' });
};

const formatCurrency = (value) => {
  if (!value) return 'R$ 0,00';
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value);
};
</script>

<style scoped>
</style>
