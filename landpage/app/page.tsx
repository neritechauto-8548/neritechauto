import { ReplicaPage } from '@/components/replica-page';
import { pageMetadata } from '@/lib/site';

export const metadata = pageMetadata(
  'Sistema de gestão para oficinas mecânicas',
  'NERITECH AUTO: clientes, veículos, orçamentos, ordens de serviço, gestão de pátio, estoque e financeiro em um só lugar.',
  '/',
);

export default function Page() {
  return <ReplicaPage page="home" />;
}
