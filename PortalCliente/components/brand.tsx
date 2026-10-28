import Link from 'next/link';
export function Brand({inverse=false}:{inverse?:boolean}){
  return <Link href="/consulta" className={'brand'+(inverse?' brand-inverse':'')} aria-label="NERITECH AUTO — início">
    <span>NERITECH</span><span>AUTO</span>
  </Link>;
}
