import Image from 'next/image';
import Link from 'next/link';
import { ArrowRight, BarChart3, ClipboardList, FileText, Package, Users, Wallet, Wrench } from 'lucide-react';
import { FAQ } from '@/components/faq';
import { FeatureExplorer } from '@/components/feature-explorer';
import { PatioBoard } from '@/components/marketing-visuals';
import { ProductPreview } from '@/components/product-preview';

const features = [
  { title: 'Orçamentos e ordens de serviço', text: 'Da proposta à execução, mantenha serviços, itens e histórico no contexto do veículo.', href: 'orcamentos', icon: FileText, large: true },
  { title: 'Gestão de Pátio', text: 'Visualize etapas, responsáveis e o andamento das ordens de serviço em uma tela interna.', href: 'patio', icon: ClipboardList, large: true },
  { title: 'Clientes e veículos', text: 'Centralize cadastros e histórico de atendimentos.', href: 'clientes', icon: Users },
  { title: 'Estoque e compras', text: 'Acompanhe peças, produtos, fornecedores e movimentações.', href: 'estoque', icon: Package },
  { title: 'Financeiro', text: 'Conecte custos, contas e informações da operação.', href: 'financeiro', icon: Wallet },
  { title: 'Indicadores e relatórios', text: 'Consulte os dados disponíveis para decidir com mais clareza.', href: 'gerencia', icon: BarChart3 },
];
const journey = [
  { number: '01', title: 'Receba e organize', text: 'Agenda, cadastro de clientes, veículos e orçamentos.', image: '/images/home/oficina-cliente.png' },
  { number: '02', title: 'Execute e acompanhe', text: 'Ordens de serviço, equipe, etapas e pátio.', image: '/images/home/oficina-mecanico.png' },
  { number: '03', title: 'Finalize e controle', text: 'Entrega, histórico, estoque e financeiro.', image: '/images/home/oficina-gestao.png' },
];

function SectionIntro({ eyebrow, title, text }: { eyebrow: string; title: string; text?: string }) {
  return <div className="m-section-intro"><span className="m-eyebrow">{eyebrow}</span><h2>{title}</h2>{text && <p>{text}</p>}</div>;
}
export function FinalCTA() {
  return <section className="m-final-cta"><div className="m-container"><span className="m-eyebrow">Comece pela sua operação</span><h2>Sua oficina pode funcionar com mais organização e controle.</h2><p>Conheça a plataforma e confira as condições atuais do teste no cadastro.</p><Link className="m-button m-button-primary" href="/teste-gratis">Começar teste grátis <ArrowRight size={18} /></Link></div></section>;
}

export function MarketingHome() {
  return <main id="conteudo" className="m-page">
    <section className="m-hero"><Image src="/images/home/hero-oficina-neritech.png" alt="Profissional trabalhando em oficina mecânica" fill priority sizes="100vw" className="m-hero-photo" /><div className="m-hero-shade" /><div className="m-container m-hero-content"><span className="m-hero-badge">SISTEMA DE GESTÃO PARA OFICINAS MECÂNICAS</span><h1>Mais controle na oficina.<br /><span>Mais resultado no caixa.</span></h1><p>Organize atendimentos, orçamentos, ordens de serviço, estoque e financeiro em uma plataforma desenvolvida para a rotina da sua oficina.</p><div className="m-actions"><Link href="/teste-gratis" className="m-button m-button-primary">Começar teste grátis <ArrowRight size={18} /></Link><Link href="/funcionalidades" className="m-button m-button-light">Conhecer o sistema</Link></div></div></section>
    <section className="m-value m-container" aria-label="O que a plataforma reúne"><div><strong>Organização operacional</strong><p>Atendimentos, serviços, veículos e equipe.</p></div><div><strong>Controle financeiro</strong><p>Custos, receitas e informações para decisões.</p></div><div><strong>Visão integrada</strong><p>Um fluxo da recepção até a entrega do veículo.</p></div></section>
    <section className="m-section m-container" id="recursos"><SectionIntro eyebrow="O sistema" title="Uma operação conectada, do orçamento ao resultado." text="Recursos para acompanhar o trabalho da oficina sem perder o contexto de cada atendimento." /><div className="m-feature-grid">{features.map(({ title, text, href, icon: Icon, large }) => <Link href={`/funcionalidades#${href}`} className={`m-feature-card${large ? ' m-feature-card-large' : ''}`} key={title}><Icon size={24} strokeWidth={1.8} /><h3>{title}</h3><p>{text}</p><span>Explorar recurso <ArrowRight size={16} /></span></Link>)}</div></section>
    <section className="m-section m-section-soft"><div className="m-container"><SectionIntro eyebrow="Fluxo de trabalho" title="Da recepção à entrega, cada etapa no lugar certo." /><div className="m-journey">{journey.map(item => <article key={item.number}><div className="m-journey-image"><Image src={item.image} alt="" fill sizes="(max-width: 800px) 100vw, 33vw" /></div><div className="m-journey-copy"><span>{item.number}</span><h3>{item.title}</h3><p>{item.text}</p></div></article>)}</div></div></section>
    <section className="m-section m-container m-product"><div><SectionIntro eyebrow="Prévia da plataforma" title="Uma visão clara da rotina da oficina." text="Atendimento, execução, estoque e financeiro se relacionam dentro da operação." /><p className="m-illustration-note">Representação ilustrativa. Os dados exibidos não são registros de clientes.</p><Link href="/funcionalidades" className="m-text-link">Explorar funcionalidades <ArrowRight size={18} /></Link></div><ProductPreview /></section>
    <section className="m-section m-patio"><div className="m-container m-patio-grid"><div><span className="m-eyebrow">Gestão de Pátio</span><h2>O andamento das ordens de serviço à vista da equipe.</h2><p>Uma tela interna com Kanban, etapas de execução, responsáveis e recursos. O quadro também pode ser visualizado em tela cheia.</p><Link href="/funcionalidades#patio" className="m-text-link">Conhecer a Gestão de Pátio <ArrowRight size={18} /></Link></div><PatioBoard /></div></section>
    <section className="m-section m-container m-finance"><div className="m-photo"><Image src="/images/home/oficina-gestao.png" alt="Profissional consultando informações da oficina" fill sizes="(max-width: 800px) 100vw, 50vw" /></div><div><SectionIntro eyebrow="Controle financeiro" title="Mais contexto para entender os resultados." text="Acompanhe contas a pagar e receber, movimentações, custos e indicadores disponíveis. Relacione essas informações ao trabalho realizado na oficina." /><Link href="/funcionalidades#financeiro" className="m-text-link">Conhecer o financeiro <ArrowRight size={18} /></Link></div></section>
    <section className="m-section m-section-soft"><div className="m-container m-ai"><div><span className="m-eyebrow">Apoio à gestão</span><h2>Informações mais acessíveis. Decisões mais bem fundamentadas.</h2></div><p>Os recursos de inteligência artificial, quando disponíveis e habilitados para a oficina, podem apoiar consultas e análises. As permissões, o plano e a revisão humana continuam necessários.</p></div></section>
    <FAQ /><FinalCTA />
  </main>;
}

export function MarketingFeatures() {
  return <main id="conteudo" className="m-page"><section className="m-page-hero m-container"><span className="m-eyebrow">O sistema</span><h1>Uma plataforma para o fluxo real da sua oficina.</h1><p>Explore como atendimento, ordens de serviço, pátio, estoque e financeiro se conectam à rotina da equipe.</p></section><section className="m-container m-feature-explorer"><FeatureExplorer /></section><section className="m-section m-section-soft"><div className="m-container m-process"><SectionIntro eyebrow="Da entrada ao resultado" title="Informações que acompanham o serviço." /><div>{[['Atendimento', 'Cliente, veículo e orçamento no mesmo contexto.'], ['Execução', 'OS, responsáveis e etapas de pátio.'], ['Gestão', 'Peças, custos e informações financeiras.']].map(([title, text], index) => <article key={title}><span>0{index + 1}</span><h3>{title}</h3><p>{text}</p></article>)}</div></div></section><FinalCTA /></main>;
}

export function MarketingAbout() {
  return <main id="conteudo" className="m-page"><section className="m-about-hero"><div className="m-container"><span className="m-eyebrow">Nossa empresa</span><h1>Software pensado para quem vive a rotina da oficina.</h1><p>A NERITECH AUTO reúne processos e informações para apoiar atendimento, execução e gestão em um só ambiente.</p></div><Image src="/images/home/oficina-mecanico.png" alt="Profissional em oficina mecânica" fill priority sizes="100vw" /></section><section className="m-section m-container m-about-purpose"><SectionIntro eyebrow="Nosso propósito" title="Dar mais clareza ao trabalho de cada dia." /><div><p>Uma oficina precisa acompanhar veículos, pessoas, peças, prazos e custos. Nosso foco é organizar esses registros em um fluxo que ajude a equipe a atender melhor e o gestor a decidir com mais contexto.</p><p>Usabilidade, permissões e evolução responsável orientam a plataforma. Recursos e integrações dependem do plano e da configuração da oficina.</p></div></section><section className="m-section m-section-soft"><div className="m-container"><SectionIntro eyebrow="Princípios do produto" title="Tecnologia útil para a operação." /><div className="m-about-values"><article><Wrench size={25} /><h3>Rotina em primeiro lugar</h3><p>Fluxos de trabalho ligados ao que acontece na recepção, no serviço e na entrega.</p></article><article><ClipboardList size={25} /><h3>Informação organizada</h3><p>Histórico, etapas e responsáveis acessíveis aos perfis autorizados.</p></article><article><BarChart3 size={25} /><h3>Evolução com critério</h3><p>Melhorias voltadas à clareza, à usabilidade e ao controle da oficina.</p></article></div></div></section><FinalCTA /></main>;
}
