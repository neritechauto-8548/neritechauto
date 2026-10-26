import Link from 'next/link';
import { Check } from 'lucide-react';
type Plan = { name: string; description: string; features: string[]; featured?: boolean };
const plans: Plan[] = [
  { name: 'NeriTech Pro', description: 'Organização da operação e visão de gestão para a rotina da oficina.', featured: true, features: ['Orçamentos e ordens de serviço', 'Clientes e veículos', 'Estoque e compras', 'Gestão financeira'] },
  { name: 'NeriTech Ultra', description: 'Mais recursos de gestão para operações com necessidades ampliadas.', features: ['Recursos do Pro', 'Condições e módulos adicionais sob consulta', 'Disponibilidade conforme contratação'] },
];
export function Pricing({ prices }: { prices?: Record<string,string> }) { return <div className="pricing-grid">{plans.map(plan=><article className={`card price-card ${plan.featured?'featured':''}`} key={plan.name}><span className="eyebrow">Plano comercial</span><h3>{plan.name}</h3><p>{plan.description}</p><div className="price">{prices?.[plan.name] || 'Consulte condições'}</div><p className="note">Recursos e valores finais dependem das condições comerciais vigentes.</p><ul className="feature-list">{plan.features.map(feature=><li key={feature}><Check size={18}/>{feature}</li>)}</ul><Link className="button" href="/teste-gratis">Conhecer teste grátis</Link></article>)}</div> }
