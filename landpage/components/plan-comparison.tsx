'use client';

import { Check, Minus } from 'lucide-react';
import type { PublicPlan } from '@/lib/trial-api';

type Row = { label: string; value: (plan: PublicPlan) => string | boolean | undefined };
const rows: Row[] = [
  { label: 'Usuários', value: plan => plan.maxUsuarios == null ? undefined : String(plan.maxUsuarios) },
  { label: 'Clientes', value: plan => plan.maxClientes == null ? undefined : String(plan.maxClientes) },
  { label: 'Veículos', value: plan => plan.maxVeiculos == null ? undefined : String(plan.maxVeiculos) },
  { label: 'OS por mês', value: plan => plan.maxOsMes == null ? undefined : String(plan.maxOsMes) },
  { label: 'Armazenamento', value: plan => plan.storageGb == null ? undefined : `${plan.storageGb} GB` },
  { label: 'Produtos', value: plan => plan.maxProdutos == null ? undefined : String(plan.maxProdutos) },
  { label: 'Fornecedores', value: plan => plan.maxFornecedores == null ? undefined : String(plan.maxFornecedores) },
  { label: 'App mobile', value: plan => plan.possuiAppMobile },
  { label: 'API', value: plan => plan.possuiApi },
  { label: 'Integração NF-e', value: plan => plan.possuiIntegracaoNfe },
  { label: 'Backup automático', value: plan => plan.possuiBackupAutomatico },
  { label: 'Suporte técnico', value: plan => plan.suporteTecnico || undefined },
  { label: 'Período de teste', value: plan => plan.periodoTesteDias == null ? undefined : `${plan.periodoTesteDias} dias` },
];
const number = (value: string) => /^\d+$/.test(value) ? new Intl.NumberFormat('pt-BR').format(Number(value)) : value;

export function PlanComparison({ plans }: { plans: PublicPlan[] | null }) {
  const byName = ['NeriTech Pro', 'NeriTech Ultra'].map(name => plans?.find(plan => plan.nome === name));

  return <div className="pricing-comparison-table-scroll" role="region" aria-label="Tabela de comparação de planos" tabIndex={0}>
    <table className="pricing-comparison-table">
      <caption className="sr-only">Comparação dos limites e recursos atuais dos planos NeriTech Pro e NeriTech Ultra</caption>
      <thead><tr><th scope="col">Recurso</th><th scope="col">NeriTech Pro</th><th scope="col">NeriTech Ultra</th></tr></thead>
      <tbody>{rows.map(row => <tr key={row.label}>
        <th scope="row">{row.label}</th>
        {byName.map((plan, index) => {
          const value = plan ? row.value(plan) : undefined;
          return <td key={index}>{value === true
            ? <><Check size={19} className="pricing-included" aria-hidden="true"/><span className="sr-only">Incluído</span></>
            : value === false
              ? <><Minus size={18} className="pricing-unavailable" aria-hidden="true"/><span className="sr-only">Não incluído</span></>
              : typeof value === 'string' ? number(value) : 'Consultar'}</td>;
        })}
      </tr>)}</tbody>
    </table>
  </div>;
}
