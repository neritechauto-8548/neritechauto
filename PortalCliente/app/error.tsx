'use client';
import {useEffect} from 'react';
export default function Error({error,reset}:{error:Error;reset:()=>void}){
  useEffect(()=>{ /* Não registrar erros ou detalhes pessoais no console do cliente. */ },[error]);
  return <main className="centered"><div className="panel notice">
    <p className="eyebrow">Não foi possível concluir</p><h1>Ocorreu um problema ao carregar a página.</h1>
    <p>Tente novamente. Seus dados não foram alterados.</p>
    <button className="btn btn-primary" onClick={reset}>Tentar novamente</button>
  </div></main>;
}
