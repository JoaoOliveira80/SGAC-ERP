import { useMemo, useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { ArrowUpRight, Plus, Search } from 'lucide-react'
import { Link } from 'react-router-dom'
import { api } from '../lib/api'
import { dataHora, moeda } from '../lib/utils'
import type { Status } from '../lib/types'
import type { PageProps } from '../App'
import { Empty, ErrorBox, Loading, PageHeader, Panel, Pill } from '../components/common'
import { Input, Select } from '../components/ui/fields'
import { Button } from '../components/ui/button'
import SolicitacaoDetail from '../components/SolicitacaoDetail'
export default function Solicitacoes({notify}:PageProps) {
  const query=useQuery({ queryKey:['solicitacoes'],queryFn:api.solicitacoes })
  const [search,setSearch]=useState('')
  const [status,setStatus]=useState<Status|'TODAS'>('TODAS')
  const [selected,setSelected]=useState<number|null>(null)
  const filtered=useMemo(()=>query.data?.filter(s=>(status==='TODAS'||s.status===status)&&`${s.id} ${s.solicitante} ${s.fornecedor} ${s.centroCusto}`.toLowerCase().includes(search.toLowerCase()))||[],[query.data,search,status])
  return <><PageHeader eyebrow="Operação de compras" title="Solicitações de aquisição" description="Gerencie pedidos, acompanhe o fluxo de aprovação e consulte o histórico." action={<Link to="/nova-solicitacao"><Button><Plus size={16}/> Nova solicitação</Button></Link>}/><Panel><div className="p-5 flex gap-3 flex-col sm:flex-row justify-between"><div className="relative w-full sm:max-w-[360px]"><Search className="absolute left-3 top-3 text-[#9eb0be]" size={16}/><Input aria-label="Pesquisar solicitações" className="pl-10" placeholder="Buscar por solicitante, fornecedor ou ID" value={search} onChange={e=>setSearch(e.target.value)}/></div><Select aria-label="Filtrar status" className="sm:w-48" value={status} onChange={e=>setStatus(e.target.value as Status|'TODAS')}><option value="TODAS">Todos os status</option><option value="RASCUNHO">Rascunhos</option><option value="PENDENTE">Pendentes</option><option value="APROVADA">Aprovadas</option><option value="REJEITADA">Rejeitadas</option></Select></div>{query.isPending?<Loading/>:query.isError?<div className="p-6"><ErrorBox error={query.error}/></div>:filtered.length===0?<Empty title="Nenhuma solicitação encontrada" description="Tente outros filtros ou crie uma solicitação."/>:<div className="table-wrap"><table className="sgac-table"><thead><tr><th>Protocolo</th><th>Solicitante</th><th>Fornecedor / Centro de custo</th><th>Valor</th><th>Status</th><th>Detalhes</th></tr></thead><tbody>{filtered.map(s=><tr key={s.id}><td><div className="font-extrabold text-[#235779]">#{String(s.id).padStart(4,'0')}</div><span className="text-[11px] text-[#9badba]">{dataHora(s.criadaEm)}</span></td><td className="font-semibold">{s.solicitante}</td><td><div className="font-medium">{s.fornecedor}</div><span className="text-[11px] text-[#9badba]">{s.centroCusto}</span></td><td className="whitespace-nowrap font-extrabold">{moeda(s.valorTotal)}</td><td><Pill status={s.status}/></td><td><Button size="sm" variant="ghost" onClick={()=>setSelected(s.id)}>Abrir <ArrowUpRight size={14}/></Button></td></tr>)}</tbody></table></div>}<div className="p-4 px-6 text-xs border-t border-[#edf1f5] text-[#96a6b5]">{filtered.length} registro(s)</div></Panel>{selected!==null&&<SolicitacaoDetail id={selected} onClose={()=>setSelected(null)} notify={notify}/>}</>
}
