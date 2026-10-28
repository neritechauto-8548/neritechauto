import type { Metadata } from 'next';
import { LegalPage } from '@/components/legal-page';
import { pageMetadata } from '@/lib/site';
export const metadata:Metadata=pageMetadata('Política de Cookies','Informações sobre cookies no site da NERITECH AUTO.','/politica-de-cookies');
export default function Page(){return <LegalPage slug="politica-de-cookies"/>}
