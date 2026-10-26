import type { Metadata } from 'next';
import { ReplicaChrome } from '@/components/replica-chrome';
import { siteUrl, signature } from '@/lib/site';
import './globals.css';
import './marketing.css';
import './rebuild.css';
import './landing-fidelity.css';
import './reference-fidelity.css';
import './reference-about.css';
import './reference-original.css';

export const metadata: Metadata = {
  metadataBase: new URL(siteUrl),
  title: { default:'NERITECH AUTO | Gestão inteligente para oficinas',template:'%s | NERITECH AUTO' },
  description:signature,
  openGraph:{locale:'pt_BR',siteName:'NERITECH AUTO',type:'website'},
  icons:{icon:'/favicon.svg'}
};
export default function RootLayout({children}:Readonly<{children:React.ReactNode}>) {
 return <html lang="pt-BR"><body><a className="skip-link" href="#conteudo">Ir para o conteúdo</a><ReplicaChrome>{children}</ReplicaChrome></body></html>;
}