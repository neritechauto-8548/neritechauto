export const siteUrl = process.env.NEXT_PUBLIC_SITE_URL || 'https://neritechauto.com.br';
export const signature = 'Inteligência na gestão. Controle na operação. Lucro em foco.';
export const heroTitle = 'Organize sua oficina, acompanhe seu lucro e tome decisões mais inteligentes com IA.';
export const heroSubtitle = 'Da entrada do veículo à entrega, conecte orçamentos, ordens de serviço, pátio, estoque e financeiro. Enxergue custos, margens e resultados com clareza, sem perder o controle da operação.';
export const trialUrl = '/teste-gratis';
export const loginUrl = process.env.NEXT_PUBLIC_APP_URL || 'https://sistema.neritechauto.com.br/auth/login';
export const nav = [{ href: '/funcionalidades', label: 'Funcionalidades' }, { href: '/precos', label: 'Planos' }, { href: '/sobre', label: 'Quem somos' }, { href: '/blog', label: 'Blog' }];
export function pageMetadata(title: string, description: string, path: string) { return { title, description, alternates: { canonical: path }, openGraph: { title, description, url: path, siteName: 'NERITECH AUTO', locale: 'pt_BR', type: 'website' as const } }; }
