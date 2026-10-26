
import Script from 'next/script';
import { replicaPages, type ReplicaKey } from '@/lib/reference-pages';

// Conteúdo editorial próprio da NERITECH AUTO. Sem requests nem dados recebidos de terceiros.
// HTML preserva a geometria e as classes visuais, com redação própria da marca.
export function ReplicaPage({page}:{page:ReplicaKey}){
 const {html,bodyClass}=replicaPages[page];
 const markup=html.replace(/<main(\s|>)/i,(_m,sep)=>'<main id="conteudo"'+sep);
 return <div className={`reference-site ${bodyClass}`} data-page={page}>
   <div dangerouslySetInnerHTML={{__html:markup}}/>
   <Script src="/reference/site.js" strategy="afterInteractive"/>
 </div>;
}
