'use client';

import Link from 'next/link';
import { ArrowUpRight, Check } from 'lucide-react';
import { useState } from 'react';
import { PlanComparison } from '@/components/plan-comparison';
import type { PublicPlan } from '@/lib/trial-api';

const names = ['NeriTech Pro', 'NeriTech Ultra'] as const;
const descriptions = [
  'Para organizar e acompanhar a rotina da oficina.',
  'Para quem precisa de mais capacidade de gestão.',
];

const money = (value: number) =>
  new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
const quantity = (value: number) => new Intl.NumberFormat('pt-BR').format(value);
const isPrice = (value: number | null | undefined): value is number =>
  typeof value === 'number' && Number.isFinite(value) && value >= 0;

export function PricingCatalog({ plans }: { plans: PublicPlan[] | null }) {
  const [period, setPeriod] = useState<'monthly' | 'annual'>('monthly');
  const hasAnnual = names.every(name => {
    const plan = plans?.find(item => item.nome === name);
    return plan && isPrice(plan.precoAnual);
  });
  const available = names.every(name => plans?.some(item => item.nome === name));

  return <>
    {hasAnnual && <div className="pricing-period" role="group" aria-label="Período de cobrança">
      <button type="button" aria-pressed={period === 'monthly'} onClick={() => setPeriod('monthly')}>Mensal</button>
      <button type="button" aria-pressed={period === 'annual'} onClick={() => setPeriod('annual')}>Anual</button>
    </div>}
    <div className="pricing-grid pricing-grid-reference">
      {names.map((name, index) => {
        const plan = plans?.find(item => item.nome === name);
        const price = period === 'annual' ? plan?.precoAnual : plan?.precoMensal;
        const limits: Array<[string, number | null | undefined]> = plan ? [
          ['Usuários', plan.maxUsuarios],
          ['Clientes', plan.maxClientes],
          ['Veículos', plan.maxVeiculos],
          ['OS por mês', plan.maxOsMes],
          ['Produtos', plan.maxProdutos],
          ['Armazenamento (GB)', plan.storageGb],
        ] : [];
        const knownLimits = limits.filter((entry): entry is [string, number] => typeof entry[1] === 'number' && Number.isFinite(entry[1]));

        return <article className="card price-card" key={name}>
          <header className="plan-heading">
            <span className="plan-index">PLANO {String(index + 1).padStart(2, '0')}</span>
            <h3>{name}</h3>
            <p>{plan?.descricao || descriptions[index]}</p>
          </header>
          <div className="price-block">
            <span className="plan-price-caption">Valor do plano</span>
            <div className="plan-price">
              <strong>{isPrice(price) ? money(price) : 'Sob consulta'}</strong>
              {isPrice(price) && <span>/{period === 'annual' ? 'ano' : 'mês'}</span>}
            </div>
          </div>
          <div className="price-features">
            <h4>Limites e informações</h4>
            <ul className="feature-list">
              {knownLimits.length ? knownLimits.map(([label, value]) =>
                <li key={label}><Check size={17} aria-hidden="true"/><span>{label}: <strong>{quantity(value)}</strong></span></li>
              ) : <li><Check size={17} aria-hidden="true"/><span>Consulte os limites e condições atuais do plano.</span></li>}
            </ul>
          </div>
          <Link className="button plan-cta" href={plan ? `/teste-gratis?plano=${plan.id}` : '/teste-gratis'}>
            Teste grátis <ArrowUpRight size={17} aria-hidden="true"/>
          </Link>
        </article>;
      })}
    </div>
    {!available && <p className="plans-state" role="status">Os preços e limites não estão disponíveis no momento. Consulte as condições vigentes antes de contratar.</p>}
    <p className="pricing-disclaimer">Preços, limites e condições de teste dependem do catálogo comercial vigente. A disponibilidade dos recursos pode variar por plano.</p>
    <div className="pricing-compare">
      <div className="pricing-compare-heading">
        <h2>Compare os planos com clareza</h2>
        <p>Confira os limites e recursos informados para cada plano, lado a lado.</p>
      </div>
      <PlanComparison plans={plans} />
    </div>
  </>;
}
