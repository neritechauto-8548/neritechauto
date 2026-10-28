import type { Metadata } from 'next';
import { LegalPage } from '@/components/legal-page';
import { pageMetadata } from '@/lib/site';
export const metadata:Metadata=pageMetadata('Termos de Uso','Termos de uso da NERITECH AUTO.','/termos-de-uso');
export default function Page(){return <LegalPage slug="termos-de-uso"/>}
