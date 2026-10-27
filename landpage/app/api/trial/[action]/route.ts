import { NextResponse } from 'next/server';
import { apiBase } from '@/lib/trial-api';

export const runtime = 'nodejs';
export const dynamic = 'force-dynamic';

type Context = {params:Promise<{action:string}>};
const noCache = {'Cache-Control':'no-store'};
const allowedGet = new Set(['status','plans']);

function unavailable() {
  return NextResponse.json({message:'Serviço de cadastro temporariamente indisponível.'},
    {status:503,headers:noCache});
}

async function forward(action:string, method:'GET'|'POST', body?:string) {
  try {
    const response = await fetch(apiBase+'/public/trial/'+action, {
      method,
      headers:{'Accept':'application/json', ...(method==='POST'?{'Content-Type':'application/json'}:{})},
      ...(body === undefined ? {} : {body}),
      signal:AbortSignal.timeout(method==='POST'?30000:12000),
      cache:'no-store',
      redirect:'manual',
    });
    if (response.status>=300 && response.status<400) return unavailable();
    const data:unknown = await response.json().catch(()=>null);
    if (data===null) return unavailable();
    return NextResponse.json(data, {status:response.status,headers:noCache});
  } catch {
    return unavailable();
  }
}

export async function GET(_request:Request, context:Context) {
  const {action}=await context.params;
  if (!allowedGet.has(action))
    return NextResponse.json({message:'Rota não encontrada.'},{status:404,headers:noCache});
  return forward(action,'GET');
}

export async function POST(request:Request, context:Context) {
  const {action}=await context.params;
  if (action!=='register')
    return NextResponse.json({message:'Rota não encontrada.'},{status:404,headers:noCache});
  if (Number(request.headers.get('content-length')||0)>12288)
    return NextResponse.json({message:'Dados enviados excedem o limite.'},{status:413,headers:noCache});
  const body=await request.text();
  if (body.length>12288)
    return NextResponse.json({message:'Dados enviados excedem o limite.'},{status:413,headers:noCache});
  try {
    const value:unknown=JSON.parse(body);
    if (!value || typeof value!=='object' || Array.isArray(value))
      return NextResponse.json({message:'Dados inválidos.'},{status:400,headers:noCache});
  } catch {
    return NextResponse.json({message:'Dados inválidos.'},{status:400,headers:noCache});
  }
  // O backend valida, limita tentativas e persiste o cadastro.
  // Não registrar credenciais, nem repetir automaticamente a operação.
  return forward('register','POST',body);
}
