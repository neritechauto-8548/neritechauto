import type { Metadata } from 'next';
import Image from 'next/image';
import Link from 'next/link';
import { ArrowRight, BookOpen, Headset, ShieldCheck } from 'lucide-react';
import { FeatureExplorer } from '@/components/feature-explorer';
import { PatioBoard } from '@/components/marketing-visuals';
import { ProductPreview } from '@/components/product-preview';
import { posts } from '@/lib/posts';
import { pageMetadata } from '@/lib/site';

export const metadata: Metadata = pageMetadata('Funcionalidades', 'Explore as áreas da NERITECH AUTO, do atendimento ao financeiro, em uma plataforma para oficinas mecânicas.', '/funcionalidades');

export default function Features() {
  return <main id="conteudo" className="nl-page nl-features-page">
    <section className="nl-features-hero"><div className="nl-container nl-features-hero-grid"><div className="nl-features-hero-media"><ProductPreview/><span>Prévia ilustrativa</span></div><div><span className="nl-kicker">Conheça a plataforma</span><h1>Sua oficina com <em>mais potência.</em></h1><p>Da entrada do veículo à entrega, organize informações, acompanhe o trabalho e encontre clareza para crescer.</p><Link href="/teste-gratis" className="nl-button">Conhecer teste grátis <ArrowRight size={17}/></Link></div></div></section>
    <section className="nl-container nl-features-intro"><span className="nl-kicker">NERITECH AUTO</span><h2>Uma plataforma construída para o ritmo da oficina.</h2><p>Atendimento, serviços, pátio, estoque e financeiro compartilham contexto. Assim, a equipe encontra o próximo passo e a gestão consegue acompanhar custos, margens e resultados.</p><p>A disponibilidade de cada recurso depende do plano, das permissões e da configuração técnica. Recursos de IA apoiam análises com revisão humana quando estiverem habilitados.</p></section>
    <section className="nl-section nl-features-explorer"><div className="nl-container"><FeatureExplorer/></div></section>
    <section className="nl-section"><div className="nl-container"><div className="nl-section-head"><span className="nl-kicker">Operação em foco</span><h2>Gestão de Pátio em <em>uma única tela interna.</em></h2><p>Da recepção à entrega, visualize as etapas e os responsáveis com a OS no mesmo contexto.</p></div><div className="nl-features-patio"><PatioBoard/><div><h3>Mais clareza para a equipe inteira.</h3><p>O quadro mostra uma representação da organização operacional. Os dados reais são acessados somente dentro da aplicação, conforme permissões.</p><Link href="/teste-gratis" className="nl-text-link">Conhecer teste grátis <ArrowRight size={16}/></Link></div></div></div></section>
    <section className="nl-section nl-features-support"><div className="nl-container nl-features-support-grid"><article><Headset size={27}/><span className="nl-kicker">Apoio à sua rotina</span><h2>Uma operação que não depende da memória.</h2><p>Dados organizados ajudam a dar continuidade ao atendimento entre pessoas e turnos.</p></article><article><BookOpen size={27}/><span className="nl-kicker">Aprenda a evoluir</span><h2>Informação útil para decisões melhores.</h2><p>Explore as áreas da plataforma e entenda onde cada recurso pode apoiar a equipe.</p><Link href="/sobre" className="nl-text-link">Conheça nossa proposta <ArrowRight size={16}/></Link></article></div></section>
    <section className="nl-section"><div className="nl-container"><div className="nl-section-head"><span className="nl-kicker">Conteúdo para oficinas</span><h2>Ideias para aplicar <em>no dia a dia.</em></h2></div><div className="nl-features-articles">{posts.slice(0,3).map(post => <Link key={post.id} href={`/blog/${post.id}`}><Image src={post.image} width={900} height={520} alt=""/><div><span>{post.category}</span><h3>{post.title}</h3><strong>Ler artigo <ArrowRight size={15}/></strong></div></Link>)}</div></div></section>
    <section className="nl-features-banner"><div className="nl-container"><ShieldCheck size={30}/><div><span className="nl-kicker">Próximo passo</span><h2>Veja o que a NERITECH AUTO pode fazer pela sua oficina.</h2><p>Consulte as condições vigentes e comece com os recursos disponíveis para seu plano.</p></div><Link href="/teste-gratis" className="nl-button nl-button-light">Conhecer teste grátis <ArrowRight size={17}/></Link></div></section>
  </main>;
}
