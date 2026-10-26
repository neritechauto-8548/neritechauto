/* Abas de funcionalidades. Fluxos de cadastro e precos usam React e backend reais. */
(() => {
  'use strict';
  const root=document.querySelector('.reference-site[data-page="funcionalidades"]');
  if(!root)return;
  const tabs=[...root.querySelectorAll('.feature-tab')];
  const desc=root.querySelector('#feature-explain');
  const notes=root.querySelector('.explorer-notes');
  const preview=root.querySelector('.explorer-image');
  const panel=root.querySelector('#feature-panel');
  if(!tabs.length||!desc||!notes||!preview||!panel)return;
  const copy={
    'Painéis':['Consulte painéis e indicadores da operação.',['Painéis por período','Orçamentos e OS','Indicadores operacionais']],
    'Ordens de Serviço':['Registre a execução dos serviços e acompanhe as ordens de serviço.',['Ordens de serviço','Etapas e responsáveis','Histórico de atendimento']],
    'Vendas':['Registre vendas de peças sem precisar abrir uma OS.',['Produtos e clientes','Movimentações de estoque','Histórico das vendas']],
    'Clientes e CRM':['Cadastros de clientes, veículos e histórico de relacionamento.',['Cadastros','Veículos','Histórico e comunicação']],
    'Financeiro':['Contas e lançamentos financeiros da operação.',['Contas a pagar','Contas a receber','Movimentações e indicadores']],
    'Fiscal':['Documentos fiscais conforme configuração e habilitação.',['Documentos fiscais','Informações fiscais','Emissão sujeita a provedor habilitado']],
    'Estoque e Compras':['Produtos, peças, fornecedores e compras organizados.',['Produtos e kits','Inventário','Entradas e saídas']],
    'Cotações':['Consulte cotações e planeje compras com fornecedores cadastrados.',['Fornecedores','Cotações','Compras']],
    'Equipe':['Organize usuários, funcionários, cargos e permissões.',['Usuários e funcionários','Cargos','Permissões']],
    'Integrações':['Recursos dependem do plano e dos provedores habilitados.',['Disponibilidade por plano','Configuração de integrações','API conforme permissão']]
  };
  function activate(tab,focus=false){
    const data=copy[tab.dataset.feature];if(!data)return;
    tabs.forEach((item,index)=>{const selected=item===tab;item.id='feature-tab-'+index;item.setAttribute('role','tab');item.setAttribute('aria-controls','feature-panel');item.setAttribute('aria-selected',String(selected));item.tabIndex=selected?0:-1;item.classList.toggle('active',selected)});
    panel.setAttribute('role','tabpanel');panel.setAttribute('aria-labelledby',tab.id);
    desc.textContent=data[0];
    notes.replaceChildren(...data[1].map(t=>{const div=document.createElement('div');div.className='explorer-note';const symbol=document.createElement('span');symbol.textContent='✓';symbol.setAttribute('aria-hidden','true');const p=document.createElement('p');p.textContent=t;div.append(symbol,p);return div}));
    if(tab.dataset.feature==='Integrações'){const element=document.createElement('div');element.className='integration-mini';element.textContent='Consulte os provedores e recursos disponíveis.';preview.replaceChildren(element)}
    else {const img=document.createElement('img');img.src='/images/home/oficina-gestao.png';img.alt='Representação ilustrativa da plataforma';preview.replaceChildren(img)}
    if(focus){tab.focus();tab.scrollIntoView({block:'nearest',inline:'center'})}
  }
  tabs.forEach(tab=>{
    tab.addEventListener('click',()=>activate(tab));
    tab.addEventListener('keydown',event=>{const index=tabs.indexOf(tab);let next=index;if(event.key==='ArrowRight')next=(index+1)%tabs.length;else if(event.key==='ArrowLeft')next=(index+tabs.length-1)%tabs.length;else if(event.key==='Home')next=0;else if(event.key==='End')next=tabs.length-1;else return;event.preventDefault();activate(tabs[next],true)})
  });
  root.querySelectorAll('[data-tab-delta]').forEach(button=>button.addEventListener('click',()=>{const index=Math.max(0,tabs.findIndex(tab=>tab.classList.contains('active')));const delta=Number(button.dataset.tabDelta)||1;activate(tabs[(index+delta+tabs.length)%tabs.length],true)}));
  activate(tabs.find(t=>t.classList.contains('active'))||tabs[0]);
})();
