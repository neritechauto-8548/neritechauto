import { ItemOSProdutoResponse, ItemOSServicoResponse } from '../models/os.models';

export type OsOperationsTab = 'scope' | 'diagnostics' | 'checklist' | 'evidence';
export type OsSectionState = 'idle' | 'loading' | 'ready' | 'forbidden' | 'error';

export interface OsChecklistItem {
  id: number;
  ordemServicoId: number;
  checklistModeloId?: number | null;
  itemModeloId?: number | null;
  descricao?: string | null;
  feito: boolean;
  ordem?: number | null;
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
}

export interface OsPhotoEvidence {
  id: number;
  empresaId: number;
  ordemServicoId: number;
  arquivoUrl?: string | null;
  contentType?: string | null;
  tamanho?: number | null;
  descricao?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
}

export type OsDiagnosticUrgency = 'BAIXA' | 'MEDIA' | 'ALTA' | 'CRITICA';
export type OsVehicleSystem =
  | 'MOTOR'
  | 'TRANSMISSAO'
  | 'FREIOS'
  | 'SUSPENSAO'
  | 'DIRECAO'
  | 'ELETRICO'
  | 'CLIMATIZACAO'
  | 'COMBUSTIVEL'
  | 'ESCAPE'
  | 'OUTROS';

export interface OsDiagnosticRequest {
  ordemServicoId: number;
  sistemaVeiculo?: OsVehicleSystem | null;
  componenteEspecifico?: string | null;
  problemaIdentificado: string;
  causaProvavel?: string | null;
  solucaoRecomendada?: string | null;
  urgencia?: OsDiagnosticUrgency | null;
  impactoSeguranca?: boolean | null;
  impactoDirigibilidade?: boolean | null;
  custoEstimado?: number | null;
  tempoEstimadoReparo?: number | null;
  ferramentasNecessarias?: string | null;
  pecasNecessarias?: string | null;
  evidenciasEncontradas?: string | null;
  testesRealizados?: string | null;
  codigoErro?: string | null;
  mecanicoDiagnosticoId?: number | null;
  fotosDiagnostico?: string | null;
  videosDiagnostico?: string | null;
  aprovadoCliente?: boolean | null;
  observacoes?: string | null;
}

export interface OsDiagnosticResponse extends OsDiagnosticRequest {
  id: number;
  dataDiagnostico?: string | null;
  dataAprovacaoCliente?: string | null;
  dataCadastro?: string | null;
  dataAtualizacao?: string | null;
  versao?: number | null;
}

export interface OsOperationalScope {
  products: ItemOSProdutoResponse[];
  services: ItemOSServicoResponse[];
}

export interface OsSectionStatus {
  state: OsSectionState;
  message: string;
}

export const DIAGNOSTIC_URGENCY_OPTIONS: Array<{ label: string; value: OsDiagnosticUrgency }> = [
  { label: 'Baixa', value: 'BAIXA' },
  { label: 'Média', value: 'MEDIA' },
  { label: 'Alta', value: 'ALTA' },
  { label: 'Crítica', value: 'CRITICA' },
];

export const VEHICLE_SYSTEM_OPTIONS: Array<{ label: string; value: OsVehicleSystem }> = [
  { label: 'Motor', value: 'MOTOR' },
  { label: 'Transmissão', value: 'TRANSMISSAO' },
  { label: 'Freios', value: 'FREIOS' },
  { label: 'Suspensão', value: 'SUSPENSAO' },
  { label: 'Direção', value: 'DIRECAO' },
  { label: 'Elétrico', value: 'ELETRICO' },
  { label: 'Climatização', value: 'CLIMATIZACAO' },
  { label: 'Combustível', value: 'COMBUSTIVEL' },
  { label: 'Escape', value: 'ESCAPE' },
  { label: 'Outros', value: 'OUTROS' },
];
