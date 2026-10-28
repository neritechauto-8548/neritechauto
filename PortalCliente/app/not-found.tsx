import Link from 'next/link';
import {Brand} from '@/components/brand';
export default function NotFound(){
  return <main className="centered"><div className="panel notice">
    <Brand/><p className="eyebrow">Página indisponível</p>
    <h1>Não encontramos esta página.</h1><p>Confira o endereço ou volte para a consulta de ordens.</p>
    <Link className="btn btn-primary" href="/consulta">Ir para consulta</Link>
  </div></main>;
}
