export const siteUrl = process.env.NEXT_PUBLIC_SITE_URL || 'https://neritechauto.com.br';
export const signature = 'Mais controle na oficina. Mais resultado no caixa.';
export const heroTitle = 'Mais controle na oficina. Mais resultado no caixa.';
export const heroSupport = 'Cada orçamento conta. Cada resultado importa.';
export const heroSubtitle = 'Crie orçamentos, acompanhe os serviços e tenha controle sobre custos, margens e lucro. Tudo em uma plataforma de gestão para oficinas, com inteligência artificial para apoiar suas decisões.';
export const trialUrl = '/teste-gratis';
export const loginUrl = process.env.NEXT_PUBLIC_APP_URL || 'https://sistema.neritechauto.com.br/auth/login';
export const nav = [{ href: '/funcionalidades', label: 'Funcionalidades' }, { href: '/precos', label: 'Planos' }, { href: '/sobre', label: 'Quem somos' }, { href: '/blog', label: 'Blog' }];
export function pageMetadata(title: string, description: string, path: string) { return { title, description, alternates: { canonical: path }, openGraph: { title, description, url: path, siteName: 'NERITECH AUTO', locale: 'pt_BR', type: 'website' as const } }; }
