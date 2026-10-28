import { NextRequest } from 'next/server';
import { accessToken,clearSession,endpoint,json,requestBackend,safeError,sameOrigin,validId } from '@/lib/server';
export async function POST(req:NextRequest,ctx:{params:Promise<{id:string}>}){
  if(!sameOrigin(req)) return json({message:'Requisição não autorizada.'},403);
  const {id}=await ctx.params;
  if(!validId(id)) return json({message:'Ordem inválida.'},400);
  const token=await accessToken();
  if(!token) return json({message:'Entre para responder ao orçamento.'},401);
  const body=await req.json().catch(()=>null);
  const decisao=body?.decisao;
  const motivo=typeof body?.motivo==='string'?body.motivo.trim():'';
  if(decisao!=='APROVAR' && decisao!=='REVISAR')return json({message:'Decisão inválida.'},422);
  if(decisao==='REVISAR' && (motivo.length<5 || motivo.length>1000))return json({message:'Explique a revisão em 5 a 1000 caracteres.'},422);
  const envName=decisao==='APROVAR'?'PORTAL_CUSTOMER_APPROVE_ENDPOINT':'PORTAL_CUSTOMER_REVISION_ENDPOINT';
  const path=endpoint(envName,id);
  if(!path)return json({message:'A aprovação pelo portal ainda não está habilitada na oficina.'},503);
  const r=await requestBackend(path,{method:'POST',token,body:decisao==='REVISAR'?{motivo}:{}});
  if(r.status!==200 && r.status!==204){
    const out=json({message:safeError(r.status)},r.status);
    return r.status===401?clearSession(out):out;
  }
  return json({ok:true});
}
