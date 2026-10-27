'use client';

import { useEffect, useRef, useState, type FormEvent } from 'react';
import { ArrowLeft, ArrowRight, Check, Eye, EyeOff } from 'lucide-react';
import type { PublicPlan, TrialStatus } from '@/lib/trial-api';
import { loginUrl } from '@/lib/site';

const trialApi = '/api/trial';
type Form = { nomeCompleto:string; email:string; nomeEmpresa:string; telefone:string; celular:string; cnpjOuCpf:string; segmento:string; senha:string; confirmarSenha:string; planoId:string; aceiteLegal:boolean };
type FieldName = keyof Form;
const initial:Form = {nomeCompleto:'',email:'',nomeEmpresa:'',telefone:'',celular:'',cnpjOuCpf:'',segmento:'oficina',senha:'',confirmarSenha:'',planoId:'',aceiteLegal:false};
const digits = (value:string) => value.replace(/\D/g,'');
const formatPhone = (value:string) => {const d=digits(value).slice(0,11);if(d.length<=2)return d;if(d.length<=6)return `(${d.slice(0,2)}) ${d.slice(2)}`;const split=d.length===11?7:6;return `(${d.slice(0,2)}) ${d.slice(2,split)}-${d.slice(split)}`};
const validDocument = (value:string) => {const d=digits(value);if(![11,14].includes(d.length)||/^(\d)\1+$/.test(d))return false;const calc=(length:number,weights:number[])=>{const sum=d.slice(0,length).split('').reduce((total,n,i)=>total+Number(n)*weights[i],0);const rest=sum%11;return String(rest<2?0:11-rest)};if(d.length===11)return calc(9,[10,9,8,7,6,5,4,3,2])===d[9]&&calc(10,[11,10,9,8,7,6,5,4,3,2])===d[10];return calc(12,[5,4,3,2,9,8,7,6,5,4,3,2])===d[12]&&calc(13,[6,5,4,3,2,9,8,7,6,5,4,3,2])===d[13]};

export function TrialForm() {
  const [form,setForm]=useState<Form>(initial);
  const [errors,setErrors]=useState<Partial<Record<FieldName,string>>>({});
  const [status,setStatus]=useState<TrialStatus|null>(null);
  const [plans,setPlans]=useState<PublicPlan[]>([]);
  const [loading,setLoading]=useState(true);
  const [submitting,setSubmitting]=useState(false);
  const [step,setStep]=useState<1|2>(1);
  const [serverError,setServerError]=useState('');
  const [showPassword,setShowPassword]=useState(false);
  const [registered,setRegistered]=useState<{name:string;workshop:string;email:string}|null>(null);
  const sending=useRef(false);

  const loadAvailability = async (isActive:()=>boolean) => {
    setLoading(true);
    try {
      const [statusResponse,plansResponse]=await Promise.all([
        fetch(trialApi+'/status',{cache:'no-store',signal:AbortSignal.timeout(12000)}),
        fetch(trialApi+'/plans',{cache:'no-store',signal:AbortSignal.timeout(12000)}),
      ]);
      const trialStatus:TrialStatus = statusResponse.ok ? await statusResponse.json() : {available:false};
      const data:unknown = plansResponse.ok ? await plansResponse.json() : [];
      if(!isActive())return;
      const available=Array.isArray(data)?data.filter((p):p is PublicPlan=>typeof p?.id==='number'&&['NeriTech Pro','NeriTech Ultra'].includes(p?.nome)):[];
      setStatus(trialStatus);
      setPlans(available);
      const requested=new URLSearchParams(window.location.search).get('plano');
      setForm(current=>{
        const selected=available.find(p=>String(p.id)===(requested||current.planoId))||available[0];
        return {...current,planoId:selected?String(selected.id):''};
      });
    } catch {
      if(!isActive())return;
      setStatus({available:false});
      setPlans([]);
    } finally {
      if(isActive())setLoading(false);
    }
  };
  useEffect(()=>{
    let active=true;
    void loadAvailability(()=>active);
    return()=>{active=false};
  },[]);
  const trialReady=status?.available===true&&!!status.legalVersion&&plans.length===2;

  const update=(key:FieldName,value:string|boolean)=>{setForm(current=>({...current,[key]:value}));setErrors(current=>({...current,[key]:undefined}));setServerError('')};
  const input=(label:string,key:FieldName,options?:{type?:string;autoComplete?:string;inputMode?:'tel'|'numeric';maxLength?:number})=><div className="nl-trial-field"><label htmlFor={key}>{label}</label><input id={key} name={key} type={options?.type||'text'} autoComplete={options?.autoComplete} inputMode={options?.inputMode} maxLength={options?.maxLength} value={String(form[key])} aria-invalid={!!errors[key]} aria-describedby={errors[key]?`${key}-error`:undefined} onChange={event=>update(key,key==='telefone'||key==='celular'?formatPhone(event.target.value):event.target.value)}/>{errors[key]&&<small id={`${key}-error`} role="alert">{errors[key]}</small>}</div>;

  function next(event:FormEvent){event.preventDefault();const found:Partial<Record<FieldName,string>>={};if(form.nomeCompleto.trim().split(/\s+/).length<2)found.nomeCompleto='Informe nome e sobrenome.';if(!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email.trim()))found.email='Informe um e-mail válido.';if(form.nomeEmpresa.trim().length<2)found.nomeEmpresa='Informe o nome da oficina.';if(!/^[1-9][0-9][0-9]{8,9}$/.test(digits(form.telefone)))found.telefone='Informe telefone com DDD.';if(!/^[1-9][0-9]9[0-9]{8}$/.test(digits(form.celular)))found.celular='Informe celular brasileiro com DDD.';setErrors(found);if(Object.keys(found).length)return;if(!trialReady){setServerError('O cadastro não está disponível. Verifique os planos e tente novamente.');return;}setStep(2);setServerError('')}

  async function submit(event:FormEvent){event.preventDefault();if(sending.current)return;setServerError('');const found:Partial<Record<FieldName,string>>={};if(!validDocument(form.cnpjOuCpf))found.cnpjOuCpf='Informe CPF ou CNPJ válido.';if(form.senha.length<12||form.senha.length>128)found.senha='A senha deve ter de 12 a 128 caracteres.';if(form.confirmarSenha!==form.senha)found.confirmarSenha='As senhas não coincidem.';if(!plans.some(p=>String(p.id)===form.planoId))found.planoId='Selecione um dos planos disponíveis.';if(!form.aceiteLegal)found.aceiteLegal='Aceite os termos para continuar.';setErrors(found);if(Object.keys(found).length)return;if(!trialReady){setServerError('Não foi possível confirmar a disponibilidade do cadastro. Tente novamente mais tarde.');return}sending.current=true;setSubmitting(true);try{const response=await fetch(trialApi+'/register',{method:'POST',headers:{'Content-Type':'application/json'},signal:AbortSignal.timeout(30000),body:JSON.stringify({nomeCompleto:form.nomeCompleto.trim(),email:form.email.trim().toLowerCase(),nomeEmpresa:form.nomeEmpresa.trim(),telefone:digits(form.telefone),celular:digits(form.celular),cnpjOuCpf:digits(form.cnpjOuCpf),segmento:form.segmento,senha:form.senha,planoId:Number(form.planoId),aceiteLegal:true,versaoLegal:status.legalVersion})});const data=await response.json().catch(()=>({}));if(response.ok&&data.success===true){setRegistered({name:form.nomeCompleto.trim(),workshop:form.nomeEmpresa.trim(),email:typeof data.email==='string'?data.email:form.email.trim()});setForm(initial)}else if(response.status===409)setServerError('E-mail ou documento já cadastrado. Use outro ou entre na sua conta.');else if(response.status===429)setServerError('Muitas tentativas. Aguarde antes de tentar novamente.');else if(response.status===503)setServerError('O cadastro está temporariamente indisponível. Tente novamente mais tarde.');else setServerError(typeof data.message==='string'?data.message:'Não foi possível concluir o cadastro. Verifique os dados e tente novamente.')}catch{setServerError('Não foi possível conectar ao servidor. Confira sua conexão e tente novamente.')}finally{sending.current=false;setSubmitting(false)}}

  if(registered)return <div className="nl-trial-success" role="status"><div className="nl-trial-success-icon"><Check size={28}/></div><span className="nl-kicker">NERITECH AUTO</span><h1>Boas-vindas, {registered.name.split(' ')[0]}!</h1><p>O cadastro de <strong>{registered.workshop}</strong> foi confirmado pela plataforma. Entre com <strong>{registered.email}</strong> e a senha que você definiu.</p><div className="nl-trial-access"><span>Endereço de acesso</span><a href={loginUrl}>{loginUrl}</a></div><a href={loginUrl} className="nl-button">Acessar minha oficina <ArrowRight size={17}/></a><p className="nl-trial-fine">Se o serviço de e-mail estiver configurado, você receberá também orientações de acesso. Nenhum SMS foi enviado por esta página.</p></div>;

  return <div className="nl-trial-card"><div className="nl-trial-head"><div><span className="nl-kicker">Comece por aqui</span><h1>Teste grátis</h1></div><span aria-label={`Etapa ${step} de 2`}>{step}/2</span></div>{loading?<div className="nl-trial-loading" role="status">Verificando disponibilidade e planos...</div>:<>
    {!trialReady&&<div className="nl-trial-unavailable" role="status"><p className="nl-trial-alert">O cadastro de teste está indisponível no momento. Não foi possível confirmar os planos e os termos legais junto à plataforma.</p><button type="button" className="nl-trial-retry" onClick={()=>void loadAvailability(()=>true)}>Tentar novamente</button></div>}
    {step===1?<form onSubmit={next} noValidate><p className="nl-trial-intro">Conte um pouco sobre você e sua oficina.</p><div className="nl-trial-grid">{input('Seu nome completo','nomeCompleto',{autoComplete:'name'})}{input('Seu e-mail','email',{type:'email',autoComplete:'email'})}{input('Nome da sua oficina','nomeEmpresa',{autoComplete:'organization'})}<div className="nl-trial-pair">{input('Telefone da oficina','telefone',{type:'tel',inputMode:'tel',autoComplete:'tel'})}{input('Celular do responsável','celular',{type:'tel',inputMode:'tel',autoComplete:'mobile tel'})}</div></div><button className="nl-button nl-trial-submit" type="submit" disabled={!trialReady}>Próximo <ArrowRight size={17}/></button></form>:
    <form onSubmit={submit} noValidate><button type="button" className="nl-trial-back" onClick={()=>{setStep(1);setErrors({});setServerError('')}}><ArrowLeft size={16}/> Voltar</button><h2>Dados de acesso</h2><p className="nl-trial-intro">Acesse a NERITECH AUTO com seu e-mail e a senha definida abaixo. Não há subdomínio por oficina.</p><div className="nl-trial-grid">{input('CPF ou CNPJ','cnpjOuCpf',{inputMode:'numeric',maxLength:18})}<div className="nl-trial-pair"><div className="nl-trial-field"><label htmlFor="segmento">Segmento</label><select id="segmento" value={form.segmento} onChange={event=>update('segmento',event.target.value)}><option value="oficina">Oficina mecânica</option><option value="autocenter">Auto center</option><option value="funilaria">Funilaria e pintura</option></select></div><div className="nl-trial-field"><label htmlFor="planoId">Plano</label><select id="planoId" value={form.planoId} onChange={event=>update('planoId',event.target.value)} aria-invalid={!!errors.planoId} aria-describedby={errors.planoId?'planoId-error':undefined}><option value="">Selecione</option>{plans.map(plan=><option key={plan.id} value={plan.id}>{plan.nome}</option>)}</select>{errors.planoId&&<small id="planoId-error" role="alert">{errors.planoId}</small>}</div></div><div className="nl-trial-pair"><div className="nl-trial-field"><label htmlFor="senha">Definir senha</label><div className="nl-trial-password"><input id="senha" type={showPassword?'text':'password'} autoComplete="new-password" value={form.senha} aria-invalid={!!errors.senha} aria-describedby={errors.senha?'senha-error':undefined} onChange={event=>update('senha',event.target.value)}/><button type="button" aria-label={showPassword?'Ocultar senhas':'Mostrar senhas'} onClick={()=>setShowPassword(value=>!value)}>{showPassword?<EyeOff size={17}/>:<Eye size={17}/>}</button></div>{errors.senha&&<small id="senha-error" role="alert">{errors.senha}</small>}</div>{input('Confirmar senha','confirmarSenha',{type:showPassword?'text':'password',autoComplete:'new-password'})}</div></div><p className="nl-trial-hint">Use pelo menos 12 caracteres. Sua senha nunca é enviada por e-mail.</p><label className="nl-trial-consent"><input type="checkbox" checked={form.aceiteLegal} onChange={event=>update('aceiteLegal',event.target.checked)} aria-invalid={!!errors.aceiteLegal}/><span>Li e aceito os <a href={status?.termsUrl?.startsWith('https://')?status.termsUrl:'/termos-de-uso'} target="_blank" rel="noopener noreferrer">Termos de Uso</a> e a <a href={status?.privacyUrl?.startsWith('https://')?status.privacyUrl:'/politica-de-privacidade'} target="_blank" rel="noopener noreferrer">Política de Privacidade</a>.</span></label>{errors.aceiteLegal&&<small className="nl-trial-error" role="alert">{errors.aceiteLegal}</small>}{serverError&&<p className="nl-trial-alert" role="alert">{serverError}</p>}<button className="nl-button nl-trial-submit" type="submit" disabled={submitting||!trialReady}>{submitting?'Enviando cadastro...':'Testar agora'}</button><p className="nl-trial-hint">Seus dados serão usados para processar o cadastro. Não informe dados de clientes.</p></form>}
  </>}</div>;
}
