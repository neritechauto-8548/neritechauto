import type { Metadata } from 'next';
import { LegalPage } from '@/components/legal-page';
import { pageMetadata } from '@/lib/site';
export const metadata:Metadata=pageMetadata('DPA','Acordo de processamento de dados da NERITECH AUTO.','/dpa');
export default function Page(){return <LegalPage slug="dpa"/>}
