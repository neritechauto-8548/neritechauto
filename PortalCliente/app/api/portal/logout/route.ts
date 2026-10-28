import { NextRequest } from 'next/server';
import { clearSession,json,sameOrigin } from '@/lib/server';
export async function POST(req:NextRequest){
  if (!sameOrigin(req)) return json({message:'Requisição não autorizada.'},403);
  return clearSession(json({ok:true}));
}
