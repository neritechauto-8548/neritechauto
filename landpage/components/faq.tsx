import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from '@/components/ui/accordion';
const items=[
  ['Como funciona o teste grátis?','O cadastro depende da disponibilidade do serviço. As condições do período de teste são exibidas antes do envio. Não pedimos cartão no formulário.'],
  ['A NERITECH AUTO funciona no celular?','A plataforma pode ser acessada pelo navegador em dispositivos compatíveis. Algumas atividades dependem de permissões e configuração.'],
  ['Posso acompanhar o pátio?','Sim. A Gestão de Pátio reúne, em uma tela interna, o andamento operacional dos veículos e das ordens de serviço.'],
  ['A IA toma decisões pela oficina?','Não. Recursos de assistência, quando habilitados, apoiam a análise. A revisão e a decisão permanecem com a equipe.'],
  ['Quais recursos estão em cada plano?','Consulte a comparação e os dados comerciais atuais na página de planos. A disponibilidade depende do plano e da configuração contratada.'],
];
export function FAQ(){return <section className="section section-soft" id="faq"><div className="wrap faq"><div className="section-heading center"><span className="eyebrow">Dúvidas comuns</span><h2>Perguntas frequentes</h2><p>Respostas claras para conhecer a plataforma.</p></div><Accordion type="single" collapsible>{items.map(([q,a],i)=><AccordionItem key={q} value={`item-${i}`}><AccordionTrigger>{q}</AccordionTrigger><AccordionContent>{a}</AccordionContent></AccordionItem>)}</Accordion></div></section>}
