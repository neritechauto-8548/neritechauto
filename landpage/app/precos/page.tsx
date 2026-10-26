import { PricingCatalog } from '@/components/pricing-catalog';
import { getPublicPlans } from '@/lib/trial-api';
import { pageMetadata } from '@/lib/site';

export const metadata = pageMetadata(
  'Planos e preços',
  'Compare NeriTech Pro e NeriTech Ultra. Consulte preços e recursos atualizados diretamente no catálogo da NERITECH AUTO.',
  '/precos',
);

export default async function Page() {
  const plans = await getPublicPlans();
  return <main id="conteudo" className="reference-site nl-page nl-prices-page" data-page="precos">
    <section className="pricing-hero" aria-labelledby="prices-title">
      <div className="pricing-bg-left" aria-hidden="true" />
      <div className="pricing-bg-right" aria-hidden="true" />
      <div className="pricing-overlay" aria-hidden="true" />
      <div className="pricing-hero-copy">
        <h1 id="prices-title">DOIS PLANOS PARA A SUA OFICINA.<br /><span>ESCOLHA COMO QUER GERIR.</span></h1>
        <p>Orçamentos, controle da operação e informações financeiras. Compare os recursos e escolha o plano adequado à sua oficina.</p>
      </div>
    </section>
    <section className="nl-prices-catalog pricing-main narrow-container" aria-label="Planos NERITECH AUTO">
      <PricingCatalog plans={plans} />
    </section>
  </main>;
}
