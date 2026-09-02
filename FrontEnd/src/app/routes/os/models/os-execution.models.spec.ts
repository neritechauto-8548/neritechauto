import {
  executionBlockerLabel,
  formatExecutionDuration,
} from './os-execution.models';

describe('OS execution contract helpers', () => {
  it('formata duracao autoritativa sem produzir tempo negativo', () => {
    expect(formatExecutionDuration(0)).toBe('00:00:00');
    expect(formatExecutionDuration(3661)).toBe('01:01:01');
    expect(formatExecutionDuration(-10)).toBe('00:00:00');
  });

  it('nao transforma ausência de duração em dado fictício', () => {
    expect(formatExecutionDuration(undefined)).toBe('00:00:00');
    expect(formatExecutionDuration(null)).toBe('00:00:00');
  });

  it('traduz bloqueios conhecidos e preserva códigos desconhecidos para diagnóstico', () => {
    expect(executionBlockerLabel('OS_SERVICE_NOT_AUTHORIZED'))
      .toBe('Aguardando autorização do serviço');
    expect(executionBlockerLabel('OS_TECHNICIAN_SESSION_ACTIVE'))
      .toBe('Você já possui outra sessão de execução aberta');
    expect(executionBlockerLabel('OS_NEW_BLOCKER')).toBe('OS_NEW_BLOCKER');
  });
});
