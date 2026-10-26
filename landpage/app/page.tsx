import type { Metadata } from 'next';
import { LandingHome } from '@/components/landing-home';
import { pageMetadata } from '@/lib/site';

export const metadata: Metadata = pageMetadata('Gestão para oficinas mecânicas', 'O futuro da gestão automotiva começa na sua oficina. Organize serviços, pátio, estoque e financeiro com a NERITECH AUTO.', '/');

export default function Home() { return <LandingHome/>; }
