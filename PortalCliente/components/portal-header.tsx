'use client';
import Link from 'next/link';
import {useRouter,usePathname} from 'next/navigation';
import {LogOut,ClipboardList} from 'lucide-react';
import {Brand} from './brand';
import {useState} from 'react';
export function PortalHeader({authenticated=false}:{authenticated?:boolean}){
  const router=useRouter(),path=usePathname();
  const [exiting,setExiting]=useState(false);
  async function logout(){
    setExiting(true);
    try{await fetch('/api/portal/logout',{method:'POST',cache:'no-store'});}
    finally {router.replace('/login');router.refresh();}
  }
  return <header className="portal-header"><div className="container header-inner">
    <Brand/>
    <nav className="header-links" aria-label="Navegação do portal">
      <Link className={path==='/consulta'?'selected':''} href="/consulta">Consultar OS</Link>
      {authenticated&&<Link className={path==='/historico'?'selected':''} href="/historico">Meu histórico</Link>}
    </nav>
    {authenticated?<button className="btn btn-quiet" disabled={exiting} onClick={logout}><LogOut size={17}/>{exiting?'Saindo…':'Sair'}</button>:
      <Link className="btn btn-outline" href="/login"><ClipboardList size={17}/>Área do cliente</Link>}
  </div></header>;
}
