import {Brand} from './brand';
import {CarFront,ClipboardCheck,ShieldCheck} from 'lucide-react';
export function AuthLayout({children,eyebrow,title,description}:{children:React.ReactNode;eyebrow:string;title:string;description:string}){
  return <main className="auth-grid">
    <aside className="auth-image">
      <div className="auth-image-top"><Brand inverse/><span className="auth-image-caption">Acompanhamento de serviços</span></div>
      <div className="auth-image-copy">
        <div className="auth-image-symbol"><CarFront size={26}/></div>
        <p className="auth-image-kicker">Sua oficina mais perto de você</p>
        <h2>Seu carro em boas mãos.<br/>Você sempre informado.</h2>
        <p>Consulte o andamento do atendimento e acompanhe as informações disponibilizadas pela oficina.</p>
        <div className="auth-image-icons"><span><ShieldCheck size={17}/>Acesso protegido</span><span><ClipboardCheck size={17}/>Serviços organizados</span></div>
      </div>
    </aside>
    <section className="auth-main"><div className="auth-content">
      <div className="mobile-auth-brand"><Brand/></div>
      <p className="eyebrow">{eyebrow}</p>
      <h1>{title}</h1><p className="intro">{description}</p>
      {children}
      <p className="fineprint">NERITECH AUTO · As informações são disponibilizadas pela oficina responsável.</p>
    </div></section>
  </main>;
}
