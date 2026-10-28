import { redirect } from 'next/navigation';

// Não publicar uma prévia de e-mail como confirmação real de cadastro.
export default function Page() {
  redirect('/teste-gratis');
}
