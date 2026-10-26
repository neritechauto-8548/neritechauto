import { ReplicaPage } from '@/components/replica-page';
import { pageMetadata } from '@/lib/site';

export const metadata = pageMetadata(
  'Funcionalidades',
  'Conheça as funcionalidades de gestão de oficina da NERITECH AUTO: atendimento, orçamentos, OS, pátio, estoque, financeiro e fiscal.',
  '/funcionalidades',
);

export default function Page() {
  return <ReplicaPage page="funcionalidades" />;
}
