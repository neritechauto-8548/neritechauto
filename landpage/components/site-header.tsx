'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { useState } from 'react';
import { ArrowUpRight, Menu, X } from 'lucide-react';
import { loginUrl } from '@/lib/site';

const links = [{href:'/funcionalidades',label:'Funcionalidades'},{href:'/precos',label:'Planos e preços'},{href:'/sobre',label:'Quem somos'}];

export function SiteHeader() {
  const path = usePathname();
  const [open, setOpen] = useState(false);
  return <header className="nl-site-header"><div className="nl-container nl-header-inner"><Link href="/" className="nl-brand" aria-label="NERITECH AUTO — início"><span className="nl-brand-mark">N</span><span>NERITECH <b>AUTO</b></span></Link><nav className="nl-desktop-nav" aria-label="Navegação principal">{links.map(item => <Link key={item.href} href={item.href} aria-current={path===item.href?'page':undefined}>{item.label}</Link>)}</nav><div className="nl-header-actions"><a href={loginUrl}>Entrar</a><Link className="nl-header-cta" href="/teste-gratis" aria-current={path==='/teste-gratis'?'page':undefined}>Teste grátis <ArrowUpRight size={16}/></Link></div><button className="nl-menu-toggle" type="button" aria-label={open?'Fechar menu':'Abrir menu'} aria-expanded={open} aria-controls="nl-mobile-nav" onClick={() => setOpen(value=>!value)}>{open?<X size={23}/>:<Menu size={23}/>}</button></div>{open && <nav className="nl-mobile-nav" id="nl-mobile-nav" aria-label="Navegação móvel">{links.map(item=><Link key={item.href} href={item.href} aria-current={path===item.href?'page':undefined} onClick={()=>setOpen(false)}>{item.label}</Link>)}<a href={loginUrl}>Entrar</a><Link href="/teste-gratis" className="nl-header-cta" onClick={()=>setOpen(false)}>Teste grátis <ArrowUpRight size={16}/></Link></nav>}</header>;
}
