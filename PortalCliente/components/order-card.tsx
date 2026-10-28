import Link from 'next/link';
import {CarFront,ChevronRight,CalendarDays} from 'lucide-react';
import {dateBr,money} from '@/lib/format';
import type {Order} from '@/lib/types';
import {StatusBadge} from './status-badge';
export function OrderCard({order}:{order:Order}){
  return <article className="order-card">
    <div className="order-card-top"><div className="order-card-icon"><CarFront size={22}/></div><StatusBadge status={order.status}/></div>
    <h3>{order.veiculo}</h3><p className="order-number">OS #{order.numeroOs} {order.placa&&'· '+order.placa}</p>
    <div className="order-metadata"><span><CalendarDays size={15}/> Entrada: {dateBr(order.dataEntrada)}</span>
    <span>Valor: {money(order.valorTotal)}</span></div>
    <Link className="order-link" href={'/os/'+order.id}>Acompanhar OS <ChevronRight size={17}/></Link>
  </article>;
}
