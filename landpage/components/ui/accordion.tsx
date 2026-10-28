'use client';
import * as AccordionPrimitive from '@radix-ui/react-accordion';
import { ChevronDown } from 'lucide-react';
import { cn } from '@/lib/utils';
export const Accordion = AccordionPrimitive.Root;
export function AccordionItem({ className, ...props }: React.ComponentProps<typeof AccordionPrimitive.Item>) { return <AccordionPrimitive.Item className={cn('border-b border-slate-200', className)} {...props} />; }
export function AccordionTrigger({ className, children, ...props }: React.ComponentProps<typeof AccordionPrimitive.Trigger>) { return <AccordionPrimitive.Header><AccordionPrimitive.Trigger className={cn('flex w-full items-center justify-between gap-4 py-5 text-left font-semibold text-slate-900 hover:text-[#18328a] focus-visible:outline-2 focus-visible:outline-[#18328a] [&[data-state=open]>svg]:rotate-180', className)} {...props}>{children}<ChevronDown className="h-5 w-5 shrink-0 transition-transform" /></AccordionPrimitive.Trigger></AccordionPrimitive.Header>; }
export function AccordionContent({ className, children, ...props }: React.ComponentProps<typeof AccordionPrimitive.Content>) { return <AccordionPrimitive.Content className={cn('pb-5 text-slate-600', className)} {...props}>{children}</AccordionPrimitive.Content>; }
