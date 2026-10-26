import type { MetadataRoute } from 'next';
import { posts } from '@/lib/posts';
import { siteUrl } from '@/lib/site';
export default function sitemap():MetadataRoute.Sitemap{const paths=['/','/funcionalidades','/precos','/teste-gratis','/sobre','/blog','/politica-de-privacidade','/termos-de-uso','/politica-de-cookies','/dpa',...posts.map(post=>`/blog/${post.id}`)];return paths.map(path=>({url:new URL(path,siteUrl).toString(),changeFrequency:path==='/'?'weekly':'monthly',priority:path==='/'?1:.7}))}
