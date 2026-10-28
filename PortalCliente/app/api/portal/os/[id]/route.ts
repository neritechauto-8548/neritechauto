import { NextRequest } from 'next/server';
import { accessToken,clearSession,endpoint,json,requestBackend,safeError,validId } from '@/lib/server';
import { normalizeOrder } from '@/lib/format';
export async function GET(_req:NextRequest,ctx:{params:Promise<{id:string}>}){
  const {id}=await ctx.params;
  if(!validId(id)) return json({message:'Ordem inválida.'},400);
  const token=await accessToken();
  if(!token) return json({message:'Entre para visualizar sua ordem de serviço.'},401);
  const path=endpoint('PORTAL_CUSTOMER_DETAIL_ENDPOINT',id);
  if(!path) return json({message:'Os detalhes ainda não foram integrados à API de clientes.'},503);
  const r=await requestBackend(path,{token});
  if(r.status!==200){
    const out=json({message:safeError(r.status)},r.status);
    return r.status===401?clearSession(out):out;
  }
  const order=normalizeOrder(r.data);
  if(!order || String(order.id)!==id) return json({message:'A API retornou uma ordem inválida.'},502);
  return json(order);
}
