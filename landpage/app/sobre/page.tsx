import type { Metadata } from 'next';
import Image from 'next/image';
import Link from 'next/link';
import { ArrowRight, BarChart3, Eye, HeartHandshake, Layers3, Lightbulb, ShieldCheck, Users } from 'lucide-react';
import { pageMetadata } from '@/lib/site';

export const metadata: Metadata = pageMetadata('Quem somos', 'Conheça a proposta, os valores e a direção da NERITECH AUTO para oficinas mecânicas.', '/sobre');

const principles = [
  { icon: ShieldCheck, title: 'Transparência', text: 'Informações claras em cada etapa do trabalho.' },
  { icon: HeartHandshake, title: 'Pessoas no centro', text: 'Tecnologia a serviço de quem faz a oficina acontecer.' },
  { icon: Layers3, title: 'Simplicidade', text: 'Fluxos compreensíveis para a equipe inteira.' },
  { icon: BarChart3, title: 'Gestão consciente', text: 'Dados vistos com contexto antes de decidir.' },
  { icon: Users, title: 'Foco no cliente', text: 'Histórico e continuidade para atender melhor.' },
  { icon: Lightbulb, title: 'Evolução contínua', text: 'Aprimoramento constante da experiência.' },
];

export default function About() {
  return <main id="conteudo" className="nl-page nl-about-page">
    <section className="nl-about-hero"><Image src="/images/home/oficina-gestao.png" width={1536} height={1024} priority alt="Profissional organizando o atendimento em uma oficina"/><div className="nl-container"><span className="nl-kicker">Quem somos</span><h1>Uma gestão melhor começa <em>por quem vive a oficina.</em></h1></div></section>
    <section className="nl-container nl-about-story"><article className="nl-about-story-card"><span className="nl-kicker">Nossa proposta</span><h2>Da rotina ao resultado.</h2><p>A NERITECH AUTO reúne atendimento, execução e gestão em uma plataforma para oficinas mecânicas. O objetivo é dar à equipe clareza sobre o próximo passo e à gestão mais contexto para decidir.</p><p>Construímos a experiência em torno da operação real: cliente, veículo, orçamento, serviço, peças e resultado fazem parte da mesma conversa.</p></article><div className="nl-about-journey"><span className="nl-kicker">Nossa direção</span><h2>Uma evolução que acompanha o trabalho.</h2><div className="nl-about-milestone"><span>01</span><div><strong>Entender a rotina</strong><p>Começar pelo que acontece na recepção, no pátio e na entrega.</p></div></div><div className="nl-about-milestone"><span>02</span><div><strong>Conectar as áreas</strong><p>Fazer a informação circular entre atendimento, serviços e gestão.</p></div></div><div className="nl-about-milestone"><span>03</span><div><strong>Melhorar a decisão</strong><p>Usar os dados disponíveis com responsabilidade e revisão humana.</p></div></div></div></section>
    <section className="nl-section nl-about-purpose"><div className="nl-container"><div className="nl-about-purpose-grid"><article><Eye size={27}/><span className="nl-kicker">Visão</span><h2>Uma oficina com mais clareza.</h2><p>Queremos que a tecnologia dê visibilidade ao trabalho e ajude pessoas a tomar decisões melhores.</p></article><article><HeartHandshake size={27}/><span className="nl-kicker">Missão</span><h2>Conectar operação e gestão.</h2><p>Organizamos processos e informação para apoiar o atendimento, a execução e a sustentabilidade do negócio.</p></article></div></div></section>
    <section className="nl-section"><div className="nl-container"><div className="nl-section-head nl-center"><span className="nl-kicker">O que nos orienta</span><h2>Valores que aparecem <em>na experiência.</em></h2><p>Princípios para construir uma plataforma útil e responsável.</p></div><div className="nl-about-values">{principles.map(({ icon: Icon, title, text }) => <article key={title}><Icon size={25}/><h3>{title}</h3><p>{text}</p></article>)}</div></div></section>
    <section className="nl-about-difference"><div className="nl-container"><div><span className="nl-kicker">Diferenciais</span><h2>Feito para conectar o que a oficina já precisa fazer.</h2><p>Orçamentos, OS, Gestão de Pátio, estoque e financeiro compartilham contexto. A assistência de IA, quando habilitada, apoia a análise sem tirar a decisão das pessoas.</p><Link href="/funcionalidades" className="nl-text-link">Explorar a plataforma <ArrowRight size={17}/></Link></div><Image src="/images/home/oficina-cliente.png" width={1536} height={1024} alt="Atendimento a uma cliente em oficina mecânica"/></div></section>
    <section className="nl-quote"><div className="nl-container nl-quote-grid"><div className="nl-quote-mark">“</div><div><span className="nl-kicker">Nosso compromisso</span><h2>Uma operação mais simples para quem trabalha. Uma visão mais clara para quem decide.</h2><p>Essa é a direção que guia a NERITECH AUTO.</p><Link href="/teste-gratis" className="nl-button nl-button-light">Conhecer teste grátis <ArrowRight size={17}/></Link></div></div></section>
  </main>;
}
