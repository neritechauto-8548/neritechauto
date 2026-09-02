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

export type CockpitOperationalStage =
  | 'contexto'
  | 'autorizacao'
  | 'preparacao'
  | 'execucao'
  | 'revisao'
  | 'financeiro'
  | 'entrega'
  | 'concluida';

export interface CockpitLoadError {
  state: Exclude<CockpitLoadState, 'idle' | 'loading' | 'ready'>;
  message: string;
}

export interface CockpitActionContext {
  os?: Pick<
    OrdemServicoResponse,
    | 'consultorResponsavelId'
    | 'mecanicoResponsavelId'
    | 'dataInicioExecucao'
    | 'dataFimExecucao'
    | 'dataEntrega'
  >;
  pendingApprovalCount?: number;
  checklistPendingCount?: number;
  finalized?: boolean;
  paid?: boolean;
  partialSources?: boolean;
}

export interface CockpitNextAction {
  stage: CockpitOperationalStage;
  title: string;
  description: string;
  actionKey:
    | 'refresh'
    | 'assign-owner'
    | 'review-approvals'
    | 'complete-checklist'
    | 'start-execution'
    | 'review-close'
    | 'open-finance'
    | 'prepare-delivery'
    | 'completed';
  blocking: boolean;
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

/**
 * Deriva a orientação operacional usando apenas fatos já persistidos ou
 * projeções explicitamente carregadas pelo cockpit. A ordem abaixo não cria
 * um novo workflow: ela prioriza bloqueios reais para sugerir a próxima ação.
 */
export function deriveCockpitNextAction(context: CockpitActionContext): CockpitNextAction {
  if (context.partialSources) {
    return {
      stage: 'contexto',
      title: 'Revisar dados indisponíveis',
      description: 'Uma ou mais fontes do cockpit não responderam. Atualize os dados antes de uma decisão operacional.',
      actionKey: 'refresh',
      blocking: true,
    };
  }

  if (!context.os?.consultorResponsavelId && !context.os?.mecanicoResponsavelId) {
    return {
      stage: 'preparacao',
      title: 'Definir responsável',
      description: 'A OS ainda não possui responsável operacional associado.',
      actionKey: 'assign-owner',
      blocking: true,
    };
  }

  if (Number(context.pendingApprovalCount || 0) > 0) {
    return {
      stage: 'autorizacao',
      title: 'Revisar autorizações pendentes',
      description: 'Existem itens ou adicionais sem autorização confirmada do cliente.',
      actionKey: 'review-approvals',
      blocking: true,
    };
  }

  if (Number(context.checklistPendingCount || 0) > 0) {
    return {
      stage: 'preparacao',
      title: 'Concluir checklist pendente',
      description: 'Há itens de checklist ainda não concluídos para esta OS.',
      actionKey: 'complete-checklist',
      blocking: true,
    };
  }

  if (!context.os?.dataInicioExecucao && !context.finalized) {
    return {
      stage: 'execucao',
      title: 'Iniciar execução',
      description: 'O contexto necessário está disponível e ainda não há início de execução registrado.',
      actionKey: 'start-execution',
      blocking: false,
    };
  }

  if (!context.finalized) {
    return {
      stage: context.os?.dataFimExecucao ? 'revisao' : 'execucao',
      title: context.os?.dataFimExecucao ? 'Revisar e finalizar OS' : 'Continuar execução',
      description: context.os?.dataFimExecucao
        ? 'A execução possui término registrado. Revise escopo, evidências e condições antes do fechamento.'
        : 'A execução está em andamento. Mantenha apontamentos, peças e evidências atualizados.',
      actionKey: context.os?.dataFimExecucao ? 'review-close' : 'start-execution',
      blocking: false,
    };
  }

  if (!context.paid) {
    return {
      stage: 'financeiro',
      title: 'Encaminhar para o Financeiro',
      description: 'A OS está finalizada e ainda não há pagamento confirmado na projeção financeira carregada.',
      actionKey: 'open-finance',
      blocking: false,
    };
  }

  if (!context.os?.dataEntrega) {
    return {
      stage: 'entrega',
      title: 'Preparar entrega do veículo',
      description: 'A operação está finalizada e paga, mas a entrega ainda não foi registrada.',
      actionKey: 'prepare-delivery',
      blocking: false,
    };
  }

  return {
    stage: 'concluida',
    title: 'Atendimento concluído',
    description: 'A OS possui finalização, pagamento e entrega registrados.',
    actionKey: 'completed',
    blocking: false,
  };
}
