import type { Metadata } from 'next';
import Image from 'next/image';
import { TrialForm } from '@/components/trial-form';
import { pageMetadata } from '@/lib/site';

export const metadata: Metadata = pageMetadata('Teste grátis', 'Cadastre sua oficina para conhecer o teste grátis da NERITECH AUTO, conforme disponibilidade do serviço.', '/teste-gratis');

export default function Trial() {
  return <main id="conteudo" className="nl-page"><section className="nl-trial-stage"><Image src="/images/home/oficina-mecanico.png" width={1536} height={1024} priority alt=""/><div className="nl-trial-stage-shade"/><div className="nl-trial-stage-inner"><TrialForm/></div></section></main>;
}
