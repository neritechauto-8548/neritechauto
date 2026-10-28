export type OrderItem = {
  descricao: string;
  quantidade?: number;
  valorUnitario?: number;
  valorTotal?: number;
};
export type OrderEvent = {
  status: string;
  data?: string;
  descricao?: string;
};
export type Order = {
  id: number;
  numeroOs: string;
  status: string;
  statusNome?: string;
  veiculo: string;
  placa?: string;
  dataEntrada?: string;
  previsaoEntrega?: string;
  km?: number;
  observacoes?: string;
  valorServicos?: number;
  valorPecas?: number;
  valorDesconto?: number;
  valorTotal?: number;
  servicos: OrderItem[];
  pecas: OrderItem[];
  historicoStatus: OrderEvent[];
  // O backend autorizado precisa controlar explicitamente esta capacidade.
  podeAprovar: boolean;
};
export type HistoryResult = { content: Order[]; totalElements: number };
export type ApiError = { message: string };
