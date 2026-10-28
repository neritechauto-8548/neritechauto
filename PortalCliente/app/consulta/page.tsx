import {AuthLayout} from '@/components/auth-layout';
import {ConsultaForm} from '@/components/consulta-form';
export const metadata={title:'Consultar ordem de serviço'};
export default function Consulta(){return <AuthLayout eyebrow="Consulta de OS" title="Acompanhe seu veículo." description="Consulte o andamento da ordem de serviço com as informações e o código de acesso enviados pela oficina."><ConsultaForm/></AuthLayout>;}
