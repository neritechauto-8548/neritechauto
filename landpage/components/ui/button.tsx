import * as React from 'react';
import { cva, type VariantProps } from 'class-variance-authority';
import { cn } from '@/lib/utils';

const buttonVariants = cva('inline-flex items-center justify-center gap-2 rounded-full text-sm font-semibold transition-colors focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-[#18328a] disabled:pointer-events-none disabled:opacity-50', {
  variants: { variant: { default: 'bg-[#18328a] text-white hover:bg-[#102469]', outline: 'border border-[#18328a] text-[#18328a] hover:bg-[#eef2ff]', light: 'bg-white text-[#18328a] hover:bg-[#eef2ff]' }, size: { default: 'min-h-11 px-6 py-2', lg: 'min-h-12 px-8 py-3', sm: 'min-h-9 px-4 py-1' } },
  defaultVariants: { variant: 'default', size: 'default' }
});

export function Button({ className, variant, size, ...props }: React.ComponentProps<'button'> & VariantProps<typeof buttonVariants>) {
  return <button className={cn(buttonVariants({ variant, size }), className)} {...props} />;
}
export { buttonVariants };
