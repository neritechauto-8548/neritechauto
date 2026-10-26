'use client';

import { useRef } from 'react';
import { Check, Minus, X } from 'lucide-react';
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

export function PlanComparison({ plans }: { plans: PublicPlan[] | null }) {
  const dialog = useRef<HTMLDialogElement>(null);
  const byName = ['NeriTech Pro', 'NeriTech Ultra'].map(name => plans?.find(plan => plan.nome === name));
  return <>
    <button type="button" className="button button-outline" onClick={() => dialog.current?.showModal()}>Comparar planos em detalhe</button>
    <dialog ref={dialog} className="compare-dialog" aria-labelledby="compare-title" onClick={event => { if (event.target === dialog.current) dialog.current?.close(); }}>
      <div className="compare-dialog-head"><h2 id="compare-title">Compare os planos</h2><button type="button" aria-label="Fechar comparação" onClick={() => dialog.current?.close()}><X size={22}/></button></div>
      <div className="compare-dialog-scroll"><table className="comparison"><caption className="sr-only">Limites e recursos do catálogo comercial atual</caption><thead><tr><th scope="col">Recurso</th><th scope="col">NeriTech Pro</th><th scope="col">NeriTech Ultra</th></tr></thead><tbody>{rows.map(row => <tr key={row.label}><th scope="row">{row.label}</th>{byName.map((plan, index) => { const value = plan ? row.value(plan) : undefined; return <td key={index}>{value === true ? <><Check size={20} className="inline text-[#18328a]"/><span className="sr-only">Incluído</span></> : value === false ? <><Minus size={18} className="inline text-slate-400"/><span className="sr-only">Não incluído</span></> : value ?? 'Consultar'}</td>; })}</tr>)}</tbody></table></div>
      <div className="compare-dialog-foot"><small>Dados sujeitos ao catálogo e à contratação vigentes.</small><button type="button" className="button button-outline button-sm" onClick={() => dialog.current?.close()}>Fechar</button></div>
    </dialog>
  </>;
}
