import type { Metadata } from 'next';
import { BlogList } from '@/components/blog-list';
import { pageMetadata } from '@/lib/site';
export const metadata:Metadata=pageMetadata('Blog','Conteúdos sobre gestão, finanças e mercado para oficinas mecânicas.','/blog');
export default function Blog(){return <main id="conteudo"><section className="page-heading"><div className="wrap"><span className="eyebrow">Conteúdo</span><h1>Ideias para uma gestão mais consciente.</h1><p>Artigos sobre processos, custos e mudanças no setor automotivo.</p></div></section><section className="section"><div className="wrap"><BlogList/></div></section></main>}
