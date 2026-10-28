'use client';

import Link from 'next/link';
import { ArrowRight, Check, ChevronLeft, ChevronRight } from 'lucide-react';
import { useEffect, useState } from 'react';
import { modules } from '@/lib/modules';

const integrationCards = [
  { title: 'WhatsApp', text: 'Configuração de canal e webhooks para comunicação, conforme provedor e credenciais da oficina.', status: 'Requer configuração' },
  { title: 'E-mail', text: 'Configuração de envio para mensagens da operação, sujeita a serviço de e-mail habilitado.', status: 'Requer configuração' },
  { title: 'SMS', text: 'Configuração de canal de mensagens quando houver provedor disponível e contratado.', status: 'Sujeito a provedor' },
  { title: 'Fiscal', text: 'Parâmetros fiscais e emissão dependem da configuração e das condições técnicas aplicáveis.', status: 'Sujeito a habilitação' },
  { title: 'API', text: 'Acesso sujeito ao plano contratado, permissões e configuração técnica.', status: 'Conforme plano' },
];

export function FeatureExplorer() {
  const [active, setActive] = useState(0);
  useEffect(() => { const index = modules.findIndex(item => `#${item.id}` === window.location.hash); if (index >= 0) { setActive(index); document.getElementById('feature-explorer')?.scrollIntoView(); } }, []);
  const module = modules[active];
  const Icon = module.icon;
  const change = (step: number) => setActive((active + step + modules.length) % modules.length);

  return <div className="feature-explorer scroll-mt-28" id="feature-explorer">
    <div className="section-rule"><h2>Explorando as áreas do sistema</h2></div>
    <div className="feature-tabs" role="tablist" aria-label="Áreas da plataforma">
      <button type="button" className="tab-arrow" aria-label="Área anterior" onClick={() => change(-1)}><ChevronLeft size={18}/></button>
      <div className="feature-tab-list">{modules.map((item, index) => <button
        id={`module-tab-${item.id}`} key={item.id} type="button" role="tab"
        aria-selected={index === active} aria-controls="module-panel" tabIndex={index === active ? 0 : -1}
        className={`feature-tab ${index === active ? 'active' : ''}`}
        onClick={() => setActive(index)}
        onKeyDown={event => { if (event.key === 'ArrowRight') { event.preventDefault(); change(1); document.getElementById(`module-tab-${modules[(active+1)%modules.length].id}`)?.focus(); } else if (event.key === 'ArrowLeft') { event.preventDefault(); change(-1); document.getElementById(`module-tab-${modules[(active-1+modules.length)%modules.length].id}`)?.focus(); } }}
      >{item.title}</button>)}</div>
      <button type="button" className="tab-arrow" aria-label="Próxima área" onClick={() => change(1)}><ChevronRight size={18}/></button>
    </div>
    <div id="module-panel" role="tabpanel" aria-labelledby={`module-tab-${module.id}`} className={`feature-panel ${module.id === 'integracoes' ? 'feature-panel-integrations' : ''}`}>
      <div className="feature-panel-copy">
        <div className="feature-panel-label"><Icon size={20}/><span>{module.title}</span></div>
        <h3>{module.benefit}</h3>
        <div className="feature-fact-grid"><div><Check size={17}/><strong>O desafio</strong><p>{module.problem}</p></div><div><Check size={17}/><strong>Como funciona</strong><p>{module.operation}</p></div></div>
        {module.availability && <p className="note">{module.availability}</p>}
        <Link className="text-link" href="/teste-gratis">Conhecer teste grátis <ArrowRight size={16}/></Link>
      </div>
      {module.id === 'integracoes' ? <div className="integration-card-grid" aria-label="Canais configuráveis">{integrationCards.map(card => <article className="integration-card" key={card.title}><span className="integration-card-status">{card.status}</span><h4>{card.title}</h4><p>{card.text}</p></article>)}</div> : <div className="feature-panel-visual" aria-label={`Visual ilustrativo de ${module.title}`}>
        <div className="feature-window"><div className="feature-window-top"><span className="feature-window-mark">N</span><span>NERITECH AUTO</span><span className="feature-window-top-end">Prévia ilustrativa</span></div><div className="feature-window-content"><div className="feature-window-side"><span/><span/><span/><span/></div><div className="feature-window-main"><div className="feature-window-heading"><Icon size={22}/><div><strong>{module.title}</strong><small>Visão da operação</small></div></div><div className="feature-window-metrics"><div><b>01</b><span>Organizar</span></div><div><b>02</b><span>Acompanhar</span></div><div><b>03</b><span>Decidir</span></div></div><div className="feature-window-row"><span>{module.title}</span><i/></div><div className="feature-window-row"><span>Atendimento e histórico</span><i/></div><div className="feature-window-row"><span>Dados para gestão</span><i/></div></div></div></div>
      </div>}
    </div>
  </div>;
}
