import type { Metadata } from 'next';
import Image from 'next/image';
import Link from 'next/link';
import { ArrowRight, BarChart3, Eye, HeartHandshake, Layers3, Lightbulb, ShieldCheck, Users, Wrench, CheckCircle2 } from 'lucide-react';
import { pageMetadata } from '@/lib/site';

export const metadata: Metadata = pageMetadata('Quem somos', 'Conheça a proposta e os valores da NERITECH AUTO para oficinas mecânicas.', '/sobre');

const principles = [
  {icon:ShieldCheck,title:'Transparência',text:'Clareza e responsabilidade na apresentação de informações e processos.'},
  {icon:HeartHandshake,title:'Humanização',text:'Pessoas no centro das decisões dentro e fora da oficina.'},
  {icon:Lightbulb,title:'Evolução constante',text:'Melhoria contínua da experiência e dos fluxos de trabalho.'},
  {icon:Layers3,title:'Simplicidade',text:'A complexidade fica no sistema; a rotina continua compreensível.'},
  {icon:Users,title:'Foco no cliente',text:'Atendimento com contexto, histórico e continuidade.'},
  {icon:BarChart3,title:'Inovação com propósito',text:'Ferramentas que ajudam a transformar dados em decisões.'}
];

const milestones = [
 {title:'Entender a oficina',text:'Partimos da realidade do atendimento, do serviço e da gestão.',image:'/images/home/oficina-mecanico.png'},
 {title:'Conectar as áreas',text:'Clientes, veículos, OS, peças e financeiro compartilham contexto.',image:'/images/home/oficina-cliente.png'},
 {title:'Evoluir a operação',text:'Uma experiência que busca apoiar equipes e decisões melhores.',image:'/images/home/oficina-gestao.png'}
];

export default function About() {
  return <main id="conteudo" className="nl-page nl-about-page nr-about-page">
    <section className="nr-about-photo"><Image src="/images/home/oficina-gestao.png" width={1536} height={1024} priority alt="Profissional da área automotiva em ambiente de oficina"/></section>
    <section className="nl-container nr-about-history"><div className="nr-about-story-card"><span className="nr-eyebrow">NERITECH AUTO</span><h1>NOSSA PROPOSTA</h1><p>A NERITECH AUTO nasceu da ideia de tornar mais clara a rotina de quem vive o setor de reparação automotiva. Reunimos os processos da oficina em uma experiência organizada, que conecta atendimento, execução e resultado.</p><p>Nosso objetivo é diminuir a informação dispersa: clientes, veículos, orçamentos, ordens de serviço, peças e finanças precisam fazer parte do mesmo fluxo.</p><p>Queremos ajudar oficinas a trabalhar com mais previsibilidade, sem complicar aquilo que já é complexo por natureza.</p></div><div className="nr-about-timeline"><span className="nr-eyebrow">COMO PENSAMOS A EVOLUÇÃO</span>{milestones.map((item,i)=><div className="nr-about-timeline-row" key={item.title}><div className="nr-about-timeline-photo"><Image src={item.image} width={600} height={390} alt="" /></div><div className="nr-about-timeline-step"><b>{String(i+1).padStart(2,'0')}</b><strong>{item.title}</strong><small>{item.text}</small></div></div>)}</div></section>
    <section className="nl-container nr-about-mission"><article><Eye size={28}/><h2>A EMPRESA</h2><p>Uma plataforma de gestão para oficinas mecânicas, pensada para apoiar a produtividade e a organização das informações durante todo o atendimento.</p></article><article><Wrench size={28}/><h2>VISÃO</h2><p>Ser referência em tecnologia de gestão para o setor automotivo, com uma experiência acessível, confiável e preparada para evoluir.</p></article></section>
    <section className="nr-section nr-about-values-section nl-container"><div className="nr-center-heading"><h2>Principais valores que orientam a NERITECH AUTO</h2></div><div className="nr-about-value-grid">{principles.map(({icon:Icon,title,text})=><article key={title}><span className="nr-about-value-icon"><Icon size={18}/></span><h3>{title}</h3><p>{text}</p></article>)}</div></section>
    <section className="nr-about-principles nl-container"><div className="nr-center-heading"><h2>O que queremos entregar para sua oficina</h2><p>Compromissos de produto, não depoimentos ou resultados fabricados.</p></div><div className="nr-about-commitments"><article><CheckCircle2 size={25}/><h3>Mais organização</h3><p>Um caminho claro da chegada do veículo até a entrega.</p></article><article><CheckCircle2 size={25}/><h3>Mais continuidade</h3><p>Informações acessíveis conforme as permissões de cada profissional.</p></article><article><CheckCircle2 size={25}/><h3>Mais visibilidade</h3><p>Indicadores e dados disponíveis no contexto certo.</p></article></div></section>
    <section className="nr-about-bottom"><div className="nl-container"><div><span className="nr-eyebrow">CONHEÇA A PLATAFORMA</span><h2>A tecnologia precisa trabalhar a favor da oficina.</h2><p>Veja como atendimento, serviços, pátio, estoque e financeiro podem fazer parte de uma única operação.</p><Link href="/funcionalidades" className="nr-btn nr-btn-white">Explorar funcionalidades <ArrowRight size={17}/></Link></div><Image src="/images/home/oficina-cliente.png" width={1536} height={1024} alt="Atendimento em uma oficina mecânica"/></div></section>
  </main>;
}
