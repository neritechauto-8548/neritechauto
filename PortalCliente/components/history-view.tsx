'use client';
import {useEffect,useState} from 'react';
import Link from 'next/link';
import {CarFront,RefreshCw,Info} from 'lucide-react';
import {OrderCard} from './order-card';
import type {Order,HistoryResult} from '@/lib/types';
export function HistoryView(){
  const [data,setData]=useState<Order[]>([]),[loading,setLoading]=useState(true),[error,setError]=useState('');
  const [reload,setReload]=useState(0);
  useEffect(()=>{
    const ctrl=new AbortController();
    fetch('/api/portal/history',{cache:'no-store',signal:ctrl.signal}).then(async r=>{
      const json=await r.json();
      if(r.status===401){window.location.assign('/login?next=/historico');return;}
      if(!r.ok){setError(json.message||'Não foi possível carregar as ordens.');return;}
      setData((json as HistoryResult).content??[]);
      setError('');
    }).catch(e=>{if(e.name!=='AbortError')setError('Sem conexão com o servidor.');}).finally(()=>{if(!ctrl.signal.aborted)setLoading(false);});
    return ()=>ctrl.abort();
  },[reload]);
  return <main className="container portal-content">
    <div className="heading-bar"><div><p className="eyebrow">Minha oficina</p><h1>Histórico de atendimentos</h1>
      <p>Confira as ordens de serviço vinculadas à sua conta.</p></div>
      <button className="btn btn-outline" onClick={()=>{setLoading(true);setReload(x=>x+1)}} disabled={loading}><RefreshCw size={16}/> Atualizar</button>
    </div>
    {loading&&<div className="card-grid" role="status" aria-label="Carregando ordens"><div className="card-skeleton"/><div className="card-skeleton"/><div className="card-skeleton"/></div>}
    {!loading&&error&&<div className="panel notice"><Info size={25}/><h2>Não foi possível carregar seu histórico</h2><p>{error}</p>
      <button className="btn btn-outline" onClick={()=>{setLoading(true);setReload(x=>x+1)}}>Tentar novamente</button></div>}
    {!loading&&!error&&data.length===0&&<div className="panel notice"><CarFront size={35}/><h2>Nenhuma ordem encontrada</h2><p>As ordens de serviço autorizadas aparecerão aqui quando estiverem disponíveis.</p>
      <Link href="/consulta" className="btn btn-outline">Consultar uma OS</Link></div>}
    {!loading&&!error&&data.length>0&&<><div className="section-meta"><h2>Suas ordens de serviço</h2><span>{data.length} {data.length===1?'registro':'registros'}</span></div>
      <div className="card-grid">{data.map(o=><OrderCard key={o.id} order={o}/>)}</div></>}
  </main>;
}
