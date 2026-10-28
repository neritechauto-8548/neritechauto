import {statusLabel} from '@/lib/format';
export function StatusBadge({status}:{status:string}){
  const type=status==='ENTREGUE'||status==='FINALIZADO'||status==='CONCLUIDA'?'good':
    status==='CANCELADO'||status==='CANCELADA'?'bad':
    status==='AGUARDANDO_APROVACAO'||status==='PENDENTE'?'warning':'info';
  return <span className={'status-badge status-'+type}><span className="status-dot"/>{statusLabel(status)}</span>;
}
