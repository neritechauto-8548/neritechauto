import * as React from 'react';
import { cva, type VariantProps } from 'class-variance-authority';
import { cn } from '@/lib/utils';

const buttonVariants = cva('inline-flex items-center justify-center gap-2 rounded-full text-sm font-semibold transition-colors focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-[#635BFF] disabled:pointer-events-none disabled:opacity-50', {
  variants: { variant: { default: 'bg-[#635BFF] text-white hover:bg-[#4D44D9]', outline: 'border border-[#635BFF] text-[#635BFF] hover:bg-[#EFEEFF]', light: 'bg-white text-[#635BFF] hover:bg-[#EFEEFF]' }, size: { default: 'min-h-11 px-6 py-2', lg: 'min-h-12 px-8 py-3', sm: 'min-h-9 px-4 py-1' } },
  defaultVariants: { variant: 'default', size: 'default' }
});

export function Button({ className, variant, size, ...props }: React.ComponentProps<'button'> & VariantProps<typeof buttonVariants>) {
  return <button className={cn(buttonVariants({ variant, size }), className)} {...props} />;
}
export { buttonVariants };
