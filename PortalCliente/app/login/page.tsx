import {AuthLayout} from '@/components/auth-layout';
import {LoginForm} from '@/components/login-form';
import {Suspense} from 'react';
export const metadata={title:'Acessar minha conta'};
export default function Login(){return <AuthLayout eyebrow="Portal do cliente" title="Bom ter você por aqui." description="Acesse os serviços e o histórico disponibilizados pela sua oficina."><Suspense fallback={<p className="hint">Preparando acesso…</p>}><LoginForm/></Suspense></AuthLayout>;}
