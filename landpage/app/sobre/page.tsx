import { ReplicaPage } from '@/components/replica-page';
import { pageMetadata } from '@/lib/site';

export const metadata = pageMetadata(
  'Quem somos',
  'Conheça a NERITECH AUTO: orçamentos, controle operacional, lucratividade e inteligência artificial como apoio à gestão de oficinas.',
  '/sobre',
);

export default function Page() {
  return <ReplicaPage page="sobre" />;
}
