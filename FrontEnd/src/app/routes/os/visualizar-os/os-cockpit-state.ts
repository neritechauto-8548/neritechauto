import { OrdemServicoResponse } from '../models/os.models';

export type CockpitLoadState =
  | 'idle'
  | 'loading'
  | 'ready'
  | 'forbidden'
  | 'not-found'
  | 'conflict'
  | 'error';

export type CockpitItemState = 'aprovado' | 'aguardando';

export interface CockpitLoadError {
  state: Exclude<CockpitLoadState, 'idle' | 'loading' | 'ready'>;
  message: string;
}

/**
 * O cockpit precisa representar falhas reais sem substituir a resposta por dados
 * demonstrativos. A documentação D4 exige estados 403/404/409 explícitos.
 */
export function resolveCockpitLoadError(status?: number): CockpitLoadError {
  if (status === 403) {
    return {
      state: 'forbidden',
      message: 'Você não possui permissão para visualizar esta ordem de serviço.',
    };
  }

  if (status === 404) {
    return {
      state: 'not-found',
      message: 'A ordem de serviço não foi encontrada ou não está disponível neste contexto.',
    };
  }

  if (status === 409) {
    return {
      state: 'conflict',
      message: 'A ordem de serviço foi alterada em outro contexto. Atualize os dados antes de continuar.',
    };
  }

  return {
    state: 'error',
    message: 'Não foi possível carregar a ordem de serviço. Tente atualizar a página.',
  };
}

/**
 * O contrato atual expõe somente aprovadoCliente booleano. `false`/ausente não
 * prova recusa; portanto o frontend não inventa estado "negado".
 */
export function resolveItemState(aprovadoCliente?: boolean): CockpitItemState {
  return aprovadoCliente === true ? 'aprovado' : 'aguardando';
}

/**
 * O valor financeiro exibido pelo cockpit vem da OS/read model autoritativo.
 * Nunca recalcular a verdade financeira no browser a partir da tabela visual.
 */
export function authoritativeOsTotal(os?: Pick<OrdemServicoResponse, 'valorTotal'>): number {
  const total = Number(os?.valorTotal ?? 0);
  return Number.isFinite(total) ? total : 0;
}
