import { ReplicaPage } from '@/components/replica-page';
import { pageMetadata } from '@/lib/site';

export const metadata = pageMetadata(
  'Quem somos',
  'Conheça a NERITECH AUTO e nossa proposta de simplificar a gestão de oficinas mecânicas, com tecnologia orientada à operação.',
  '/sobre',
);

export default function Page() {
  return <ReplicaPage page="sobre" />;
}
