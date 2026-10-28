
'use client';
import { usePathname } from 'next/navigation';
import Image from 'next/image';
import { useState, type ReactNode } from 'react';
import { Button, buttonVariants } from '@/components/ui/button';
import { SiteHeader } from '@/components/site-header';
import { SiteFooter } from '@/components/site-footer';
import { cn } from '@/lib/utils';
import { loginUrl } from '@/lib/site';
import { ThemeToggle, useMarketingTheme } from '@/components/marketing-theme';

const replicaRoutes=new Set(['/','/funcionalidades','/precos','/sobre','/teste-gratis','/bem-vindo','/validar-telefone','/email']);
const links=[
  { href:'/funcionalidades', label:'O sistema' },
  { href:'/precos', label:'Planos' },
  { href:'/sobre', label:'Nossa empresa' },
];

function ReferenceLogo() {
 const {theme}=useMarketingTheme();
 return <a href="/" className="site-logo" aria-label="NERITECH AUTO — início">
   <Image src={theme==='dark'?'/neritech-logo-white.svg':'/neritech-logo.svg'} alt="NERITECH AUTO" width={222} height={44} priority />
 </a>;
}
function ReferenceHeader({ path }: { path:string }) {
 const [open,setOpen]=useState(false);
 const {theme}=useMarketingTheme();
 return <header className="site-header"><div className="container header-row">
   <a href="/" className="site-logo" aria-label="NERITECH AUTO — início"><Image src={theme==='dark'?'/neritech-logo-white.svg':'/neritech-logo.svg'} alt="NERITECH AUTO" width={222} height={44} priority /></a>
   <nav className={`nav-links${open?' open':''}`} id="main-nav" aria-label="Navegação principal">
     {links.map(l=><a key={l.href} href={l.href} className={l.href===path?'active':''} onClick={()=>setOpen(false)}>{l.label}</a>)}
     <a href={loginUrl} className="nav-login" onClick={()=>setOpen(false)}>Entrar</a>
     <ThemeToggle className="header-theme-mobile" />
   </nav>
   <div className="header-actions">
     <a href={loginUrl} className="header-login">Entrar</a>
     <ThemeToggle className="header-theme-desktop" />
     <a href="/teste-gratis" className={cn(buttonVariants({size:'sm'}),'btn','btn-orange')}>Teste grátis <span aria-hidden="true">↗</span></a>
     <Button type="button" variant="outline" size="sm" className="hamburger" aria-label={open?'Fechar menu':'Abrir menu'} aria-controls="main-nav" aria-expanded={open} onClick={()=>setOpen(v=>!v)}>{open?'×':'☰'}</Button>
   </div>
 </div></header>;
}
function ReferenceFooter() {
 return <footer className="site-footer"><div className="container"><div className="footer-columns">
   <div><ReferenceLogo/><p className="footer-follow">Mais controle na oficina. Mais resultado no caixa.</p></div>
   <div><h4>NERITECH AUTO</h4><p>Software de gestão com orçamentos, controle operacional, visão de lucro e apoio de IA.</p><a href="/funcionalidades">O sistema</a><a href="/precos">Planos</a><a href="/sobre">Nossa empresa</a></div>
   <div><h4>Comece agora</h4><a className="footer-link" href="/teste-gratis">Conhecer teste grátis</a><a className="footer-link" href={loginUrl}>Acessar minha oficina</a><h4>Informações legais</h4><a href="/termos-de-uso">Termos de uso</a><a href="/politica-de-privacidade">Privacidade</a><a href="/politica-de-cookies">Cookies</a></div>
 </div><div className="footer-bottom"><nav><a href="/">Início</a><a href="/dpa">Proteção de dados</a><a href="/blog">Blog</a></nav><span>© {new Date().getFullYear()} NERITECH AUTO</span></div></div></footer>;
}
export function ReplicaChrome({children}:{children:ReactNode}){
 const pathname=usePathname()??'/';
 const isReplica=replicaRoutes.has(pathname);
 if(!isReplica)return <><SiteHeader/><div className="marketing-header-spacer" aria-hidden="true"/>{children}<SiteFooter/></>;
 const noHead=pathname==='/validar-telefone'||pathname==='/email';
 const noFoot=['/teste-gratis','/bem-vindo','/validar-telefone','/email'].includes(pathname);
 return <>{!noHead&&<><ReferenceHeader key={pathname} path={pathname}/><div className="reference-header-spacer" aria-hidden="true"/></>} {children} {!noFoot&&<ReferenceFooter/>}</>;
}
