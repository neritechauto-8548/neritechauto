import Link from 'next/link';
import { ArrowUpRight } from 'lucide-react';
import { loginUrl } from '@/lib/site';

export function SiteFooter() {
  return <footer className="nl-site-footer"><div className="nl-container nl-footer-grid"><div className="nl-footer-about"><Link href="/" className="nl-brand" aria-label="NERITECH AUTO — início"><span className="nl-brand-mark">N</span><span>NERITECH <b>AUTO</b></span></Link><p>Mais controle na oficina. Mais resultado no caixa.</p></div><div><h2>NERITECH AUTO</h2><Link href="/">Início</Link><Link href="/funcionalidades">O sistema</Link><Link href="/precos">Planos</Link><Link href="/sobre">Nossa empresa</Link></div><div><h2>Informações</h2><Link href="/termos-de-uso">Termos de uso</Link><Link href="/politica-de-privacidade">Política de privacidade</Link><Link href="/politica-de-cookies">Cookies</Link><Link href="/dpa">Proteção de dados</Link></div><div><h2>Comece agora</h2><p>Conheça os recursos de orçamento, controle, financeiro e IA, conforme o plano escolhido.</p><Link className="nl-footer-cta" href="/teste-gratis">Conhecer teste grátis <ArrowUpRight size={16}/></Link><a href={loginUrl}>Já tenho acesso</a></div></div><div className="nl-container nl-footer-bottom"><span>© {new Date().getFullYear()} NERITECH AUTO. Todos os direitos reservados.</span><span>Software de gestão para oficinas mecânicas</span></div></footer>;
}
