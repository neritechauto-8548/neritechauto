import { TrialForm } from '@/components/trial-form';
import { pageMetadata } from '@/lib/site';

export const metadata = pageMetadata(
  'Teste grátis',
  'Faça seu cadastro de teste na NERITECH AUTO. Confira os planos disponíveis e conclua o processo diretamente na plataforma.',
  '/teste-gratis',
);

export default function Page() {
  return <main id="conteudo" className="nl-page">
    <section className="nl-trial-stage" aria-label="Cadastro de teste grátis">
      <div className="nl-trial-stage-shade" aria-hidden="true" />
      <div className="nl-trial-stage-inner"><TrialForm /></div>
    </section>
  </main>;
}
