import { redirect } from 'next/navigation';

// Não apresentar confirmação SMS sem provedor e verificação no backend.
export default function Page() {
  redirect('/teste-gratis');
}
