/* Site demonstrativo, sem backend. Navegação e interações criadas para estudo visual. */
(() => {
  const page = document.querySelector('.reference-site')?.dataset.page || 'home';
  const logo = '<span class="logo-mark" aria-hidden="true">♧</span><span class="logo-on">ON</span>MOTOR';
  const headerHost = document.getElementById('site-header');
  if (headerHost) {
    const nav = [
      ['Funcionalidades','/funcionalidades','funcionalidades'],
      ['Planos e preços','/precos','planos'],
      ['Quem somos','/sobre','quem-somos']
    ];
    headerHost.innerHTML = `<header class="site-header"><div class="container header-row"><a class="site-logo" href="/" aria-label="Onmotor - Página inicial">${logo}</a><nav class="nav-links" id="main-nav" aria-label="Navegação principal">${nav.map(([label,href,key])=>`<a href="${href}" class="${key===page?'active':''}">${label}</a>`).join('')}</nav><div class="header-actions"><a class="btn btn-orange" href="/teste-gratis">Teste grátis ↗</a><button class="lang" aria-label="Idioma Português">◎ <span>PT</span></button><button class="hamburger" id="nav-menu" aria-label="Abrir menu" aria-controls="main-nav" aria-expanded="false">☰</button></div></div></header>`;
    const trigger = document.getElementById('nav-menu');
    trigger?.addEventListener('click', () => {
      const open = document.getElementById('main-nav').classList.toggle('open');
      trigger.setAttribute('aria-expanded',String(open));
      trigger.setAttribute('aria-label',open?'Fechar menu':'Abrir menu');
      trigger.textContent = open ? '×' : '☰';
    });
  }
  const footerHost = document.getElementById('site-footer');
  if (footerHost && !footerHost.classList.contains('footer-hidden')) {
    footerHost.innerHTML = `<footer class="site-footer"><div class="container"><div class="footer-columns"><div><a class="site-logo" href="/">${logo}</a><div class="footer-follow">Acompanhe nas redes</div><div class="footer-socials"><a href="#" title="Instagram">◎</a><a href="#" title="YouTube">▶</a><a href="#" title="TikTok">♪</a><a href="#" title="Facebook">▣</a><a href="#" title="LinkedIn">▥</a></div></div><div><h4>Contato</h4><p><strong>E-mail</strong> suporte@onmotor.com.br</p><p><strong>Endereço</strong> Alameda Afonso Schmidt, 119 · Santa Teresinha · São Paulo (SP)</p><a class="footer-phone" href="tel:+551126497495">✆ &nbsp;(11) 2649-7495</a><a class="footer-phone" href="tel:+5511971569231">◉ &nbsp;(11) 97156-9231</a></div><div><h4>Acesso remoto</h4><a class="footer-link" href="#">▣ &nbsp;Baixe o nosso acesso remoto</a><h4 style="margin-top:21px">Baixe o app Onmotor</h4><div class="app-store-buttons"><a href="#">● App Store</a><a href="#">▶ Google Play</a></div></div></div><div class="footer-bottom"><nav><a href="/">Início</a><a href="/sobre">Quem Somos</a><a href="#">Blog</a><a href="#">Política de Privacidade do App Onmotor</a></nav><span>© 2026 Onmotor</span></div></div></footer>`;
  }
  /* Comparação / período de preço */
  const compare = document.getElementById('compare-modal');
  if (compare) {
    const close = () => { compare.hidden = true; document.body.classList.remove('modal-open'); };
    document.querySelectorAll('[data-open-compare]').forEach(b=>b.addEventListener('click',()=>{compare.hidden=false;document.body.classList.add('modal-open');compare.querySelector('[data-close-compare]')?.focus();}));
    document.querySelectorAll('[data-close-compare]').forEach(b=>b.addEventListener('click', close));
    compare.addEventListener('click',e=>{if(e.target===compare)close();});
    document.addEventListener('keydown',e=>{if(e.key==='Escape'&&!compare.hidden)close();});
    const sw = document.querySelector('.pricing-switch');
    sw?.addEventListener('click', e => {
      const b = e.target.closest('[data-period]');if(!b)return;
      sw.querySelectorAll('button').forEach(x=>x.classList.toggle('selected',x===b));
      const factor = b.dataset.period === 'mensal' ? 1 : b.dataset.period === 'semestral' ? .95 : .9;
      document.querySelectorAll('[data-monthly]').forEach(n => n.textContent = (Number(n.dataset.monthly)*factor).toLocaleString('pt-BR',{minimumFractionDigits:2,maximumFractionDigits:2}));
    });
  }
  /* Abas de funcionalidades */
  const tabs = [...document.querySelectorAll('.feature-tab')];
  if(tabs.length) {
    const content = {
      'Gerência':['Tenha uma visão geral do negócio, indicadores, gráficos e desempenho da oficina.',['Acompanhe faturamento, O.S. e indicadores.','Veja metas e desempenho dos colaboradores.','Filtre informações por período.','Tenha os números da oficina em um só lugar.']],
      'Serviços':['Acompanhe a rotina de serviços e a produtividade da sua equipe.',['Abertura rápida de ordens de serviço.','Histórico de serviços realizados.','Controle de etapas e execução.','Relatórios dos principais serviços.']],
      'Balcão':['Venda balcão com rapidez, indicadores, notas fiscais e vínculo com o app Garagem.',['Relatórios de faturamento de peças e ranking de vendas por período.','Localize clientes e acompanhe indicadores de vendas.','Visualize a venda e emita documentos com segurança.','Crie uma venda balcão com rapidez e preços atualizados.','Envie link de pagamento da venda para seu cliente.','Emita NF-e, NFS-e e NFC-e da venda balcão.','Controle informações e movimentações da nota fiscal.','Veja relatórios de comissão paga por vendas.']],
      'Clientes':['Gerencie cadastros e veículos com praticidade, organização e histórico.',['Cadastre clientes e veículos.','Acesse histórico de serviços.','Registre contatos e informações úteis.','Acompanhe o relacionamento com o cliente.']],
      'Financeiro':['Organize o fluxo financeiro e acompanhe recebimentos e pagamentos.',['Controle contas a pagar e receber.','Veja lançamentos financeiros.','Consulte gráficos e relatórios.','Acompanhe cobranças e pagamentos.']],
      'Fiscal':['Organize a emissão e consulta dos documentos fiscais.',['Emita NF-e, NFC-e e NFS-e.','Consulte documentos fiscais emitidos.','Gerencie configurações fiscais.','Acompanhe informações por período.']],
      'Estoque':['Tenha controle das peças, saldos, entradas e saídas de produtos.',['Cadastre produtos, peças e kits.','Controle entradas e saídas.','Acompanhe necessidade de reposição.','Emita relatórios de inventário.']],
      'Fornecedores':['Centralize compras e informações de fornecedores em um único lugar.',['Cadastre fornecedores.','Acompanhe cotações.','Registre compras e pedidos.','Consulte histórico de movimentações.']],
      'RH':['Acompanhe colaboradores, equipes e produtividade em tempo real.',['Cadastre seus colaboradores.','Acompanhe produtividade e apontamentos.','Organize permissões de acesso.','Consulte relatórios de equipe.']],
      'Integrações':['Potencialize a sua oficina integrando com nossos parceiros.',['Stone: novas opções de pagamento.','Fraga: catálogos de peças.','DiagWeb: informações para diagnóstico.','API: amplie as possibilidades de integração.','Mecanizou: fornecimento de peças.']]
    };
    const activeTab = (name) => {
      tabs.forEach(t=>{const on=t.dataset.feature===name;t.classList.toggle('active',on);t.setAttribute('aria-selected',String(on));t.setAttribute('role','tab');});
      document.getElementById('feature-explain').textContent=content[name][0];
      const notes=document.querySelector('.explorer-notes');
      notes.innerHTML=content[name][1].map((text,i)=>`<div class="explorer-note"><span aria-hidden="true">${['▥','⌕','◎','＋','$','▣','▤','♢'][i%8]}</span><p>${text}</p></div>`).join('');
      if(name==='Integrações')document.getElementById('feature-panel').querySelector('.explorer-image').innerHTML='<div class="integration-mini"><b>stone</b><b>◆ fraga</b><b>API</b><b>MECANIZOU</b><b>DIAGweb</b></div>';
      else document.getElementById('feature-panel').querySelector('.explorer-image').innerHTML='<img src="/images/home/oficina-gestao.png" alt="Prévia ilustrativa do sistema">';
    };
    tabs.forEach(t=>t.addEventListener('click',()=>activeTab(t.dataset.feature)));
    document.querySelectorAll('[data-tab-delta]').forEach(b=>b.addEventListener('click',()=>{
      const i=Math.max(0,tabs.findIndex(t=>t.classList.contains('active')));
      const j=(i+Number(b.dataset.tabDelta)+tabs.length)%tabs.length;activeTab(tabs[j].dataset.feature);tabs[j].scrollIntoView({block:'nearest',inline:'center',behavior:'smooth'});
    }));
    const search=document.getElementById('video-search');
    search?.addEventListener('input',()=>document.querySelectorAll('.video-item').forEach(x=>x.hidden=!x.textContent.toLowerCase().includes(search.value.toLowerCase())));
  }
  /* Formulário de demonstração */
  const register = document.getElementById('registration-form');
  if(register) {
    const formData = name => register.elements.namedItem(name);
    const err = (step, msg) => { document.getElementById('error-step-'+step).textContent = msg; };
    const show = step => {document.querySelectorAll('.signup-step').forEach(el=>el.hidden=String(step)!==el.dataset.step);document.querySelector('.signup-panel').scrollIntoView({block:'center'});};
    document.getElementById('next-registration')?.addEventListener('click',()=>{
      const names=['nome','email','empresa','telefone','celular'];
      for (const name of names) {
        const field=formData(name);
        if(!field.value.trim() || !field.checkValidity()){err(1,'Preencha corretamente todos os campos para continuar.');field.focus();return;}
      }
      err(1,'');show(2);
    });
    document.getElementById('back-registration')?.addEventListener('click',()=>show(1));
    document.getElementById('has-coupon')?.addEventListener('change',e=>document.querySelector('.coupon-box').hidden=!e.target.checked);
    const slug = formData('slug');
    slug?.addEventListener('input',()=>slug.value=slug.value.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g,'').replace(/[^a-z0-9-]/g,''));
    register.addEventListener('submit',e=>{
      e.preventDefault();
      const names=['slug','login','senha','senhaConfirmada'];
      for(const name of names) { const field=formData(name); if(!field.value.trim()||!field.checkValidity()){err(2,'Confira os campos obrigatórios e os dados informados.');field.focus();return;} }
      if(formData('senha').value!==formData('senhaConfirmada').value){err(2,'As senhas informadas não coincidem.');return;}
      if(!formData('terms').checked){err(2,'Para continuar, é necessário aceitar os termos.');return;}
      err(2,'');window.location.href='/bem-vindo';
    });
  }
  /* Validação visual do código SMS, sem serviço real */
  const pinInputs=[...document.querySelectorAll('.pin-inputs input')];
  if(pinInputs.length) {
    pinInputs.forEach((inp,i)=>{
      inp.addEventListener('input',()=>{inp.value=inp.value.replace(/\D/g,'').slice(-1);if(inp.value&&i<pinInputs.length-1)pinInputs[i+1].focus();});
      inp.addEventListener('keydown',e=>{if(e.key==='Backspace'&&!inp.value&&i>0)pinInputs[i-1].focus();});
      inp.addEventListener('paste',e=>{e.preventDefault();const str=e.clipboardData.getData('text').replace(/\D/g,'').slice(0,6);str.split('').forEach((c,j)=>pinInputs[j].value=c);pinInputs[Math.min(str.length,5)].focus();});
    });
    document.getElementById('verify-form')?.addEventListener('submit',e=>{
      e.preventDefault();document.getElementById('verify-error').textContent=pinInputs.every(n=>n.value)?'Demonstração visual: nenhuma verificação SMS foi realizada.':'Informe os 6 dígitos do código.';
    });
    const resend=document.getElementById('resend-sms');const caption=document.getElementById('resend-caption');let remaining=34;
    const tick=()=>{if(!resend || !caption)return;resend.disabled=remaining>0;caption.textContent=remaining>0?`Aguarde ${remaining} segundos antes de reenviar.`:'Reenvio disponível apenas na versão com backend.';remaining=Math.max(0,remaining-1);};
    tick();const timer=setInterval(()=>{tick();if(remaining===0)clearInterval(timer);},1000);
    resend?.addEventListener('click',()=>{document.getElementById('verify-error').textContent='Demonstração visual: o serviço de SMS não está conectado.';});
  }
  document.querySelectorAll('a[href="#"]').forEach(a=>a.addEventListener('click',e=>e.preventDefault()));
})();