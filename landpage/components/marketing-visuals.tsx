import { ArrowRight, CircleCheck, ClipboardList, FileText, Package, Wallet } from 'lucide-react';

export function PatioBoard() {
  const stages = [
    { title: 'Recepção', cards: ['Entrada registrada', 'Cliente e veículo'] },
    { title: 'Orçamento', cards: ['Itens organizados', 'Aguardando decisão'] },
    { title: 'Em serviço', cards: ['OS em andamento', 'Peças vinculadas'] },
    { title: 'Entrega', cards: ['Revisão final', 'Histórico atualizado'] },
  ];
  return <div className="patio-board" aria-label="Ilustração das etapas do pátio"><div className="patio-board-head"><span className="brand-mark">N</span><strong>Gestão de Pátio</strong><span className="pill">Prévia ilustrativa</span></div><div className="patio-board-columns">{stages.map((stage, index) => <div className="patio-board-column" key={stage.title}><h3><span>{String(index + 1).padStart(2, '0')}</span>{stage.title}</h3>{stage.cards.map(card => <div className="patio-board-card" key={card}><CircleCheck size={15}/>{card}</div>)}</div>)}</div></div>;
}

export function JourneyMap() {
  const steps = [
    { icon: FileText, title: 'Orçamento', detail: 'Proposta com itens claros' },
    { icon: ClipboardList, title: 'Ordem de serviço', detail: 'Trabalho acompanhado' },
    { icon: Package, title: 'Estoque', detail: 'Peças no contexto' },
    { icon: Wallet, title: 'Financeiro', detail: 'Custos e resultado' },
  ];
  return <div className="journey-map" aria-label="Fluxo ilustrativo da oficina">{steps.map(({ icon: Icon, title, detail }, index) => <div className="journey-map-step" key={title}><span><Icon size={23}/></span><strong>{title}</strong><small>{detail}</small>{index < steps.length - 1 && <ArrowRight className="journey-map-arrow" size={18}/>}</div>)}</div>;
}
