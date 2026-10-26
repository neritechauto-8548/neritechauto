import Link from 'next/link';
import { ArrowUpRight } from 'lucide-react';
import { loginUrl } from '@/lib/site';

export function SiteFooter() {
  return <footer className="nl-site-footer"><div className="nl-container nl-footer-grid"><div className="nl-footer-about"><Link href="/" className="nl-brand" aria-label="NERITECH AUTO — início"><span className="nl-brand-mark">N</span><span>NERITECH <b>AUTO</b></span></Link><p>Inteligência na gestão. Controle na operação. Lucro em foco.</p></div><div><h2>Explore</h2><Link href="/">Início</Link><Link href="/funcionalidades">Funcionalidades</Link><Link href="/precos">Planos e preços</Link><Link href="/sobre">Quem somos</Link></div><div><h2>Informações</h2><Link href="/termos-de-uso">Termos de uso</Link><Link href="/politica-de-privacidade">Política de privacidade</Link><Link href="/politica-de-cookies">Cookies</Link><Link href="/dpa">Proteção de dados</Link></div><div><h2>Comece agora</h2><p>Conheça o fluxo de gestão e confira as condições atuais do teste.</p><Link className="nl-footer-cta" href="/teste-gratis">Conhecer teste grátis <ArrowUpRight size={16}/></Link><a href={loginUrl}>Já tenho acesso</a></div></div><div className="nl-container nl-footer-bottom"><span>© {new Date().getFullYear()} NERITECH AUTO. Todos os direitos reservados.</span><span>Software de gestão para oficinas mecânicas</span></div></footer>;
}
