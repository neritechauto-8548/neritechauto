import { NextRequest } from 'next/server';
import { COOKIE_NAME,endpoint,json,requestBackend,safeError,sameOrigin,sessionAge } from '@/lib/server';

export async function POST(req: NextRequest) {
  if (!sameOrigin(req)) return json({message:'Requisição não autorizada.'},403);
  const path=endpoint('PORTAL_CUSTOMER_LOGIN_ENDPOINT');
  if (!path) return json({message:'A autenticação de clientes ainda não está disponível. Fale com sua oficina.'},503);
  const body=await req.json().catch(()=>null);
  const email=typeof body?.email==='string'?body.email.trim().toLowerCase():'';
  const senha=typeof body?.senha==='string'?body.senha:'';
  if (email.length>254 || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email) || senha.length<1 || senha.length>256) {
    return json({message:'Informe e-mail e senha válidos.'},422);
  }
  const r=await requestBackend(path,{method:'POST',body:{email,senha}});
  if (r.status!==200) return json({message:safeError(r.status)},r.status);
  const d=r.data as Record<string,unknown>|null;
  const clienteId=d?.clienteId,empresaId=d?.empresaId;
  const scope=d?.scope ?? d?.tipoConta;
  const token=d?.accessToken;
  if (scope !== 'PORTAL_CLIENTE' || typeof token !== 'string' || token.length<20 ||
      typeof clienteId !== 'number' || clienteId<=0 ||
      typeof empresaId !== 'number' || empresaId<=0) {
    return json({message:'O backend não retornou uma sessão válida de proprietário.'},502);
  }
  const result=json({ok:true});
  result.cookies.set(COOKIE_NAME,token,{
    httpOnly:true,secure:process.env.NODE_ENV==='production',sameSite:'lax',path:'/',maxAge:sessionAge()
  });
  return result;
}
