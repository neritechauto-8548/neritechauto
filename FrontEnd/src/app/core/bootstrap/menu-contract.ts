import { Menu, MenuChildrenItem } from './menu.service';

/**
 * UI-MASTER-001 — contrato de navegação global.
 *
 * A autorização continua sendo definida pelas permissões retornadas pelo backend.
 * Este contrato governa somente nomes/ordem/agrupamento visual da raiz oficial e
 * do grupo Movimentação, evitando que payloads legados ou futuros alterem o
 * Application Shell sem revisão documental.
 */
export const CANONICAL_ROOT_MENU_ORDER = [
  'gestao-patio',
  'home',
  'clientes',
  'operacional',
  'cadastros',
  'movimentacao',
  'financeiro',
  'fiscal',
  'historico',
  'graficos',
  'agendamentos',
  'relatorios',
] as const;

export const CANONICAL_MOVIMENTACAO_ORDER = [
  'orcamentos',
  'ordens-servico',
  'checklists',
  'aprovacoes',
  'pecas',
  'faturamento',
] as const;

const ROOT_INDEX = new Map<string, number>(
  CANONICAL_ROOT_MENU_ORDER.map((key, index) => [key, index])
);

const MOVIMENTACAO_INDEX = new Map<string, number>(
  CANONICAL_MOVIMENTACAO_ORDER.map((key, index) => [key, index])
);

export function applyCanonicalMenuContract(menu: Menu[]): Menu[] {
  return menu
    .map(item => cloneMenu(item))
    // A UI Master proíbe novos módulos diretamente na raiz sem revisão.
    .filter(item => ROOT_INDEX.has(menuKey(item.name)))
    .map(item => {
      if (menuKey(item.name) !== 'movimentacao') {
        return item;
      }

      return {
        ...item,
        children: normalizeMovimentacao(item.children || []),
      };
    })
    .sort((a, b) => indexOf(ROOT_INDEX, a.name) - indexOf(ROOT_INDEX, b.name));
}

function normalizeMovimentacao(children: MenuChildrenItem[]): MenuChildrenItem[] {
  return children
    .map(child => cloneMenu(child))
    .filter(child => MOVIMENTACAO_INDEX.has(menuKey(child.name)))
    .sort(
      (a, b) =>
        indexOf(MOVIMENTACAO_INDEX, a.name) - indexOf(MOVIMENTACAO_INDEX, b.name)
    );
}

function cloneMenu<T extends MenuChildrenItem>(item: T): T {
  return {
    ...item,
    children: item.children?.map(child => cloneMenu(child)),
  } as T;
}

function indexOf(index: Map<string, number>, name: string): number {
  return index.get(menuKey(name)) ?? Number.MAX_SAFE_INTEGER;
}

function menuKey(value: string): string {
  return String(value || '')
    .replace(/^menu\./, '')
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .trim()
    .toLowerCase()
    .replace(/\s+/g, '-')
    .replace(/_/g, '-');
}
