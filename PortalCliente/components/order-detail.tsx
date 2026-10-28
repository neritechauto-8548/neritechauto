'use client';
import {useEffect,useState} from 'react';
import Link from 'next/link';
import {useRouter} from 'next/navigation';
import {ArrowLeft,CarFront,CalendarDays,CheckCircle2,ClipboardList,Info,Package,Printer,Wrench,ShieldCheck} from 'lucide-react';
import {money,dateBr,statusLabel} from '@/lib/format';
import {StatusBadge} from './status-badge';
import type {Order} from '@/lib/types';

export function OrderDetail({id}:{id:string}){
  const router=useRouter();
  const [order,setOrder]=useState<Order|null>(null),[error,setError]=useState(''),[loading,setLoading]=useState(true);
  const [dialog,setDialog]=useState<'APROVAR'|'REVISAR'|null>(null),[motivo,setMotivo]=useState(''),[actionLoading,setActionLoading]=useState(false),[actionError,setActionError]=useState('');
  const [version,setVersion]=useState(0);
  useEffect(()=>{
    const c=new AbortController();
    fetch('/api/portal/os/'+encodeURIComponent(id),{cache:'no-store',signal:c.signal}).then(async r=>{
      const data=await r.json();
      if(r.status===401){window.location.assign('/login?next=/os/'+encodeURIComponent(id));return;}
      if(!r.ok){setError(data.message||'Não foi possível abrir esta OS.');return;}
      setOrder(data as Order);setError('');
    }).catch(e=>{if(e.name!=='AbortError')setError('Não foi possível conectar com a oficina.');})
      .finally(()=>{if(!c.signal.aborted)setLoading(false);});
    return ()=>c.abort();
  },[id,version]);
  async function decide(){
    if(!dialog)return;
    if(dialog==='REVISAR'&&(motivo.trim().length<5||motivo.length>1000)){setActionError('Descreva o motivo (5 a 1000 caracteres).');return;}
    setActionLoading(true);setActionError('');
    try{
      const r=await fetch('/api/portal/os/'+encodeURIComponent(id)+'/decision',{
        method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({decisao:dialog,motivo}),cache:'no-store'});
      const data=await r.json();
      if(r.status===401){router.replace('/login?next=/os/'+id);router.refresh();return;}
      if(!r.ok){setActionError(data.message||'A decisão não pôde ser registrada.');return;}
      setDialog(null);setMotivo('');setLoading(true);setVersion(v=>v+1);
    }catch{setActionError('Conexão indisponível. Nenhuma decisão foi confirmada.');}
    finally{setActionLoading(false);}
  }
  return <main className="container portal-content">
    <div className="detail-actions"><Link className="back-link" href="/historico"><ArrowLeft size={16}/> Voltar ao histórico</Link>
      <button className="btn btn-quiet" type="button" onClick={()=>window.print()}><Printer size={17}/> Imprimir</button></div>
    {loading&&<div className="panel notice" role="status">Carregando detalhes da ordem de serviço…</div>}
    {!loading&&error&&<div className="panel notice"><Info size={26}/><h1>Não foi possível carregar a OS</h1><p>{error}</p>
      <button className="btn btn-outline" onClick={()=>{setLoading(true);setVersion(v=>v+1)}}>Tentar novamente</button></div>}
    {!loading&&!error&&order&&<>
      <div className="heading-bar detail-heading"><div><p className="eyebrow">Ordem de serviço</p><h1>OS #{order.numeroOs}</h1>
        <p><CalendarDays size={16}/> Entrada em {dateBr(order.dataEntrada)}</p></div>
        <StatusBadge status={order.status}/></div>
      {order.podeAprovar&&<section className="approval-banner"><div className="approval-symbol"><ClipboardList size={24}/></div>
        <div className="approval-copy"><h2>Orçamento disponível para sua decisão</h2>
          <p>Confira os itens e valores antes de aprovar. Sua escolha ficará registrada pela oficina.</p></div>
        <div className="approval-actions"><button className="btn btn-outline" onClick={()=>{setDialog('REVISAR');setActionError('')}}>Solicitar revisão</button>
          <button className="btn btn-primary" onClick={()=>{setDialog('APROVAR');setActionError('')}}>Revisar e aprovar</button></div></section>}
      <div className="detail-grid">
        <div className="detail-primary">
          <section className="panel detail-panel"><div className="panel-heading"><CarFront size={19}/><h2>Dados do veículo</h2></div>
            <dl className="details-dl"><div><dt>Veículo</dt><dd>{order.veiculo}</dd></div>
              <div><dt>Placa</dt><dd>{order.placa||'Não informada'}</dd></div>
              <div><dt>Quilometragem</dt><dd>{order.km?new Intl.NumberFormat('pt-BR').format(order.km)+' km':'Não informada'}</dd></div>
              <div><dt>Previsão de entrega</dt><dd>{dateBr(order.previsaoEntrega)}</dd></div></dl>
            {order.observacoes&&<div className="notes"><h3>Observações</h3><p>{order.observacoes}</p></div>}
          </section>
          <section className="panel detail-panel"><div className="panel-heading"><Wrench size={19}/><h2>Serviços</h2></div>
            {order.servicos.length===0?<p className="empty-list">Nenhum serviço detalhado foi disponibilizado.</p>:
              <ul className="item-list">{order.servicos.map((item,i)=><li key={i}><span>{item.descricao}</span><strong>{money(item.valorTotal??item.valorUnitario)}</strong></li>)}</ul>}
          </section>
          <section className="panel detail-panel"><div className="panel-heading"><Package size={19}/><h2>Peças e materiais</h2></div>
            {order.pecas.length===0?<p className="empty-list">Nenhuma peça detalhada foi disponibilizada.</p>:
              <ul className="item-list">{order.pecas.map((item,i)=><li key={i}><span>{item.descricao}{item.quantidade?' · '+item.quantidade+' un.':''}</span><strong>{money(item.valorTotal??item.valorUnitario)}</strong></li>)}</ul>}
          </section>
        </div>
        <aside className="detail-aside">
          <section className="panel detail-panel"><div className="panel-heading"><ClipboardList size={19}/><h2>Resumo dos valores</h2></div>
            <dl className="money-list"><div><dt>Serviços</dt><dd>{money(order.valorServicos)}</dd></div>
              <div><dt>Peças</dt><dd>{money(order.valorPecas)}</dd></div>
              {typeof order.valorDesconto==='number'&&order.valorDesconto>0&&<div><dt>Descontos</dt><dd>− {money(order.valorDesconto)}</dd></div>}
              <div className="total"><dt>Total</dt><dd>{money(order.valorTotal)}</dd></div></dl>
          </section>
          <section className="panel detail-panel"><div className="panel-heading"><CheckCircle2 size={19}/><h2>Andamento da OS</h2></div>
            {order.historicoStatus.length?<ol className="timeline">{order.historicoStatus.map((e,i)=><li key={i}>
              <strong>{e.descricao||statusLabel(e.status)}</strong><span>{dateBr(e.data)}</span></li>)}</ol>:
              <p className="empty-list">Situação atual: {statusLabel(order.status)}. Não há outras etapas publicadas.</p>}
          </section>
          <p className="privacy-note"><ShieldCheck size={17}/> Informações restritas à sua conta. Entre em contato com a oficina para mais detalhes.</p>
        </aside>
      </div>
    </>}
    {dialog&&<div className="modal-overlay" role="presentation"><div role="dialog" aria-modal="true" aria-labelledby="decision-title" className="modal">
      <div className="modal-head"><h2 id="decision-title">{dialog==='APROVAR'?'Confirmar aprovação do orçamento':'Solicitar revisão do orçamento'}</h2></div>
      <p>{dialog==='APROVAR'?'Confira os valores apresentados. Ao confirmar, a decisão será enviada para a oficina.':'Descreva o que deseja alterar antes de encaminhar sua solicitação.'}</p>
      {dialog==='REVISAR'&&<div className="field"><label htmlFor="reason">Motivo da revisão</label>
        <textarea id="reason" value={motivo} onChange={e=>setMotivo(e.target.value)} maxLength={1000} rows={4} placeholder="Explique o que deseja revisar." /></div>}
      {actionError&&<p className="alert alert-error" role="alert"><Info size={17}/>{actionError}</p>}
      <div className="modal-actions"><button className="btn btn-outline" type="button" disabled={actionLoading} onClick={()=>setDialog(null)}>Cancelar</button>
        <button className="btn btn-primary" type="button" disabled={actionLoading} onClick={decide}>{actionLoading?'Enviando…':dialog==='APROVAR'?'Confirmar aprovação':'Enviar revisão'}</button></div>
    </div></div>}
  </main>;
}
