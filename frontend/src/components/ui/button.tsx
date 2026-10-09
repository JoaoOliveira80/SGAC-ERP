import type { ButtonHTMLAttributes } from 'react'
import { cva, type VariantProps } from 'class-variance-authority'
import { cn } from '../../lib/utils'

const buttonVariants = cva('inline-flex items-center justify-center gap-2 rounded-xl font-semibold transition-all duration-150 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-[#3882b7]', {
  variants: {
    variant: { primary: 'bg-[#1674ad] hover:bg-[#115d90] text-white shadow-sm shadow-blue-900/15', outline: 'border border-[#dbe5ec] bg-white text-[#1b3650] hover:bg-[#f5f9fc]', ghost: 'text-[#49627b] hover:bg-[#eaf3f9]', danger: 'bg-[#bd3f4b] text-white hover:bg-[#a33340]', secondary: 'bg-[#e8f2f9] text-[#18658f] hover:bg-[#d7eaf6]' },
    size: { md: 'h-10 px-4 text-sm', sm: 'h-9 px-3 text-xs', lg: 'h-11 px-5 text-sm', icon: 'h-9 w-9' },
  }, defaultVariants: { variant: 'primary', size: 'md' },
})
export type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & VariantProps<typeof buttonVariants>
export function Button({ className, variant, size, ...props }: ButtonProps) { return <button className={cn(buttonVariants({ variant, size }), className)} {...props} /> }
