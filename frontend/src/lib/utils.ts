import { clsx, type ClassValue } from 'clsx'
import { twMerge } from 'tailwind-merge'
import type { Status } from './types'
export function cn(...inputs: ClassValue[]) { return twMerge(clsx(inputs)) }
export const moeda = (value: number) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(Number(value || 0))
export const dataHora = (value: string) => value ? new Date(value).toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' }) : '—'
export const statusLabel: Record<Status, string> = { RASCUNHO: 'Rascunho', PENDENTE: 'Pendente', APROVADA: 'Aprovada', REJEITADA: 'Rejeitada' }
