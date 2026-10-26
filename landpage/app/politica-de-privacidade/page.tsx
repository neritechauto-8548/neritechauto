import type { Metadata } from 'next';
import { LegalPage } from '@/components/legal-page';
import { pageMetadata } from '@/lib/site';
export const metadata:Metadata=pageMetadata('Política de Privacidade','Informações sobre privacidade e tratamento de dados.','/politica-de-privacidade');
export default function Page(){return <LegalPage slug="politica-de-privacidade"/>}
