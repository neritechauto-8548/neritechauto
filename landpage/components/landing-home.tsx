import Image from 'next/image';
import Link from 'next/link';
import {
  ArrowRight, BarChart3, CalendarDays, Check, ClipboardCheck, ClipboardList,
  FileText, Package, ShieldCheck, Sparkles, Users, Wallet, Wrench,
  MessageCircle, Boxes, CircleCheck, Smartphone, Search
} from 'lucide-react';
import { FAQ } from '@/components/faq';
import { PatioBoard } from '@/components/marketing-visuals';
import { ProductPreview } from '@/components/product-preview';

const featureTiles = [
  { title: 'CONTROLE TOTAL DA OFICINA', text: 'Orçamentos, ordens de serviço, clientes e veículos em um mesmo fluxo de trabalho.', href: '/funcionalidades#gerencia', icon: ClipboardList, variant: 'large light', image: 'oficina' },
  { title: 'PRÁTICO E MODERNO', text: 'Uma rotina mais organizada, com acesso aos recursos conforme o perfil e o plano.', href: '/funcionalidades#servicos', icon: Smartphone, variant: 'large blue', image: 'moderno' },
  { title: 'ESTOQUE E COMPRAS', text: 'Produtos, peças, fornecedores, cotações e movimentações com mais clareza.', href: '/funcionalidades#estoque', icon: Package, variant: 'small' },
  { title: 'AGILIDADE NOS PROCESSOS', text: 'Acompanhe cada etapa do atendimento sem perder o contexto da OS.', href: '/funcionalidades#orcamentos', icon: Wrench, variant: 'small blue' },
  { title: 'EQUIPE MAIS PRODUTIVA', text: 'Responsáveis e andamento dos serviços reunidos na operação.', href: '/funcionalidades#patio', icon: Users, variant: 'small dark' },
  { title: 'GESTÃO COM MAIS CLAREZA', text: 'Custos, movimentações e indicadores conectados às decisões.', href: '/funcionalidades#financeiro', icon: BarChart3, variant: 'small', image: 'financeiro' },
];

const process = [
  { icon: CalendarDays, title: 'Organize os atendimentos', text: 'Agenda, veículo, cliente e orçamento na mesma rotina.', image: '/images/home/oficina-cliente.png' },
  { icon: ClipboardCheck, title: 'Acompanhe a execução', text: 'Etapas do serviço, responsáveis e prazos em uma visão organizada.', image: '/images/home/oficina-patio.png' },
  { icon: ShieldCheck, title: 'Entregue com confiança', text: 'Registre informações e mantenha o histórico ao alcance da equipe.', image: '/images/home/oficina-gestao.png' },
];

const editorials = [
  { key: '01', eyebrow: 'GESTÃO COMPLETA', title: 'TUDO DA SUA OFICINA EM UM SÓ LUGAR', text: 'Conecte atendimento, clientes, veículos, ordens de serviço e estoque. O que acontece na operação fica disponível para as próximas decisões.', tags: ['Clientes e veículos', 'Orçamentos e OS', 'Estoque'], href: '/funcionalidades#gerencia', mode: 'preview' },
  { key: '02', eyebrow: 'FINANCEIRO', title: 'MAIS CONTROLE PARA AS SUAS DECISÕES', text: 'Conecte serviços e movimentações financeiras para acompanhar o resultado da oficina com base nos dados registrados.', tags: ['Contas a pagar', 'Contas a receber', 'Indicadores'], href: '/funcionalidades#financeiro', mode: 'finance' },
  { key: '03', eyebrow: 'CLIENTES E CRM', title: 'SEU CLIENTE SEMPRE INFORMADO', text: 'Organize contatos, histórico e acompanhamento dos serviços. Os canais de comunicação dependem das integrações habilitadas.', tags: ['Histórico', 'Relacionamento', 'Comunicação'], href: '/funcionalidades#clientes', mode: 'chat' },
  { key: '04', eyebrow: 'PEÇAS E COMPRAS', title: 'MAIS ORGANIZAÇÃO DO ESTOQUE À EXECUÇÃO', text: 'Acompanhe produtos, kits, fornecedores e movimentações para organizar o trabalho sem perder o contexto de cada peça.', tags: ['Produtos', 'Compras', 'Movimentações'], href: '/funcionalidades#estoque', mode: 'stock' },
];

function Illustration({ mode }: {mode:string}) {
  if (mode === 'preview') return <div className="nr-preview-device"><ProductPreview/><span>Interface ilustrativa do sistema</span></div>;
  if (mode === 'finance') return <div className="nr-illustration nr-illustration-finance" aria-label="Representação ilustrativa de indicadores financeiros"><div className="nr-small-dash"><span>Visão financeira</span><div className="nr-bars">{[35,51,47,73,64,88,78].map((h,i)=><i key={i} style={{height:h+'%'}}/>)}</div><div className="nr-dash-footer"><span>Desempenho</span><BarChart3 size={24}/></div></div><div className="nr-floating-badge"><Wallet size={18}/> Decisões com contexto</div></div>;
  if (mode === 'chat') return <div className="nr-illustration nr-illustration-chat" aria-label="Exemplo ilustrativo de comunicação com clientes"><div className="nr-chat-bubble"><MessageCircle size={17}/> A ordem de serviço foi registrada.</div><div className="nr-chat-bubble nr-chat-reply"><CircleCheck size={17}/> Atendimento acompanhado.</div><div className="nr-chat-bubble"><MessageCircle size={17}/> Histórico disponível para consulta.</div><small>Exemplo ilustrativo • envios sujeitos a integração</small></div>;
  return <div className="nr-illustration nr-illustration-stock" aria-label="Representação de estoque e compras"><div className="nr-stock-header"><Boxes size={24}/> Estoque e compras</div><div className="nr-stock-rows">{['Produtos e kits','Cotações e fornecedores','Inventário e movimentações'].map(t=><div key={t}><Check size={17}/><span>{t}</span><i/></div>)}</div></div>;
}

export function LandingHome() {
  return <main id="conteudo" className="nl-page nr-home">
    <section className="nr-hero">
      <Image src="/images/home/hero-oficina-neritech.png" width={1672} height={940} priority alt="Profissional em uma oficina mecânica moderna" className="nr-hero-photo"/>
      <div className="nr-hero-overlay"/>
      <div className="nl-container nr-hero-inner"><div className="nr-hero-copy">
        <span className="nr-eyebrow">SISTEMA PARA OFICINAS MECÂNICAS</span>
        <h1>O FUTURO DA<br/>GESTÃO AUTOMOTIVA<br/><em>COMEÇA AQUI.</em></h1>
        <p>Mais controle para sua oficina, do primeiro atendimento à entrega do veículo. Uma gestão conectada, simples e inteligente.</p>
        <Link href="/teste-gratis" className="nr-btn nr-btn-primary">Conhecer teste grátis <ArrowRight size={17}/></Link>
        <div className="nr-hero-helper"><CircleCheck size={17}/> Da recepção à entrega, tudo no mesmo fluxo.</div>
      </div></div>
    </section>
    <section className="nr-metrics nl-container" aria-label="Etapas da jornada"><div><strong>01 · ATENDIMENTO</strong><span>Clientes, veículos e agenda</span></div><div><strong>02 · EXECUÇÃO</strong><span>Orçamentos, OS e gestão de pátio</span></div><div><strong>03 · RESULTADOS</strong><span>Estoque, financeiro e indicadores</span></div></section>
    <section className="nr-statement"><div className="nr-decor nr-decor-one"><Wrench/></div><div className="nr-decor nr-decor-two"><ClipboardCheck/></div><div className="nr-decor nr-decor-three"><BarChart3/></div><div className="nr-decor nr-decor-four"><Sparkles/></div><h2>A oficina evoluiu, seu sistema de gestão<br/>também precisa evoluir.</h2></section>

    <section className="nr-section nr-features nl-container">
      <div className="nr-center-heading"><span className="nr-eyebrow">POTENCIALIZE O SEU DIA A DIA</span><h2>Possuímos tudo o que você precisa para <em>potencializar sua oficina</em></h2></div>
      <div className="nr-feature-mosaic">{featureTiles.map(({title,text,href,icon:Icon,variant,image})=><Link href={href} className={'nr-feature-tile '+variant} key={title}><span className="nr-icon"><Icon size={21}/></span><div className="nr-feature-info"><h3>{title}</h3><p>{text}</p></div>{image==='oficina'&&<div className="nr-tile-screen"><ProductPreview/></div>}{image==='moderno'&&<Image src="/images/home/oficina-mecanico.png" alt="" width={460} height={300} className="nr-tile-photo"/>}{image==='financeiro'&&<BarChart3 size={80} className="nr-tile-watermark"/>}</Link>)}</div>
      <div className="nr-center-heading nr-secondary-heading"><h2>Acelere com segurança e agilidade,<br/><em>vá mais longe em menos tempo.</em></h2></div>
      <div className="nr-process-cards">{process.map(({icon:Icon,title,text,image})=><article className="nr-process-card" key={title}><Image src={image} width={640} height={500} alt="" className="nr-process-photo"/><div className="nr-process-gradient"/><span className="nr-process-icon"><Icon size={21}/></span><div><h3>{title}</h3><p>{text}</p></div></article>)}</div>
      <div className="nr-wide-promo"><Image src="/images/home/oficina-mecanico.png" width={1536} height={1024} alt="Profissional trabalhando em oficina mecânica"/><div className="nr-promo-card"><ClipboardCheck size={24}/><h2>Mais organização para uma oficina que não para.</h2><p>Uma visão completa do atendimento à entrega.</p><Link href="/teste-gratis" className="nr-btn nr-btn-white">Conhecer teste grátis <ArrowRight size={16}/></Link></div></div>
    </section>

    <section className="nr-editorials nl-container" aria-label="Recursos em destaque">
      {editorials.map((item,i)=><article key={item.key} className={'nr-editorial-row '+(i%2?'reverse':'')}>
        <div className="nr-editorial-copy"><span className="nr-eyebrow">{item.eyebrow}</span><h2>{item.title}</h2><p>{item.text}</p><div className="nr-tags">{item.tags.map(tag=><span key={tag}>{tag}</span>)}</div><Link href={item.href} className="nr-text-link">Saiba mais <ArrowRight size={16}/></Link></div>
        <Illustration mode={item.mode}/>
      </article>)}
    </section>

    <section className="nr-dark-showcase">
      <div className="nl-container">
        <div className="nr-center-heading nr-on-dark"><span className="nr-eyebrow">INTELIGÊNCIA ARTIFICIAL</span><h2>Encontre o que importa, <br/>sem perder tempo.</h2><p>O assistente, quando habilitado, pode ajudar a consultar informações disponíveis, sempre com revisão humana.</p></div>
        <div className="nr-ai-search"><div><Search size={19}/> Pesquise por OS, cliente, veículo ou informação da operação <span>Busca ilustrativa</span></div><div className="nr-ai-example"><Sparkles size={20}/> “Quais serviços precisam da minha atenção hoje?”</div></div>
        <div className="nr-patio-content"><div className="nr-patio-copy"><span className="nr-eyebrow">GESTÃO DE PÁTIO EM TEMPO REAL</span><h2>Controle o pátio da sua oficina<br/>da melhor maneira.</h2><p>Uma tela interna para visualizar etapas, responsáveis e ordens de serviço. Consulte também o Kanban em tela cheia.</p><Link href="/funcionalidades#patio" className="nr-text-link nr-link-light">Conhecer Gestão de Pátio <ArrowRight size={17}/></Link></div><div className="nr-patio-visual"><PatioBoard/></div></div>
        <div className="nr-checklist"><div><span className="nr-eyebrow">INSPEÇÕES E DIAGNÓSTICOS</span><h2>Checklist personalizado, feito para a rotina da oficina.</h2><p>Organize avaliações e registros de serviços, conforme as funcionalidades disponíveis.</p><ul><li><Check size={17}/> Registros vinculados ao veículo</li><li><Check size={17}/> Etapas de verificação</li><li><Check size={17}/> Histórico do atendimento</li></ul></div><div className="nr-checklist-visual"><ClipboardCheck size={54}/><strong>Inspeção digital</strong><span>Prévia ilustrativa</span></div></div>
      </div>
    </section>
    <section className="nr-section nr-integrations nl-container"><div className="nr-center-heading"><span className="nr-eyebrow">CONEXÕES COM CRITÉRIO</span><h2>Tudo integrado. Tudo sob controle.<br/><em>Mais resultados para você.</em></h2><p>Integrações disponíveis conforme plano e configuração técnica.</p></div><div className="nr-integration-cards">{[
      ['WhatsApp','Comunicação conforme provedor e credenciais.'],
      ['E-mail','Mensagens a partir de um serviço habilitado.'],
      ['SMS','Avisos disponíveis conforme provedor.'],
      ['API','Integrações conforme plano e autorização.'],
      ['Fiscal','Documentos sujeitos à configuração e habilitação.']
    ].map(([name,desc])=><article key={name}><div className="nr-partner-mark">{name}</div><p>{desc}</p><small>Consulte disponibilidade</small></article>)}</div><div className="nr-center-link"><Link href="/funcionalidades#integracoes" className="nr-text-link">Conheça as integrações <ArrowRight size={16}/></Link></div></section>
    <section className="nr-bottom-callout"><div className="nl-container nr-bottom-layout"><Image src="/images/home/oficina-cliente.png" width={1536} height={1024} alt="Atendimento em oficina mecânica"/><div><span className="nr-eyebrow">SUA OFICINA PODE EVOLUIR</span><h2>Mais clareza para a equipe. Mais controle para quem administra.</h2><p>Descubra o que a NERITECH AUTO pode organizar na sua operação.</p><Link href="/teste-gratis" className="nr-btn nr-btn-white">Conhecer teste grátis <ArrowRight size={17}/></Link></div></div></section>
    <FAQ/>
  </main>;
}
