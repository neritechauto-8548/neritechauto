export const siteUrl = process.env.NEXT_PUBLIC_SITE_URL || 'https://neritechauto.com.br';
export const signature = 'Orçamentos claros. Controle da operação. Lucro em foco. IA para apoiar decisões.';
export const heroTitle = 'Do orçamento ao lucro, sua oficina sob controle.';
export const heroSubtitle = 'Orçamentos e OS em um só fluxo, controle dos custos e visão dos resultados. IA para apoiar consultas e análises quando habilitada.';
export const trialUrl = '/teste-gratis';
export const loginUrl = process.env.NEXT_PUBLIC_APP_URL || 'https://sistema.neritechauto.com.br/auth/login';
export const nav = [{ href: '/funcionalidades', label: 'Funcionalidades' }, { href: '/precos', label: 'Planos' }, { href: '/sobre', label: 'Quem somos' }, { href: '/blog', label: 'Blog' }];
export function pageMetadata(title: string, description: string, path: string) { return { title, description, alternates: { canonical: path }, openGraph: { title, description, url: path, siteName: 'NERITECH AUTO', locale: 'pt_BR', type: 'website' as const } }; }
