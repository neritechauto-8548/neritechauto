import type { Metadata } from 'next';
import Image from 'next/image';
import Link from 'next/link';
import { ArrowRight, BarChart3, ClipboardList, ShieldCheck } from 'lucide-react';
import { PricingCatalog } from '@/components/pricing-catalog';
import { getPublicPlans } from '@/lib/trial-api';
import { pageMetadata } from '@/lib/site';

export const metadata: Metadata = pageMetadata('Planos e preços', 'Compare os dois planos comerciais NERITECH AUTO e consulte os valores e recursos vigentes.', '/precos');
export const dynamic = 'force-dynamic';

export default async function Prices() {
  const plans = await getPublicPlans();
  return <main id="conteudo" className="nl-page nl-prices-page">
    <section className="nl-prices-hero"><Image src="/images/home/oficina-patio.png" width={1536} height={1024} priority alt="Oficina mecânica em atividade"/><div className="nl-container"><span className="nl-kicker">Planos e preços</span><h1>Escolha um plano ideal e <em>acelere a sua oficina.</em></h1><p>Dois planos NERITECH AUTO. Compare os recursos e as condições atuais antes de decidir.</p></div></section>
    <section className="nl-section nl-prices-catalog"><div className="nl-container"><PricingCatalog plans={plans}/></div></section>
    <section className="nl-section nl-prices-guidance"><div className="nl-container"><div className="nl-section-head nl-center"><span className="nl-kicker">Uma decisão com clareza</span><h2>O plano certo acompanha <em>a sua operação.</em></h2><p>Considere a equipe, o volume de serviços e os recursos habilitados para sua oficina.</p></div><div className="nl-prices-guidance-grid"><article><ClipboardList size={26}/><h3>Rotina da equipe</h3><p>Quantas pessoas usam o sistema e quantos atendimentos você acompanha?</p></article><article><BarChart3 size={26}/><h3>Visão de gestão</h3><p>Quais limites e dados financeiros são relevantes para o seu negócio?</p></article><article><ShieldCheck size={26}/><h3>Recursos habilitados</h3><p>API, fiscal e outras integrações dependem do catálogo e da configuração.</p></article></div></div></section>
    <section className="nl-prices-cta"><div className="nl-container"><div><span className="nl-kicker">Conheça na prática</span><h2>Pronto para organizar a próxima etapa?</h2><p>Confira as condições vigentes do teste antes de criar sua oficina.</p></div><Link className="nl-button nl-button-light" href="/teste-gratis">Conhecer teste grátis <ArrowRight size={17}/></Link></div></section>
  </main>;
}
