<template>
  <div class="detalhe-page bg-light-bg min-h-screen font-sans pb-24">
    <!-- Header Navigation -->
    <header class="bg-white border-b border-border-default sticky top-0 z-30">
      <div class="max-w-[1400px] mx-auto px-4 md:px-8">
        <div class="flex items-center justify-between h-14 md:h-16">
          <div class="flex items-center gap-10">
            <div class="flex items-center gap-3">
              <div class="w-7 h-7 bg-stripe-indigo rounded-lg flex items-center justify-center text-white shadow-sm">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2L2 7l10 5 10-5-10-5z"/><path d="M2 17l10 5 10-5"/><path d="M2 12l10 5 10-5"/></svg>
              </div>
              <span class="text-[14px] font-bold text-midnight-navy tracking-tight uppercase">NeriTech Portal</span>
            </div>
            
            <nav class="hidden md:flex items-center gap-8">
              <router-link v-if="isAuthenticated" to="/historico" class="text-[13px] font-semibold text-text-muted hover:text-midnight-navy transition-colors h-14 md:h-16 flex items-center">Histórico</router-link>
              <div class="text-[13px] font-bold text-stripe-indigo border-b-2 border-stripe-indigo h-14 md:h-16 flex items-center">Detalhes da O.S.</div>
            </nav>
          </div>
          
          <div class="flex items-center gap-4">
            <button @click="handlePrint" class="hidden md:flex items-center gap-2 text-[13px] font-bold text-text-muted hover:text-midnight-navy transition-colors px-3 py-1.5 rounded-md hover:bg-gray-50">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><polyline points="6 9 6 2 18 2 18 9"/><path d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2"/><rect x="6" y="14" width="12" height="8"/></svg>
              Imprimir Ficha
            </button>
            <div class="w-px h-5 bg-border-default hidden md:block"></div>
            <button @click="$router.push('/historico')" class="text-[13px] font-bold text-text-muted hover:text-midnight-navy transition-colors px-3 py-1.5 rounded-md hover:bg-gray-50 flex items-center gap-1.5">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="19" y1="12" x2="5" y2="12"/><polyline points="12 19 5 12 12 5"/></svg>
              Voltar
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Loading State -->
    <div v-if="loading" class="flex flex-col items-center justify-center py-32 animate-fade-in">
      <svg class="animate-spin h-8 w-8 text-stripe-indigo mb-4" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="3"></circle>
        <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
      </svg>
      <p class="text-[13px] text-text-muted font-medium">Buscando detalhes do serviço...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="errorMessage" class="max-w-2xl mx-auto mt-20 bg-white rounded-xl shadow-premium border border-border-default p-12 text-center animate-fade-in">
      <div class="w-14 h-14 bg-red-50 text-accent-red rounded-full flex items-center justify-center mx-auto mb-6">
        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
      </div>
      <h3 class="text-xl font-bold text-midnight-navy mb-2 tracking-tight">Falha na conexão</h3>
      <p class="text-[14px] text-text-muted font-medium mb-8 leading-relaxed">{{ errorMessage }}</p>
      <button @click="loadDetalhe" class="btn-stripe-primary px-8">Tentar novamente</button>
    </div>

    <!-- Main Content -->
    <div v-else-if="ordem" class="max-w-6xl mx-auto px-4 pt-8 md:pt-12 animate-fade-in">
      
      <!-- Approval Banner -->
      <div v-if="ordem.status === 'AGUARDANDO_APROVACAO'" class="bg-indigo-50 border border-stripe-indigo/20 rounded-xl p-5 md:p-6 mb-8 flex flex-col md:flex-row md:items-center justify-between gap-6 shadow-sm">
        <div class="flex items-start gap-4">
          <div class="w-10 h-10 rounded-full bg-stripe-indigo/10 flex items-center justify-center text-stripe-indigo shrink-0 mt-0.5">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
          </div>
          <div>
            <h3 class="text-lg font-bold text-midnight-navy tracking-tight mb-1">Aprovação Necessária</h3>
            <p class="text-[14px] text-text-main font-medium leading-relaxed max-w-2xl">
              O orçamento técnico para o veículo <strong>{{ ordem.veiculo }}</strong> está pronto. Revise os serviços aplicados abaixo e autorize o início dos trabalhos.
            </p>
          </div>
        </div>
        <div class="flex items-center gap-3 shrink-0">
          <button @click="confirmDecline = true" class="btn-stripe-secondary border-transparent bg-white shadow-sm hover:border-red-200 hover:text-accent-red" :disabled="actionLoading">
            Solicitar Revisão
          </button>
          <button @click="handleApprove" class="btn-stripe-primary shadow-lg shadow-stripe-indigo/20" :disabled="actionLoading">
            <span v-if="actionLoading" class="flex items-center gap-2">
              <svg class="animate-spin h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
              Processando...
            </span>
            <span v-else>Autorizar Serviço</span>
          </button>
        </div>
      </div>

      <!-- Page Header -->
      <div class="flex flex-col md:flex-row md:items-center justify-between gap-6 mb-8">
        <div>
          <div class="flex flex-col md:flex-row md:items-center gap-4">
          <h1 class="text-2xl md:text-3xl font-extrabold text-midnight-navy tracking-tight-stripe">
            O.S. #{{ ordem.numeroOS || ordem.numeroOs }}
          </h1>
          <div class="flex items-center gap-2">
            <span :style="ordem.statusCor ? { backgroundColor: ordem.statusCor + '20', color: ordem.statusCor, borderColor: ordem.statusCor + '40' } : {}"
                  :class="['badge-stripe inline-flex items-center px-3 py-1 rounded-full text-[12px] font-bold uppercase tracking-wider border', !ordem.statusCor ? getStatusBadgeClasses(ordem.status) : '']">
              <span class="w-1.5 h-1.5 rounded-full bg-current mr-2 animate-pulse"></span>
              {{ ordem.statusNome || getStatusLabel(ordem.status) }}
            </span>
          </div>
        </div>
  <p class="text-[15px] text-text-muted font-semibold flex items-center gap-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
            Entrada em {{ formatDate(ordem.dataEntrada) }}
          </p>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-12 gap-8">
        
        <!-- Left Column (Details) -->
        <div class="lg:col-span-8 flex flex-col gap-8">
          
          <!-- General Info Card -->
          <div class="bg-white rounded-xl border border-border-default shadow-stripe-sm overflow-hidden">
            <div class="px-6 py-5 border-b border-gray-50 bg-gray-50/30">
              <h2 class="text-[13px] font-bold text-text-muted uppercase tracking-widest">Detalhes do Veículo</h2>
            </div>
            <div class="p-6">
              <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                <div>
                  <p class="text-[11px] font-bold text-text-muted uppercase tracking-widest mb-1 opacity-70">Modelo</p>
                  <p class="text-[15px] font-bold text-midnight-navy">{{ ordem.veiculo }}</p>
                </div>
                <div>
                  <p class="text-[11px] font-bold text-text-muted uppercase tracking-widest mb-1 opacity-70">Placa</p>
                  <p class="text-[15px] font-bold text-midnight-navy tracking-widest">{{ ordem.placa || 'N/A' }}</p>
                </div>
                <div>
                  <p class="text-[11px] font-bold text-text-muted uppercase tracking-widest mb-1 opacity-70">Quilometragem</p>
                  <p class="text-[15px] font-bold text-midnight-navy">{{ formatNumber(ordem.km) }} km</p>
                </div>
                <div>
                  <p class="text-[11px] font-bold text-text-muted uppercase tracking-widest mb-1 opacity-70">Mecânico</p>
                  <p class="text-[14px] font-semibold text-text-main">{{ ordem.mecanico || 'Não atribuído' }}</p>
                </div>
                <div>
                  <p class="text-[11px] font-bold text-text-muted uppercase tracking-widest mb-1 opacity-70">Previsão</p>
                  <p class="text-[14px] font-semibold text-text-main">{{ formatDate(ordem.previsaoEntrega) }}</p>
                </div>
              </div>
              
              <div v-if="ordem.observacoes" class="mt-8 pt-6 border-t border-gray-50">
                <p class="text-[11px] font-bold text-text-muted uppercase tracking-widest mb-2 opacity-70">Sintomas / Observações Iniciais</p>
                <p class="text-[14px] text-text-main leading-relaxed bg-gray-50 rounded-lg p-4 font-medium border border-border-default/50">{{ ordem.observacoes }}</p>
              </div>
            </div>
          </div>

          <!-- Services List -->
          <div v-if="ordem.servicos && ordem.servicos.length > 0" class="bg-white rounded-xl border border-border-default shadow-stripe-sm overflow-hidden">
            <div class="flex items-center justify-between px-6 py-5 border-b border-gray-50 bg-gray-50/30">
              <h2 class="text-[13px] font-bold text-text-muted uppercase tracking-widest">Serviços Executados</h2>
              <span class="text-[12px] font-bold text-text-muted bg-white border border-border-default px-2 py-0.5 rounded-md">{{ ordem.servicos.length }}</span>
            </div>
            <div class="px-6 py-2 divide-y divide-gray-50">
              <OsServicoItem
                v-for="(servico, index) in ordem.servicos"
                :key="index"
                :servico="servico"
                class="py-4"
              />
            </div>
          </div>

          <!-- Parts List -->
          <div v-if="(ordem.produtos || ordem.pecas) && (ordem.produtos || ordem.pecas).length > 0" class="bg-white rounded-xl border border-border-default shadow-stripe-sm overflow-hidden">
            <div class="flex items-center justify-between px-6 py-5 border-b border-gray-50 bg-gray-50/30">
              <h2 class="text-[13px] font-bold text-text-muted uppercase tracking-widest">Peças Aplicadas</h2>
              <span class="text-[12px] font-bold text-text-muted bg-white border border-border-default px-2 py-0.5 rounded-md">{{ (ordem.produtos || ordem.pecas).length }}</span>
            </div>
            <div class="px-6 py-2 divide-y divide-gray-50">
              <OsPecaItem
                v-for="(peca, index) in (ordem.produtos || ordem.pecas)"
                :key="index"
                :peca="peca"
                class="py-4"
              />
            </div>
          </div>

          <!-- Gallery -->
          <div v-if="ordem.fotos && ordem.fotos.length > 0" class="bg-white rounded-xl border border-border-default shadow-stripe-sm overflow-hidden">
            <div class="px-6 py-5 border-b border-gray-50 bg-gray-50/30">
              <h2 class="text-[13px] font-bold text-text-muted uppercase tracking-widest">Documentação Fotográfica</h2>
            </div>
            <div class="p-6">
              <OsFotoViewer :fotos="ordem.fotos" />
            </div>
          </div>
          
        </div>

        <!-- Right Column (Timeline & Invoice) -->
        <div class="lg:col-span-4 flex flex-col gap-8">
          
          <!-- Invoice Summary -->
          <div class="bg-white rounded-xl border border-border-default shadow-stripe-sm overflow-hidden sticky top-24">
            <div class="px-6 py-5 border-b border-gray-50 bg-gray-50/30">
              <h2 class="text-[13px] font-bold text-text-muted uppercase tracking-widest">Resumo Financeiro</h2>
            </div>
            <div class="p-6">
              <div class="flex flex-col gap-4 mb-6">
                <!-- Subtotals -->
                <div class="flex items-center justify-between">
                  <span class="text-[14px] font-semibold text-text-muted">Total de Serviços</span>
                  <span class="text-[14px] font-bold text-midnight-navy">{{ formatCurrency(ordem.valorServicos || calcularTotalServicos()) }}</span>
                </div>
                <div class="flex items-center justify-between">
                  <span class="text-[14px] font-semibold text-text-muted">Total de Peças</span>
                  <span class="text-[14px] font-bold text-midnight-navy">{{ formatCurrency(ordem.valorProdutos || calcularTotalPecas()) }}</span>
                </div>
                <div v-if="(ordem.valorDesconto || ordem.desconto) > 0" class="flex items-center justify-between text-accent-emerald">
                  <span class="text-[14px] font-semibold">Desconto Aplicado</span>
                  <span class="text-[14px] font-bold">- {{ formatCurrency(ordem.valorDesconto || ordem.desconto) }}</span>
                </div>
              </div>
              
              <!-- Total -->
              <div class="pt-5 border-t border-border-default flex items-center justify-between">
                <span class="text-[15px] font-bold text-midnight-navy uppercase tracking-widest">Total Geral</span>
                <span class="text-2xl font-extrabold text-stripe-indigo tracking-tight">{{ formatCurrency(ordem.valorTotal) }}</span>
              </div>
            </div>
          </div>

          <!-- Timeline -->
          <div class="bg-white rounded-xl border border-border-default shadow-stripe-sm overflow-hidden">
            <div class="px-6 py-5 border-b border-gray-50 bg-gray-50/30">
              <h2 class="text-[13px] font-bold text-text-muted uppercase tracking-widest">Log de Atividades</h2>
            </div>
            <div class="p-6">
              <OsStatusTimeline
                :current-status="ordem.status"
                :historico="ordem.historicoStatus || []"
              />
            </div>
          </div>

        </div>
      </div>
    </div>

    <!-- Decline Modal -->
    <div v-if="confirmDecline" class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-midnight-navy/40 backdrop-blur-sm animate-fade-in">
      <div class="bg-white rounded-2xl shadow-premium border border-white/20 w-full max-w-md overflow-hidden transform transition-all">
        <div class="px-6 py-5 border-b border-gray-50 flex justify-between items-center">
          <h3 class="text-[15px] font-bold text-midnight-navy tracking-tight">Solicitar Revisão de Orçamento</h3>
          <button @click="confirmDecline = false" class="text-text-light hover:text-midnight-navy transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/></svg>
          </button>
        </div>
        <div class="p-6">
          <p class="text-[14px] font-medium text-text-main mb-6 leading-relaxed">
            Por favor, informe o motivo pelo qual você deseja solicitar uma revisão. Nossa equipe técnica entrará em contato.
          </p>
          <div class="mb-2">
            <label class="block text-[12px] font-bold text-text-muted uppercase tracking-widest mb-2" for="reason">Motivo da Recusa</label>
            <textarea
              id="reason"
              v-model="reclineReason"
              rows="4"
              class="form-input-stripe text-[14px] resize-none focus:ring-red-100 focus:border-accent-red"
              placeholder="Ex: Gostaria de retirar o serviço X..."
            ></textarea>
          </div>
        </div>
        <div class="px-6 py-5 bg-gray-50 flex items-center justify-end gap-3 border-t border-border-default">
          <button @click="confirmDecline = false" class="btn-stripe-secondary shadow-none px-5 border-transparent bg-transparent hover:bg-gray-200">Cancelar</button>
          <button @click="handleDecline" class="btn-stripe-primary bg-accent-red hover:bg-red-700 shadow-lg shadow-red-500/20 px-6" :disabled="!reclineReason || actionLoading">
            <span v-if="actionLoading" class="flex items-center gap-2">
              <svg class="animate-spin h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24"><circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle><path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path></svg>
              Enviando...
            </span>
            <span v-else>Confirmar Recusa</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import osService, { auth } from '@/api/osService';
import OsStatusTimeline from '@/components/OsStatusTimeline.vue';
import OsServicoItem from '@/components/OsServicoItem.vue';
import OsPecaItem from '@/components/OsPecaItem.vue';
import OsFotoViewer from '@/components/OsFotoViewer.vue';

const route = useRoute();
const router = useRouter();

const isAuthenticated = computed(() => auth.isAuthenticated());
const ordem = ref(null);
const loading = ref(true);
const errorMessage = ref('');

const actionLoading = ref(false);
const confirmDecline = ref(false);
const reclineReason = ref('');

const loadDetalhe = async () => {
  loading.value = true;
  errorMessage.value = '';
  
  try {
    const response = await osService.obterOS(route.params.id);
    ordem.value = response.data;
  } catch (error) {
    console.error('Erro ao carregar O.S.:', error);
    errorMessage.value = 'Não foi possível carregar os detalhes do serviço. Verifique sua conexão e tente novamente.';
  } finally {
    loading.value = false;
  }
};

const handleApprove = async () => {
  if (!confirm('Tem certeza que deseja autorizar a execução desta Ordem de Serviço? O valor total será confirmado.')) return;
  
  actionLoading.value = true;
  try {
    await osService.aprovarOS(ordem.value.id);
    await loadDetalhe();
    // Use timeout to allow UI update before alert block
    setTimeout(() => {
      alert('Ordem de Serviço autorizada com sucesso! A equipe técnica será notificada.');
    }, 100);
  } catch (error) {
    console.error('Erro ao aprovar:', error);
    alert('Houve um problema ao processar a aprovação. Contacte a oficina.');
  } finally {
    actionLoading.value = false;
  }
};

const handleDecline = async () => {
  if (!reclineReason.value) return;
  
  actionLoading.value = true;
  try {
    await osService.negarOS(ordem.value.id, reclineReason.value);
    confirmDecline.value = false;
    await loadDetalhe();
    setTimeout(() => {
      alert('Sua solicitação de revisão foi enviada com sucesso e a equipe será notificada.');
    }, 100);
  } catch (error) {
    console.error('Erro ao recusar:', error);
    alert('Ocorreu um erro ao enviar sua recusa. Tente novamente.');
  } finally {
    actionLoading.value = false;
  }
};

const calcularTotalServicos = () => {
  if (!ordem.value || !ordem.value.servicos) return 0;
  return ordem.value.servicos.reduce((total, serv) => total + (serv.valor || 0), 0);
};

const calcularTotalPecas = () => {
  if (!ordem.value || !ordem.value.pecas) return 0;
  return ordem.value.pecas.reduce((total, peca) => total + ((peca.valorUnitario || 0) * (peca.quantidade || 1)), 0);
};

const getStatusBadgeClasses = (status) => {
  const statusMap = {
    'AGUARDANDO_APROVACAO': 'bg-amber-50 text-accent-amber border-amber-200/50',
    'EM_EXECUCAO': 'bg-blue-50 text-stripe-indigo border-stripe-indigo/20',
    'AGUARDANDO_PECAS': 'bg-purple-50 text-purple-600 border-purple-200/50',
    'FINALIZADO': 'bg-emerald-50 text-accent-emerald border-accent-emerald/20',
    'ENTREGUE': 'bg-gray-50 text-text-muted border-gray-200',
    'CANCELADO': 'bg-red-50 text-accent-red border-red-200/50',
  };
  return statusMap[status] || 'bg-gray-50 text-text-muted border-gray-200';
};

const getStatusLabel = (status) => {
  const statusLabels = {
    'AGUARDANDO_APROVACAO': 'Aprovação Pendente',
    'EM_EXECUCAO': 'Em Execução',
    'AGUARDANDO_PECAS': 'Aguardando Material',
    'FINALIZADO': 'Manutenção Concluída',
    'ENTREGUE': 'Entrega Realizada',
    'CANCELADO': 'Serviço Cancelado',
  };
  return statusLabels[status] || status;
};

const formatDate = (date) => {
  if (!date) return '-';
  return new Date(date).toLocaleDateString('pt-BR', { day: '2-digit', month: 'long', year: 'numeric' });
};

const formatCurrency = (value) => {
  if (!value) return 'R$ 0,00';
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
};

const formatNumber = (value) => {
  if (!value) return '0';
  return new Intl.NumberFormat('pt-BR').format(value);
};

const handlePrint = () => {
  window.print();
};

onMounted(() => {
  loadDetalhe();
});
</script>

<style scoped>
@media print {
  header { display: none !important; }
  .btn-stripe-primary, .btn-stripe-secondary { display: none !important; }
  body { background-color: white !important; }
  .shadow-stripe-sm, .shadow-premium { box-shadow: none !important; border-color: #e6ebf1 !important; }
}
</style>
