import type { Metadata } from 'next';
import { ReplicaPage } from '@/components/replica-page';
export const metadata:Metadata={title:{absolute:"E-mail de boas-vindas | Demonstração"},robots:{index:false,follow:false}};
export default function Page(){return <ReplicaPage page="email"/>;}
