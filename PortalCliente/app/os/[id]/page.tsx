import {cookies} from 'next/headers';
import {redirect,notFound} from 'next/navigation';
import {PortalHeader} from '@/components/portal-header';
import {OrderDetail} from '@/components/order-detail';
import {COOKIE_NAME,validId} from '@/lib/server';
export const metadata={title:'Detalhes da ordem de serviço'};
export default async function Ordem({params}:{params:Promise<{id:string}>}){
  const {id}=await params;
  if(!validId(id))notFound();
  if(!(await cookies()).has(COOKIE_NAME))redirect('/login?next=/os/'+id);
  return <><PortalHeader authenticated/><OrderDetail id={id}/></>;
}
