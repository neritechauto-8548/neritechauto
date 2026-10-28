import { accessToken, clearSession, endpoint, json,requestBackend,safeError } from '@/lib/server';
import { normalizeHistory } from '@/lib/format';
export async function GET(){
  const token=await accessToken();
  if(!token) return json({message:'Entre na sua conta para consultar o histórico.'},401);
  const path=endpoint('PORTAL_CUSTOMER_HISTORY_ENDPOINT');
  if(!path) return json({message:'O histórico ainda não foi integrado à API de clientes.'},503);
  const r=await requestBackend(path,{token});
  if(r.status!==200) {
    const out=json({message:safeError(r.status)},r.status);
    return r.status===401?clearSession(out):out;
  }
  return json(normalizeHistory(r.data));
}
