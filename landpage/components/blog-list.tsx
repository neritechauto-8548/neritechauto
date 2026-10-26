'use client';
import { useState } from 'react';
import Link from 'next/link';
import Image from 'next/image';
import { posts } from '@/lib/posts';
export function BlogList(){const [category,setCategory]=useState('Todos');const categories=['Todos',...new Set(posts.map(p=>p.category))];const visible=category==='Todos'?posts:posts.filter(p=>p.category===category);return <><div className="flex flex-wrap gap-2 mb-8" role="group" aria-label="Filtrar artigos por categoria">{categories.map(c=><button key={c} className={`pill ${category===c?'!bg-[#18328a] !text-white':''}`} type="button" aria-pressed={category===c} onClick={()=>setCategory(c)}>{c}</button>)}</div><div className="grid-3">{visible.map(post=><article key={post.id} className="card blog-card"><Image src={post.image} width={900} height={520} alt=""/><div className="blog-card-content"><span className="eyebrow">{post.category} · {post.date}</span><h2 className="text-2xl font-bold">{post.title}</h2><p>{post.excerpt}</p><Link href={`/blog/${post.id}`}>Ler artigo →</Link></div></article>)}</div></>}
