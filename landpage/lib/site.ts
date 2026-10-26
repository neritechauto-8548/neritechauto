export const siteUrl = process.env.NEXT_PUBLIC_SITE_URL || 'https://neritechauto.com.br';
export const signature = 'Da recepção à entrega. Sua oficina sob controle.';
export const heroTitle = 'Da recepção à entrega, sua oficina organizada em cada etapa.';
export const heroSubtitle = 'Reúna clientes, veículos, orçamentos, ordens de serviço, gestão de pátio, estoque e financeiro em uma rotina organizada.';
export const trialUrl = '/teste-gratis';
export const loginUrl = process.env.NEXT_PUBLIC_APP_URL || 'https://sistema.neritechauto.com.br/auth/login';
export const nav = [{ href: '/funcionalidades', label: 'Funcionalidades' }, { href: '/precos', label: 'Planos' }, { href: '/sobre', label: 'Quem somos' }, { href: '/blog', label: 'Blog' }];
export function pageMetadata(title: string, description: string, path: string) { return { title, description, alternates: { canonical: path }, openGraph: { title, description, url: path, siteName: 'NERITECH AUTO', locale: 'pt_BR', type: 'website' as const } }; }
