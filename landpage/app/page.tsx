import { ReplicaPage } from '@/components/replica-page';
import { pageMetadata } from '@/lib/site';

export const metadata = pageMetadata(
  'Mais controle na oficina. Mais resultado no caixa.',
  'Cada orçamento conta. Cada resultado importa. Crie orçamentos, acompanhe serviços, custos e margens, com inteligência artificial de apoio quando disponível.',
  '/',
);

export default function Page() {
  return <ReplicaPage page="home" />;
}
