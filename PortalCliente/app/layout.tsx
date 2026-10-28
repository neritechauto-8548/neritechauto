import type { Metadata, Viewport } from 'next';
import './globals.css';

export const metadata: Metadata = {
  title: {default:'Portal do Cliente | NERITECH AUTO',template:'%s | NERITECH AUTO'},
  description:'Acompanhe suas ordens de serviço, consulte seu histórico e responda aos orçamentos com segurança.',
  robots: {index:false,follow:false},
};
export const viewport: Viewport = {width:'device-width',initialScale:1};
export default function RootLayout({children}:{children:React.ReactNode}){
  return <html lang="pt-BR"><body>{children}</body></html>;
}
