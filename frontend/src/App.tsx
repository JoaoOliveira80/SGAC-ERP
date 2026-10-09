import { useState } from 'react'
import { NavLink, Navigate, Route, Routes, useLocation } from 'react-router-dom'
import { Activity, Boxes, Building2, ChevronRight, CircleHelp, ClipboardList, FilePlus2, LayoutDashboard, Menu, ShieldCheck, Truck, X, CheckCircle2, AlertTriangle } from 'lucide-react'
import { Button } from './components/ui/button'
import Dashboard from './pages/Dashboard'
import Solicitacoes from './pages/Solicitacoes'
import Solicitar from './pages/Solicitar'
import Cadastros from './pages/Cadastros'
import Aprovacoes from './pages/Aprovacoes'

type Notify = (message: string, type?: 'ok' | 'error') => void
export type PageProps = { notify: Notify }
const links = [
  { title: 'Visão geral', items: [ { path: '/', icon: LayoutDashboard, label: 'Dashboard' }, { path: '/solicitacoes', icon: ClipboardList, label: 'Solicitações' }, { path: '/aprovacoes', icon: ShieldCheck, label: 'Aprovações' } ] },
  { title: 'Cadastros', items: [ { path: '/departamentos', icon: Building2, label: 'Departamentos' }, { path: '/centros-custo', icon: Boxes, label: 'Centros de custo' }, { path: '/fornecedores', icon: Truck, label: 'Fornecedores' } ] },
]
function Sidebar({ close }: { close: () => void }) {
  return <div className="h-full flex flex-col bg-[#112b43] text-white">
    <div className="h-[88px] border-b border-white/8 px-6 flex items-center gap-3"><div className="size-10 rounded-xl bg-[#2687bd] flex items-center justify-center font-bold tracking-tighter text-xl shadow-md shadow-blue-950/20">S<span className="text-[#b5e1f9]">.</span></div><div><div className="brand font-extrabold text-[18px] leading-5 tracking-[-.04em]">SGAC <span className="font-medium text-[#76b6d9] text-[12px]">ERP</span></div><div className="text-[10px] text-[#9ab2c5] tracking-[.055em] mt-1">Gestão de aquisições</div></div></div>
    <div className="flex-1 px-3 py-7 space-y-7 overflow-y-auto">{links.map(section => <div key={section.title}><div className="px-4 pb-3 text-[10px] font-extrabold uppercase tracking-[.17em] text-[#688ba6]">{section.title}</div><div className="space-y-1">{section.items.map(link=><NavLink key={link.path} to={link.path} end={link.path === '/'} onClick={close} className={({ isActive }) => `flex items-center gap-3 px-4 h-[44px] rounded-[10px] text-[13px] font-semibold transition ${isActive ? 'bg-[#20658d] text-white shadow-inner' : 'text-[#a8bfce] hover:bg-white/8 hover:text-white'}`}><link.icon size={17} strokeWidth={1.8}/><span>{link.label}</span>{link.path === '/aprovacoes' && <span className="ml-auto size-1.5 rounded-full bg-[#efc77c]"/>}</NavLink>)}</div></div>)}</div>
    <div className="px-4 pb-5"><div className="rounded-xl bg-[#203c53] border border-white/8 px-4 py-4"><div className="flex items-center gap-2 text-[12px] font-bold text-[#e6f3fc]"><Activity size={15} className="text-[#74c9ad]"/> Ambiente de demonstração</div><p className="text-[11px] leading-relaxed mt-2 text-[#a6bfd1]">Dados reais do SQL Server local, via API Spring Boot.</p></div><div className="text-[10px] text-[#7c9ab0] mt-4 px-2">SGAC © 2026 · Projeto demonstrativo</div></div>
  </div>
}
export default function App() {
  const [mobileMenu,setMobileMenu] = useState(false)
  const [notice,setNotice] = useState<{ message:string; type:'ok'|'error' } | null>(null)
  const location = useLocation()
  const notify: Notify = (message,type='ok') => { setNotice({ message,type }) }
  const crumbs: Record<string,string> = { '/': 'Dashboard', '/solicitacoes': 'Solicitações', '/nova-solicitacao': 'Nova solicitação', '/aprovacoes': 'Aprovações', '/departamentos': 'Departamentos', '/centros-custo': 'Centros de custo', '/fornecedores': 'Fornecedores' }
  return <div className="min-h-screen bg-[#f5f8fb] flex">
    <aside className="hidden lg:block fixed inset-y-0 left-0 w-[244px] z-20"><Sidebar close={()=>setMobileMenu(false)}/></aside>
    {mobileMenu && <div className="lg:hidden fixed inset-0 z-40"><button aria-label="Fechar navegação" className="absolute inset-0 bg-slate-950/45 w-full" onClick={()=>setMobileMenu(false)}/><aside className="absolute top-0 left-0 bottom-0 w-[260px]"><Sidebar close={()=>setMobileMenu(false)}/></aside></div>}
    <div className="lg:ml-[244px] min-w-0 flex-1 flex flex-col">
      <header className="h-[78px] flex items-center justify-between border-b border-[#e7eef3] bg-white/90 px-5 sm:px-9 shrink-0"><div className="flex items-center gap-3"><Button variant="ghost" size="icon" className="lg:hidden" onClick={()=>setMobileMenu(true)} aria-label="Abrir navegação"><Menu size={20}/></Button><div className="flex gap-2 items-center text-xs"><span className="text-[#94a5b3] hidden sm:inline">Workspace</span><ChevronRight size={14} className="text-[#b7c5d0] hidden sm:inline"/><span className="font-bold text-[#26475f]">{crumbs[location.pathname]||'SGAC'}</span></div></div><div className="flex items-center gap-4"><a href="http://localhost:8080/swagger-ui/index.html" target="_blank" rel="noreferrer" className="hidden sm:flex items-center gap-1.5 text-[#7a91a4] text-xs font-semibold hover:text-[#1674ad]"><CircleHelp size={16}/> API Swagger</a><div className="h-7 w-px bg-[#ebeff3] hidden sm:block"/><div className="flex items-center gap-2"><div className="size-9 rounded-full bg-[#dfedf8] text-[#2578ad] grid place-items-center font-bold text-xs">SG</div><div className="hidden sm:block leading-4"><div className="text-xs font-extrabold text-[#244158]">SGAC Admin</div><div className="text-[10px] text-[#9aabb8]">Ambiente local</div></div></div></div></header>
      <main className="flex-1 p-5 sm:p-8 lg:p-10 max-w-[1570px] w-full mx-auto"><Routes><Route path="/" element={<Dashboard notify={notify}/>}/><Route path="/solicitacoes" element={<Solicitacoes notify={notify}/>}/><Route path="/nova-solicitacao" element={<Solicitar notify={notify}/>}/><Route path="/aprovacoes" element={<Aprovacoes notify={notify}/>}/><Route path="/departamentos" element={<Cadastros kind="departamentos" notify={notify}/>}/><Route path="/centros-custo" element={<Cadastros kind="centros" notify={notify}/>}/><Route path="/fornecedores" element={<Cadastros kind="fornecedores" notify={notify}/>}/><Route path="*" element={<Navigate to="/" replace/>}/></Routes></main>
      <footer className="text-center sm:text-left border-t border-[#e7eef3] px-8 py-5 text-[11px] text-[#a0adba]">SGAC · Sistema Integrado de Gestão de Aquisições e Suprimentos <span className="mx-1.5">·</span> Interface demonstrativa</footer>
    </div>
    {notice && <div className={`fixed z-50 bottom-5 right-5 left-5 sm:left-auto sm:w-[380px] p-4 rounded-xl shadow-lg border flex gap-3 items-start ${notice.type==='ok'?'border-emerald-200 bg-white text-emerald-800':'border-red-200 bg-white text-red-800'}`} role="status">{notice.type==='ok'?<CheckCircle2 size={20}/>:<AlertTriangle size={20}/>}<div className="text-sm font-medium flex-1">{notice.message}</div><button aria-label="Fechar aviso" onClick={()=>setNotice(null)}><X size={17}/></button></div>}
    <NavLink to="/nova-solicitacao" className="fixed bottom-5 left-1/2 -translate-x-1/2 z-20 lg:hidden flex items-center gap-2 rounded-full bg-[#176f9d] px-4 py-3 shadow-lg text-white text-xs font-bold"><FilePlus2 size={16}/> Nova solicitação</NavLink>
  </div>
}
