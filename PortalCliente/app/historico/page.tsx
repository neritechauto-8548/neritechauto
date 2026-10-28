import {cookies} from 'next/headers';
import {redirect} from 'next/navigation';
import {PortalHeader} from '@/components/portal-header';
import {HistoryView} from '@/components/history-view';
import {COOKIE_NAME} from '@/lib/server';
export const metadata={title:'Meu histórico'};
export default async function Historico(){
  if(!(await cookies()).has(COOKIE_NAME)) redirect('/login?next=/historico');
  return <><PortalHeader authenticated/><HistoryView/></>;
}
