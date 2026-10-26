'use client';
import * as TabsPrimitive from '@radix-ui/react-tabs';
import { cn } from '@/lib/utils';
export const Tabs = TabsPrimitive.Root;
export function TabsList({ className, ...props }: React.ComponentProps<typeof TabsPrimitive.List>) { return <TabsPrimitive.List className={cn('flex flex-wrap gap-2', className)} {...props} />; }
export function TabsTrigger({ className, ...props }: React.ComponentProps<typeof TabsPrimitive.Trigger>) { return <TabsPrimitive.Trigger className={cn('rounded-full border border-slate-200 bg-white px-5 py-2.5 text-sm font-semibold text-slate-600 focus-visible:outline-2 focus-visible:outline-[#18328a] data-[state=active]:border-[#18328a] data-[state=active]:bg-[#18328a] data-[state=active]:text-white', className)} {...props} />; }
export function TabsContent({ className, ...props }: React.ComponentProps<typeof TabsPrimitive.Content>) { return <TabsPrimitive.Content className={cn('focus-visible:outline-2 focus-visible:outline-[#18328a]', className)} {...props} />; }
