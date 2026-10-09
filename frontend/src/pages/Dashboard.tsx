import { useQuery } from '@tanstack/react-query'
import { ArrowRight, ArrowUpRight, CheckCircle2, Clock3, FileText, Plus, Wallet } from 'lucide-react'
import { Link } from 'react-router-dom'
import { api } from '../lib/api'
import { moeda, dataHora } from '../lib/utils'
import type { PageProps } from '../App'
import { PageHeader, Panel, Pill, ErrorBox, Loading, Empty } from '../components/common'
import { Button } from '../components/ui/button'
import type { Status } from '../lib/types'

export default function Dashboard(_props: PageProps) {
  const query = useQuery({ queryKey: ['solicitacoes'], queryFn: api.solicitacoes })
  if (query.isPending) return <><PageHeader eyebrow="Panorama operacional" title="Visão geral" description="Indicadores em tempo real do banco de dados."/><Loading/></>
  if (query.isError) return <><PageHeader title="Visão geral" description="Não foi possível consultar a API."/><ErrorBox error={query.error}/></>
  const list = query.data
  const pendentes = list.filter(s=>s.status==='PENDENTE')
  const aprovadas = list.filter(s=>s.status==='APROVADA')
  const cards = [
    {label:'Total de solicitações',value:String(list.length).padStart(2,'0'),icon:FileText,color:'text-[#3c8fba]',bg:'bg-[#e5f2fa]',note:'Todas as solicitações'},
    {label:'Aguardando aprovação',value:String(pendentes.length).padStart(2,'0'),icon:Clock3,color:'text-[#cb8c25]',bg:'bg-[#fff1da]',note:'Demandam atenção'},
    {label:'Solicitações aprovadas',value:String(aprovadas.length).padStart(2,'0'),icon:CheckCircle2,color:'text-[#3b9871]',bg:'bg-[#e2f5eb]',note:'Concluídas na análise'},
    {label:'Valor total aprovado',value:moeda(aprovadas.reduce((a,s)=>a+s.valorTotal,0)),icon:Wallet,color:'text-[#786fbb]',bg:'bg-[#eeeaff]',note:'Volume de aquisições'},
  ]
  const statuses: { status:Status; label:string; color:string }[] = [ {status:'PENDENTE',label:'Pendentes',color:'bg-[#f1b757]'}, {status:'APROVADA',label:'Aprovadas',color:'bg-[#52af87]'}, {status:'RASCUNHO',label:'Rascunhos',color:'bg-[#9bb0bf]'}, {status:'REJEITADA',label:'Rejeitadas',color:'bg-[#dd7881]'} ]
  return <div>
    <PageHeader eyebrow="Panorama operacional" title="Bom dia, bem-vindo ao SGAC" description="Acompanhe solicitações, aprovações e indicadores da organização." action={<Link to="/nova-solicitacao"><Button><Plus size={16}/> Nova solicitação</Button></Link>}/>
    <div className="grid sm:grid-cols-2 xl:grid-cols-4 gap-4 mb-7">{cards.map(card=><Panel key={card.label} className="p-5 sm:p-6"><div className="flex items-start justify-between gap-2 mb-6"><span className="text-[12px] font-semibold text-[#7b8d9e]">{card.label}</span><div className={`size-10 rounded-xl grid place-items-center ${card.bg} ${card.color}`}><card.icon size={19}/></div></div><div className="font-['Manrope'] text-[25px] font-extrabold tracking-[-.04em] text-[#162f45]">{card.value}</div><div className="text-[11px] text-[#9aabb8] mt-2">{card.note}</div></Panel>)}</div>
    <div className="grid xl:grid-cols-[minmax(0,1.7fr)_minmax(290px,1fr)] gap-5 mb-7"><Panel><div className="flex items-center justify-between px-6 pt-6 pb-3"><div><h2 className="font-extrabold text-[16px] text-[#213a50]">Solicitações recentes</h2><p className="text-xs text-[#9aa9b7] mt-1">Últimos registros do sistema</p></div><Link to="/solicitacoes" className="text-[#2678a8] text-xs font-bold flex gap-1 items-center">Ver todas <ArrowUpRight size={14}/></Link></div>{list.length===0?<Empty title="Nenhuma solicitação ainda" description="Crie sua primeira solicitação e acompanhe sua evolução aqui."/>:<div className="table-wrap"><table className="sgac-table"><thead><tr><th>Solicitação</th><th>Fornecedor</th><th>Status</th><th className="text-right">Valor</th></tr></thead><tbody>{list.slice(0,6).map(item=><tr key={item.id}><td><div className="font-bold text-[#2a4861]">#{String(item.id).padStart(4,'0')}</div><div className="text-[11px] mt-1 text-[#9aacb8]">{dataHora(item.criadaEm)}</div></td><td className="font-medium text-[#688093] max-w-[170px] truncate">{item.fornecedor}</td><td><Pill status={item.status}/></td><td className="font-bold text-right whitespace-nowrap text-[#2a445b]">{moeda(item.valorTotal)}</td></tr>)}</tbody></table></div>}</Panel>
      <Panel className="p-6"><h2 className="font-extrabold text-[16px] text-[#213a50]">Distribuição por status</h2><p className="text-xs text-[#9aa9b7] mt-1">Composição das solicitações</p><div className="mt-7 flex h-3 gap-[2px] overflow-hidden rounded-full bg-[#edf2f5]">{list.length>0?statuses.map(s=><div key={s.status} className={s.color} style={{width:`${(list.filter(x=>x.status===s.status).length/list.length)*100}%`}}/>):<div className="bg-[#e1e9ef] flex-1"/>}</div><div className="mt-8 space-y-5">{statuses.map(s=>{const count=list.filter(x=>x.status===s.status).length;return <div className="flex items-center justify-between text-xs" key={s.status}><div className="flex gap-2.5 items-center text-[#637a8d]"><span className={`size-2.5 rounded-full ${s.color}`}/>{s.label}</div><span className="font-extrabold text-[#30495f]">{count}<span className="text-[#b1bdc8] font-medium ml-3">{list.length?Math.round(count/list.length*100):0}%</span></span></div>})}</div><Link to="/aprovacoes" className="mt-8 flex items-center gap-2 bg-[#eef6fb] text-[#2477a2] rounded-xl px-4 py-3 font-bold text-xs">Ir para aprovações <ArrowRight size={15} className="ml-auto"/></Link></Panel></div>
    <div className="rounded-[18px] bg-gradient-to-r from-[#123a59] to-[#1f698c] px-7 py-6 flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between text-white"><div><div className="text-[11px] uppercase tracking-[.15em] text-[#a1d2eb] font-bold mb-2">Fluxo de suprimentos</div><h2 className="font-extrabold text-lg">Tudo pronto para uma nova aquisição?</h2><p className="text-sm mt-1 text-[#b9d7e7]">Registre itens, vincule fornecedores e acompanhe o processo completo.</p></div><Link to="/nova-solicitacao"><Button className="bg-white text-[#18638d] hover:bg-[#eaf4fa] whitespace-nowrap"><Plus size={16}/> Criar solicitação</Button></Link></div>
  </div>
}
