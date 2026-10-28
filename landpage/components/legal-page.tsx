import legal from '@/lib/legal-content.json';
type Slug=keyof typeof legal;
export function LegalPage({slug}:{slug:Slug}){const page=legal[slug];return <main id="conteudo"><article className="legal prose"><span className="eyebrow">Informações legais</span><h1>{page.title}</h1><p className="note">Conteúdo migrado da página anterior. Confira a versão contratual aplicável antes de contratar.</p><div dangerouslySetInnerHTML={{__html:page.html}}/></article></main>}
