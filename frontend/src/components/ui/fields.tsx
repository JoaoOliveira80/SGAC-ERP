import type { InputHTMLAttributes, ReactNode, SelectHTMLAttributes, TextareaHTMLAttributes } from 'react'
import { cn } from '../../lib/utils'
const fieldClass = 'w-full min-h-10 rounded-xl border border-[#dce5ed] bg-white px-3 py-2.5 text-sm text-[#213b55] placeholder:text-[#9cabb9] outline-none transition focus:border-[#3294cf] focus:ring-2 focus:ring-[#3294cf]/12 disabled:bg-slate-100'
export function Field({ label, hint, children }: { label: string; hint?: string; children: ReactNode }) { return <label className="flex flex-col gap-1.5"><span className="text-xs font-semibold text-[#465e74]">{label}</span>{children}{hint && <span className="text-xs text-slate-500">{hint}</span>}</label> }
export function Input({ className, ...props }: InputHTMLAttributes<HTMLInputElement>) { return <input className={cn(fieldClass, className)} {...props}/> }
export function Textarea({ className, ...props }: TextareaHTMLAttributes<HTMLTextAreaElement>) { return <textarea className={cn(fieldClass, 'min-h-[90px] resize-y', className)} {...props}/> }
export function Select({ className, children, ...props }: SelectHTMLAttributes<HTMLSelectElement>) { return <select className={cn(fieldClass, className)} {...props}>{children}</select> }
