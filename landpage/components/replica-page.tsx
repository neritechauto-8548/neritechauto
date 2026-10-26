
import Script from 'next/script';
import { replicaPages, type ReplicaKey } from '@/lib/reference-pages';

// Conteúdo estático fornecido pelo usuário. Sem requests nem dados recebidos de terceiros.
// HTML original mantém a geometria e todas as classes do CSS de referência.
export function ReplicaPage({page}:{page:ReplicaKey}){
 const {html,bodyClass}=replicaPages[page];
 const markup=html.replace(/<main(\s|>)/i,(_m,sep)=>'<main id="conteudo"'+sep);
 return <div className={`reference-site ${bodyClass}`} data-page={page}>
   <div dangerouslySetInnerHTML={{__html:markup}}/>
   <Script src="/reference/site.js" strategy="afterInteractive"/>
 </div>;
}
