'use client';
import {useState} from 'react';
import Link from 'next/link';
import {Search,ShieldCheck,Info,ArrowRight} from 'lucide-react';
import {isValidCpf,normalizeCpf,dateBr,statusLabel} from '@/lib/format';
type Lookup={id:number;numeroOs:string;status:string;veiculo:string;placa?:string;dataEntrada?:string;previsaoEntrega?:string};
function maskCpf(value:string){const s=normalizeCpf(value);return s.replace(/^(\d{3})(\d)/,'$1.$2').replace(/^(\d{3})\.(\d{3})(\d)/,'$1.$2.$3').replace(/\.(\d{3})(\d)/,'.$1-$2');}
export function ConsultaForm(){
  const [cpf,setCpf]=useState(''),[numeroOs,setNumeroOs]=useState(''),[codigo,setCodigo]=useState('');
  const [loading,setLoading]=useState(false),[error,setError]=useState(''),[data,setData]=useState<Lookup|null>(null);
  async function submit(e:React.FormEvent<HTMLFormElement>){
    e.preventDefault();setError('');setData(null);
    if(!isValidCpf(cpf)){setError('Informe um CPF válido.');return;}
    if(!/^[\w-]{1,30}$/.test(numeroOs.trim())){setError('Informe o número válido da OS.');return;}
    if(!/^[\w-]{6,80}$/.test(codigo.trim())){setError('Informe o código seguro fornecido pela oficina.');return;}
    setLoading(true);
    try{
      const r=await fetch('/api/portal/consulta',{method:'POST',headers:{'Content-Type':'application/json'},cache:'no-store',body:JSON.stringify({cpf,numeroOs,codigoAcesso:codigo})});
      const json=await r.json();
      if(!r.ok){setError(json.message||'Não foi possível consultar sua OS.');return;}
      setData(json as Lookup);
    }catch{setError('Sem conexão com o serviço. Tente novamente.');}
    finally{setLoading(false);}
  }
  return <><div className="form-panel"><form onSubmit={submit} noValidate>
    <div className="field"><label htmlFor="cpf">CPF do responsável</label>
      <input id="cpf" type="text" inputMode="numeric" autoComplete="off" placeholder="000.000.000-00" maxLength={14} value={cpf} onChange={e=>setCpf(maskCpf(e.target.value))} required aria-describedby="cpf-hint"/>
      <small id="cpf-hint">Utilizado apenas para verificar a consulta autorizada.</small>
    </div>
    <div className="field"><label htmlFor="os">Número da ordem de serviço</label>
      <input id="os" value={numeroOs} onChange={e=>setNumeroOs(e.target.value)} maxLength={30} placeholder="Ex.: 00481" required/>
    </div>
    <div className="field"><label htmlFor="access">Código de acesso</label>
      <input id="access" value={codigo} onChange={e=>setCodigo(e.target.value)} maxLength={80} placeholder="Código enviado pela oficina" autoComplete="off" required/>
      <small>Solicite este código à oficina responsável pelo veículo.</small>
    </div>
    {error&&<p className="alert alert-error" role="alert"><Info size={18}/>{error}</p>}
    <button className="btn btn-primary full" disabled={loading} type="submit"><Search size={17}/>{loading?'Consultando…':'Consultar ordem de serviço'}</button>
  </form></div>
  <div className="auth-aux"><ShieldCheck size={18}/> Seus dados são verificados antes da consulta.</div>
  {data&&<section className="result-card" aria-live="polite">
    <div className="result-top"><p className="eyebrow">OS #{data.numeroOs}</p><span className="status-badge status-info">{statusLabel(data.status)}</span></div>
    <h2>{data.veiculo}</h2><p>{data.placa||'Placa não informada'} · Entrada: {dateBr(data.dataEntrada)}</p>
    <p>Previsão: {dateBr(data.previsaoEntrega)}</p>
    <Link href="/login" className="btn btn-outline full">Acessar detalhes com minha conta <ArrowRight size={17}/></Link>
  </section>}
  <p className="auth-bottom">Já tem uma conta? <Link href="/login">Entrar no portal</Link></p>
  </>;
}
