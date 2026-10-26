import { ReplicaPage } from '@/components/replica-page';
import { pageMetadata } from '@/lib/site';

export const metadata = pageMetadata(
  'Orçamentos, controle e lucro com apoio de IA',
  'NERITECH AUTO: orçamentos, controle da oficina, gestão financeira e inteligência artificial de apoio, conforme disponibilidade. Conheça os dois planos.',
  '/',
);

export default function Page() {
  return <ReplicaPage page="home" />;
}
