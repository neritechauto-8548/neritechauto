'use client';

import Link from 'next/link';
import { ArrowUpRight, Check } from 'lucide-react';
import { useState } from 'react';
import { PlanComparison } from '@/components/plan-comparison';
import type { PublicPlan } from '@/lib/trial-api';

const money = (value: number) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
const names = ['NeriTech Pro', 'NeriTech Ultra'];
const descriptions = ['Para organizar a operação e enxergar os dados essenciais.', 'Para oficinas que precisam de uma gestão mais ampla.'];

export function PricingCatalog({ plans }: { plans: PublicPlan[] | null }) {
  const [period, setPeriod] = useState<'monthly' | 'annual'>('monthly');
  const hasAnnual = plans?.length === 2 && plans.every(plan => typeof plan.precoAnual === 'number');
  return <>
    <div className="pricing-period" role="group" aria-label="Período de cobrança">
      <button type="button" aria-pressed={period === 'monthly'} onClick={() => setPeriod('monthly')}>Mensal</button>
      {hasAnnual && <button type="button" aria-pressed={period === 'annual'} onClick={() => setPeriod('annual')}>Anual</button>}
    </div>
    <div className="pricing-grid pricing-grid-reference">{names.map((name, index) => {
      const plan = plans?.find(item => item.nome === name);
      const price = period === 'annual' ? plan?.precoAnual : plan?.precoMensal;
      const limits = plan ? [
        ['Usuários', plan.maxUsuarios], ['Clientes', plan.maxClientes], ['Veículos', plan.maxVeiculos], ['OS por mês', plan.maxOsMes], ['Armazenamento em GB', plan.storageGb],
      ].filter((entry): entry is [string, number] => typeof entry[1] === 'number') : [];
      return <article className="card price-card" key={name}>
        <div><span className="eyebrow">Plano {index + 1}</span><h2>{name}</h2><p>{plan?.descricao || descriptions[index]}</p></div>
        <div className="price-block"><strong>{typeof price === 'number' ? money(price) : 'Sob consulta'}</strong>{typeof price === 'number' && <span>/{period === 'annual' ? 'ano' : 'mês'}</span>}</div>
        <div className="price-features"><h3>Informações do plano</h3><ul className="feature-list">{limits.length ? limits.map(([label, value]) => <li key={label}><Check size={18}/>{label}: {value}</li>) : <li><Check size={18}/>Condições e limites disponíveis mediante consulta.</li>}</ul></div>
        <Link className="button" href={plan ? `/teste-gratis?plano=${plan.id}` : '/teste-gratis'}>Conhecer teste grátis <ArrowUpRight size={17}/></Link>
      </article>;
    })}</div>
    {plans?.length !== 2 && <p className="plans-state" role="status">O catálogo comercial está indisponível agora. Confirme preços e recursos antes de contratar.</p>}
    <div className="pricing-compare"><div className="section-rule"><h2>Uma escolha com todas as informações</h2></div><PlanComparison plans={plans}/></div>
  </>;
}
