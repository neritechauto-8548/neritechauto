import type { Metadata } from 'next';
import Link from 'next/link';
import { notFound } from 'next/navigation';
import { posts } from '@/lib/posts';
import { pageMetadata } from '@/lib/site';
export function generateStaticParams(){return posts.map(post=>({id:post.id}))}
export async function generateMetadata({params}:{params:Promise<{id:string}>}):Promise<Metadata>{const {id}=await params;const post=posts.find(p=>p.id===id);return post?pageMetadata(post.title,post.excerpt,`/blog/${id}`):{title:'Artigo não encontrado'}}
export default async function Post({params}:{params:Promise<{id:string}>}){const {id}=await params;const found=posts.find(p=>p.id===id);if(!found)notFound();const post=found!;return <main id="conteudo"><article className="legal prose"><Link className="font-bold text-[#18328a]" href="/blog">← Voltar ao blog</Link><span className="eyebrow !block mt-10">{post.category} · {post.date}</span><h1>{post.title}</h1><p className="text-lg">{post.excerpt}</p><hr className="blog-editorial-rule" />{post.sections.map((section,i)=><section key={i}>{section.heading&&<h2>{section.heading}</h2>}{section.paragraphs.map(p=><p key={p}>{p}</p>)}</section>)}</article></main>}
