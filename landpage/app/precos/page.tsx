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
      <h1 id="prices-title">ESCOLHA O PLANO IDEAL E<br /><span>ACELERE A SUA OFICINA</span></h1>
    </section>
    <section className="nl-prices-catalog pricing-main narrow-container" aria-label="Planos NERITECH AUTO">
      <PricingCatalog plans={plans} />
    </section>
  </main>;
}
