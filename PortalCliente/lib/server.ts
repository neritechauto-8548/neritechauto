import { cookies } from 'next/headers';
import { NextRequest, NextResponse } from 'next/server';

export const COOKIE_NAME = 'nt_customer_access';
const noStore = { 'Cache-Control':'no-store, private', 'X-Content-Type-Options':'nosniff' };

export function json(data: unknown, status=200) {
  return NextResponse.json(data,{status, headers:noStore});
}
export function safeError(status: number): string {
  switch(status) {
    case 400: case 422: return 'Verifique os dados informados e tente novamente.';
    case 401: return 'Sua sessão expirou. Entre novamente.';
    case 403: return 'Você não possui autorização para esta informação.';
    case 404: return 'Não encontramos uma ordem com os dados informados.';
    case 409: return 'A solicitação foi alterada. Atualize a página e tente novamente.';
    case 429: return 'Muitas tentativas. Aguarde antes de tentar novamente.';
    default: return 'Serviço temporariamente indisponível. Tente novamente.';
  }
}
export function sameOrigin(req: NextRequest): boolean {
  const origin = req.headers.get('origin');
  if (!origin) return false;
  try { return new URL(origin).host === req.nextUrl.host && new URL(origin).protocol === req.nextUrl.protocol; }
  catch { return false; }
}
export function validId(id: string): boolean {
  return /^[1-9]\d{0,15}$/.test(id) && Number.isSafeInteger(Number(id));
}
export function endpoint(envName: string, id?: string): string | null {
  const path=process.env[envName];
  if (!path || !path.startsWith('/') || path.startsWith('//')) return null;
  if (id) {
    if (!validId(id)) return null;
    return path.includes('{id}') ? path.replace('{id}',id) : path.replace(/\/$/,'')+'/'+id;
  }
  return path;
}
export async function requestBackend(
  endpointPath:string, options:{method?:'GET'|'POST';body?:unknown;token?:string}
):Promise<{status:number;data:unknown}> {
  const root=process.env.PORTAL_BACKEND_URL;
  if (!root) return {status:503,data:null};
  try {
    const rootUrl=new URL(root);
    const local=['localhost','127.0.0.1'].includes(rootUrl.hostname);
    if (rootUrl.protocol !== 'https:' && !(process.env.NODE_ENV !== 'production' && local && rootUrl.protocol === 'http:')) {
      return {status:503,data:null};
    }
    if (rootUrl.username || rootUrl.password || rootUrl.search || rootUrl.hash) return {status:503,data:null};
    if (!endpointPath.startsWith('/') || endpointPath.startsWith('//')) return {status:503,data:null};
    const url=new URL(endpointPath,rootUrl);
    if (url.origin !== rootUrl.origin) return {status:503,data:null};
    const response=await fetch(url,{
      method:options.method ?? 'GET',
      cache:'no-store',
      signal:AbortSignal.timeout(12000),
      headers:{
        'Accept':'application/json',
        ...(options.body !== undefined ? {'Content-Type':'application/json'} : {}),
        ...(options.token ? {Authorization:'Bearer '+options.token} : {}),
      },
      body:options.body !== undefined?JSON.stringify(options.body):undefined,
      redirect:'manual',
    });
    if (response.status>=300 && response.status<400) return {status:502,data:null};
    if (!response.ok) return {status:response.status,data:null};
    const data=await response.json().catch(()=>null);
    return {status:response.status,data};
  } catch { return {status:503,data:null}; }
}
export async function accessToken():Promise<string|null>{
  const cookieStore=await cookies();
  return cookieStore.get(COOKIE_NAME)?.value ?? null;
}
export function sessionAge(): number {
  const value=Number(process.env.PORTAL_SESSION_MAX_AGE_SECONDS ?? '3600');
  return Number.isFinite(value)?Math.min(3600,Math.max(300,Math.floor(value))):3600;
}
export function clearSession(response:NextResponse):NextResponse{
  response.cookies.set(COOKIE_NAME,'',{httpOnly:true,secure:process.env.NODE_ENV==='production',sameSite:'lax',path:'/',maxAge:0});
  return response;
}
