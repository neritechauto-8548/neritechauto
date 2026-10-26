
'use client';
import { usePathname } from 'next/navigation';
import { useState, type ReactNode } from 'react';
import { Button, buttonVariants } from '@/components/ui/button';
import { SiteHeader } from '@/components/site-header';
import { SiteFooter } from '@/components/site-footer';
import { cn } from '@/lib/utils';

const replicaRoutes=new Set(['/','/funcionalidades','/precos','/sobre','/teste-gratis','/bem-vindo','/validar-telefone','/email']);
const links=[
  { href:'/funcionalidades', label:'Funcionalidades' },
  { href:'/precos', label:'Planos e preços' },
  { href:'/sobre', label:'Quem somos' },
];

function ReferenceLogo() {
 return <a href="/" className="site-logo" aria-label="Onmotor — início">
   <span className="logo-mark" aria-hidden="true">♧</span><span className="logo-on">ON</span>MOTOR
 </a>;
}
function ReferenceHeader({ path }: { path:string }) {
 const [open,setOpen]=useState(false);
 return <header className="site-header"><div className="container header-row">
   <ReferenceLogo/>
   <nav className={`nav-links${open?' open':''}`} id="main-nav" aria-label="Navegação principal">
     {links.map(l=><a key={l.href} href={l.href} className={l.href===path?'active':''} onClick={()=>setOpen(false)}>{l.label}</a>)}
   </nav>
   <div className="header-actions">
     <a href="/teste-gratis" className={cn(buttonVariants({size:'sm'}),'btn','btn-orange')}>Teste grátis <span aria-hidden="true">↗</span></a>
     <button type="button" className="lang" aria-label="Idioma Português" title="Português"><span>◎</span> <span>PT</span></button>
     <Button type="button" variant="outline" size="sm" className="hamburger" aria-label={open?'Fechar menu':'Abrir menu'} aria-controls="main-nav" aria-expanded={open} onClick={()=>setOpen(v=>!v)}>{open?'×':'☰'}</Button>
   </div>
 </div></header>;
}
function ReferenceFooter() {
 return <footer className="site-footer"><div className="container"><div className="footer-columns">
   <div><ReferenceLogo/><div className="footer-follow">Acompanhe nas redes</div><div className="footer-socials"><a href="#" title="Instagram">◎</a><a href="#" title="YouTube">▶</a><a href="#" title="TikTok">♪</a><a href="#" title="Facebook">▣</a><a href="#" title="LinkedIn">▥</a></div></div>
   <div><h4>Contato</h4><p><strong>E-mail</strong> suporte@onmotor.com.br</p><p><strong>Endereço</strong> Alameda Afonso Schmidt, 119 · Santa Teresinha · São Paulo (SP)</p><a className="footer-phone" href="tel:+551126497495">✆ &nbsp;(11) 2649-7495</a><a className="footer-phone" href="tel:+5511971569231">◉ &nbsp;(11) 97156-9231</a></div>
   <div><h4>Acesso remoto</h4><a className="footer-link" href="#">▣ &nbsp;Baixe o nosso acesso remoto</a><h4 style={{marginTop:21}}>Baixe o app Onmotor</h4><div className="app-store-buttons"><a href="#">● App Store</a><a href="#">▶ Google Play</a></div></div>
 </div><div className="footer-bottom"><nav><a href="/">Início</a><a href="/sobre">Quem Somos</a><a href="#">Blog</a><a href="#">Política de Privacidade do App Onmotor</a></nav><span>© 2026 Onmotor</span></div></div></footer>;
}
export function ReplicaChrome({children}:{children:ReactNode}){
 const pathname=usePathname()??'/';
 const isReplica=replicaRoutes.has(pathname);
 if(!isReplica)return <><SiteHeader/>{children}<SiteFooter/></>;
 const noHead=pathname==='/validar-telefone'||pathname==='/email';
 const noFoot=['/teste-gratis','/bem-vindo','/validar-telefone','/email'].includes(pathname);
 return <>{!noHead&&<ReferenceHeader key={pathname} path={pathname}/>} {children} {!noFoot&&<ReferenceFooter/>}</>;
}
