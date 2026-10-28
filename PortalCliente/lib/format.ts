import type { Order, OrderEvent, OrderItem, HistoryResult } from './types';

type UnknownRecord = Record<string, unknown>;
const obj = (value: unknown): UnknownRecord =>
  value !== null && typeof value === 'object' && !Array.isArray(value) ? value as UnknownRecord : {};
const str = (value: unknown) => typeof value === 'string' ? value : typeof value === 'number' ? String(value) : '';
const num = (value: unknown) => typeof value === 'number' && Number.isFinite(value) ? value :
  typeof value === 'string' && value.trim() && Number.isFinite(Number(value)) ? Number(value) : undefined;
const list = (value: unknown): unknown[] => Array.isArray(value) ? value : [];
const digits = (value: string) => value.replace(/\D/g, '');
export const normalizeCpf = (cpf: string) => digits(cpf).slice(0, 11);
export function isValidCpf(cpf: string): boolean {
  const v = normalizeCpf(cpf);
  if (v.length !== 11 || /^(\d)\1{10}$/.test(v)) return false;
  for (const len of [9,10]) {
    let sum = 0;
    for (let i = 0; i < len; i++) sum += Number(v[i]) * (len + 1 - i);
    const n = (sum * 10) % 11;
    if ((n === 10 ? 0 : n) !== Number(v[len])) return false;
  }
  return true;
}
export function money(v: unknown): string {
  const n = num(v);
  return n === undefined ? '—' : new Intl.NumberFormat('pt-BR', { style: 'currency',currency:'BRL' }).format(n);
}
export function dateBr(v: unknown): string {
  if (!v || typeof v !== 'string') return 'Não informada';
  const d = new Date(v);
  return Number.isNaN(d.getTime()) ? 'Não informada' : new Intl.DateTimeFormat('pt-BR',{dateStyle:'medium'}).format(d);
}
export function statusLabel(status: string): string {
  const labels: Record<string,string> = {
    AGUARDANDO_APROVACAO:'Aguardando aprovação',
    PENDENTE:'Aguardando aprovação',
    ABERTA:'Recebida',
    ABERTO:'Recebida',
    EM_EXECUCAO:'Em serviço',
    EM_ANDAMENTO:'Em serviço',
    AGUARDANDO_PECAS:'Aguardando peças',
    FINALIZADO:'Concluída',
    CONCLUIDA:'Concluída',
    ENTREGUE:'Entregue',
    CANCELADO:'Cancelada',
    CANCELADA:'Cancelada',
  };
  return labels[status] ?? (status ? status.replace(/_/g,' ').toLowerCase() : 'Status indisponível');
}
function item(value: unknown): OrderItem {
  const d = obj(value);
  return {
    descricao: str(d.descricao ?? d.nome ?? d.servico) || 'Item',
    quantidade: num(d.quantidade),
    valorUnitario: num(d.valorUnitario ?? d.valor),
    valorTotal: num(d.valorTotal),
  };
}
function event(value: unknown): OrderEvent {
  const d = obj(value);
  return { status:str(d.status), data:str(d.data ?? d.dataHora), descricao:str(d.descricao) };
}
export function normalizeOrder(value: unknown): Order | null {
  const d=obj(value), id=num(d.id);
  if (id === undefined || !Number.isSafeInteger(id) || id <= 0) return null;
  const vehicle = obj(d.veiculo);
  return {
    id, numeroOs:str(d.numeroOS ?? d.numeroOs ?? d.numero ?? id),
    status:str(d.status), statusNome:str(d.statusNome),
    veiculo:str(d.nomeVeiculo ?? (typeof d.veiculo === 'string' ? d.veiculo : vehicle.modeloNome ?? vehicle.modelo)) || 'Veículo',
    placa:str(d.placaVeiculo ?? d.placa ?? vehicle.placa),
    dataEntrada:str(d.dataEntrada ?? d.dataAbertura),
    previsaoEntrega:str(d.previsaoEntrega), km:num(d.km),
    observacoes:str(d.observacoes), valorServicos:num(d.valorServicos ?? d.valorMaoObra),
    valorPecas:num(d.valorPecas ?? d.valorProdutos),
    valorDesconto:num(d.valorDesconto ?? d.desconto),
    valorTotal:num(d.valorTotal), servicos:list(d.servicos).map(item),
    pecas:list(d.pecas ?? d.produtos).map(item),
    historicoStatus:list(d.historicoStatus).map(event),
    podeAprovar:d.podeAprovar === true && ['AGUARDANDO_APROVACAO','PENDENTE'].includes(str(d.status)),
  };
}
export function normalizeHistory(value: unknown): HistoryResult {
  const d=obj(value);
  const items=Array.isArray(value)?value:list(d.content);
  const content=items.map(normalizeOrder).filter((v):v is Order=>v!==null);
  return {content, totalElements:num(d.totalElements) ?? content.length};
}
