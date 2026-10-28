import type { Metadata } from 'next';
import { ReplicaChrome } from '@/components/replica-chrome';
import { MarketingThemeProvider } from '@/components/marketing-theme';
import { siteUrl, signature } from '@/lib/site';
import './globals.css';
import './marketing.css';
import './rebuild.css';
import './landing-fidelity.css';
import './reference-fidelity.css';
import './reference-about.css';
import './reference-original.css';
import './home-card-size.css';
import './neritech-brand.css';
import './marketing-dark.css';
import './home-feature-cards.css';

export const metadata: Metadata = {
  metadataBase: new URL(siteUrl),
  title: { default:'NERITECH AUTO | Orçamentos, controle, lucro e IA',template:'%s | NERITECH AUTO' },
  description:signature,
  openGraph:{locale:'pt_BR',siteName:'NERITECH AUTO',type:'website',images:[{url:'/images/home/hero-oficina-neritech.png',width:1672,height:940,alt:'NERITECH AUTO — gestão para oficinas mecânicas'}]},
  twitter:{card:'summary_large_image'},
  icons:{icon:'/favicon.svg'}
};
export default function RootLayout({children}:Readonly<{children:React.ReactNode}>) {
 return <html lang="pt-BR"><head><script dangerouslySetInnerHTML={{__html:`(function(){try{var s=localStorage.getItem('neritech-marketing-theme');document.documentElement.dataset.theme=s==='dark'?'dark':'light'}catch(e){document.documentElement.dataset.theme='light'}})();`}} /></head><body><MarketingThemeProvider><a className="skip-link" href="#conteudo">Ir para o conteúdo</a><ReplicaChrome>{children}</ReplicaChrome></MarketingThemeProvider></body></html>;
}