export type OsFinanceState = 'idle' | 'loading' | 'ready' | 'forbidden' | 'error';

export interface OsInvoiceSummary {
  id: number;
  numeroFatura?: string | null;
  ordemServicoId?: number | null;
  dataEmissao?: string | null;
  dataVencimento?: string | null;
  valorTotal?: number | null;
  valorPago?: number | null;
  valorPendente?: number | null;
  status?: string | null;
  formaPagamentoNome?: string | null;
  condicaoPagamentoNome?: string | null;
}

export interface OsPaymentSummary {
  id: number;
  faturaId?: number | null;
  osId?: number | null;
  dataPagamento?: string | null;
  valorTotal?: number | null;
  status?: string | null;
  formaPagamentoNome?: string | null;
  contaBancariaNome?: string | null;
}

export interface OsPaymentsPage {
  content: OsPaymentSummary[];
  totalElements?: number;
  totalPages?: number;
  number?: number;
  size?: number;
}
