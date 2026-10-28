import { NextRequest } from 'next/server';
import { endpoint, json, requestBackend, safeError, sameOrigin } from '@/lib/server';
import { isValidCpf, normalizeCpf, normalizeOrder } from '@/lib/format';

export async function POST(req: NextRequest) {
  if (!sameOrigin(req)) return json({message:'Requisição não autorizada.'},403);
  const path=endpoint('PORTAL_PUBLIC_LOOKUP_ENDPOINT');
  if (process.env.PORTAL_PUBLIC_LOOKUP_ENABLED !== 'true' || !path) {
    return json({message:'A consulta rápida ainda não foi habilitada pela oficina. Solicite um acesso seguro.'},503);
  }
  const body=await req.json().catch(()=>null);
  const cpf=typeof body?.cpf==='string'?normalizeCpf(body.cpf):'';
  const numeroOs=typeof body?.numeroOs==='string'?body.numeroOs.trim():'';
  const codigoAcesso=typeof body?.codigoAcesso==='string'?body.codigoAcesso.trim():'';
  if (!isValidCpf(cpf) || !/^[\w-]{1,30}$/.test(numeroOs) || !/^[\w-]{6,80}$/.test(codigoAcesso)) {
    return json({message:'Confira CPF, número da OS e código de acesso.'},422);
  }
  const r=await requestBackend(path,{method:'POST',body:{cpf,numeroOs,codigoAcesso}});
  if (r.status!==200) return json({message:safeError(r.status)},r.status);
  const order=normalizeOrder(r.data);
  if (!order) return json({message:'Resposta inesperada da oficina.'},502);
  // Consulta reduzida: jamais entregar itens, fotos, orçamento, dados privados ou capacidades.
  return json({id:order.id, numeroOs:order.numeroOs,status:order.status,
    statusNome:order.statusNome, veiculo:order.veiculo, placa:order.placa,
    dataEntrada:order.dataEntrada,previsaoEntrega:order.previsaoEntrega});
}
