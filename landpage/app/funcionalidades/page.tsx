import { ReplicaPage } from '@/components/replica-page';
import { pageMetadata } from '@/lib/site';

export const metadata = pageMetadata(
  'Funcionalidades',
  'Explore orçamentos, controle de serviços, gestão de pátio, custos, financeiro e inteligência artificial de apoio da NERITECH AUTO, conforme disponibilidade.',
  '/funcionalidades',
);

export default function Page() {
  return <ReplicaPage page="funcionalidades" />;
}
