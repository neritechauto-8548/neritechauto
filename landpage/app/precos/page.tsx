import { PricingCatalog } from '@/components/pricing-catalog';
import { getPublicPlans } from '@/lib/trial-api';
import { pageMetadata } from '@/lib/site';

export const metadata = pageMetadata(
  'Planos e preços',
  'Compare NeriTech Pro e NeriTech Ultra: preços, limites de usuários, clientes, veículos e ordens de serviço conforme o catálogo oficial.',
  '/precos',
);

export default async function Page() {
  const plans = await getPublicPlans();

  return <main id="conteudo" className="reference-site nl-page nl-prices-page" data-page="precos">
    <section className="pricing-intro" aria-labelledby="prices-title">
      <div className="pricing-intro-inner narrow-container">
        <p className="pricing-eyebrow">PLANOS NERITECH AUTO</p>
        <h1 id="prices-title">Planos para a rotina <span>da sua oficina.</span></h1>
        <p className="pricing-intro-subtitle">
          Orçamentos, ordens de serviço, estoque e gestão financeira fazem parte de uma operação bem organizada.
          Compare os recursos e os limites do NeriTech Pro e do NeriTech Ultra.
        </p>
      </div>
    </section>
    <section className="nl-prices-catalog pricing-main narrow-container" aria-labelledby="plans-title">
      <div className="pricing-section-head">
        <h2 id="plans-title">Escolha seu plano</h2>
        <p>Veja as condições informadas pelo catálogo e compare o que importa para a sua equipe.</p>
      </div>
      <PricingCatalog plans={plans} />
    </section>
  </main>;
}
