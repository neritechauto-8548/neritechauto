import type { Metadata } from 'next';
import { ReplicaPage } from '@/components/replica-page';
export const metadata:Metadata={title:{absolute:"Onmotor | Sistema para oficinas"},robots:{index:false,follow:false}};
export default function Page(){return <ReplicaPage page="home"/>;}
