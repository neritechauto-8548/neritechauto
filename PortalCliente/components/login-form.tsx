'use client';
import {useRouter,useSearchParams} from 'next/navigation';
import {useState} from 'react';
import Link from 'next/link';
import {ArrowRight,Eye,EyeOff,Info} from 'lucide-react';
export function LoginForm(){
  const router=useRouter(),search=useSearchParams();
  const [email,setEmail]=useState(''),[senha,setSenha]=useState(''),[show,setShow]=useState(false),[loading,setLoading]=useState(false),[error,setError]=useState('');
  async function submit(e:React.FormEvent<HTMLFormElement>){
    e.preventDefault();setError('');
    if(!email.trim()||!senha){setError('Preencha e-mail e senha.');return;}
    setLoading(true);
    try{
      const response=await fetch('/api/portal/login',{method:'POST',headers:{'Content-Type':'application/json'},cache:'no-store',body:JSON.stringify({email,senha})});
      const body=await response.json();
      if(!response.ok){setError(body.message||'Não foi possível entrar.');return;}
      const next=search.get('next');
      const safe=next?.startsWith('/')&&!next.startsWith('//')&&!next.includes('\\')?next:'/historico';
      router.replace(safe);router.refresh();
    }catch{setError('Falha de conexão. Tente novamente.');}
    finally{setLoading(false);}
  }
  return <><div className="form-panel"><form onSubmit={submit}>
    <div className="field"><label htmlFor="email">E-mail cadastrado</label><input id="email" type="email" autoComplete="username" placeholder="voce@email.com" value={email} onChange={e=>setEmail(e.target.value)} maxLength={254} required/></div>
    <div className="field"><label htmlFor="password">Senha</label>
      <div className="password-box"><input id="password" type={show?'text':'password'} autoComplete="current-password" placeholder="Sua senha" value={senha} onChange={e=>setSenha(e.target.value)} required maxLength={256}/>
      <button type="button" className="password-toggle" onClick={()=>setShow(!show)} aria-label={show?'Ocultar senha':'Mostrar senha'}>{show?<EyeOff size={18}/>:<Eye size={18}/>}</button></div>
    </div>
    {error&&<p className="alert alert-error" role="alert"><Info size={18}/>{error}</p>}
    <button className="btn btn-primary full" disabled={loading} type="submit">{loading?'Validando acesso…':'Entrar na minha conta'} <ArrowRight size={17}/></button>
    <p className="hint centered-text">O cadastro de acesso é fornecido pela oficina. Para recuperar sua senha, entre em contato com ela.</p>
  </form></div><p className="auth-bottom">Tem um código de consulta? <Link href="/consulta">Consultar OS</Link></p></>;
}
