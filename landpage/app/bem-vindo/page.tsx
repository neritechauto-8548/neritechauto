import { redirect } from 'next/navigation';

// A página de referência simulava um cadastro concluído sem persistência.
export default function Page() {
  redirect('/teste-gratis');
}
