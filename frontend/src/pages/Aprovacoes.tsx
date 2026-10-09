import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { ArrowUpRight, ClipboardCheck, Clock3 } from 'lucide-react'
import { api } from '../lib/api'
import { dataHora, moeda } from '../lib/utils'
import type { PageProps } from '../App'
import { Empty, ErrorBox, Loading, PageHeader, Panel } from '../components/common'
import { Button } from '../components/ui/button'
import SolicitacaoDetail from '../components/SolicitacaoDetail'
export default function Aprovacoes({notify}:PageProps) {
  const query=useQuery({queryKey:['solicitacoes'],queryFn:api.solicitacoes})
  const [selected,setSelected]=useState<number|null>(null)
  const pendentes=query.data?.filter(s=>s.status==='PENDENTE')||[]
  return <><PageHeader eyebrow="Fluxo de autorização" title="Central de aprovações" description="Analise solicitações pendentes e registre suas decisões com rastreabilidade."/><div className="rounded-2xl bg-[#eaf5fb] border border-[#d7e9f3] p-5 mb-5 flex gap-4 items-center"><div className="size-11 bg-white rounded-xl flex items-center justify-center text-[#3289b5]"><ClipboardCheck size={22}/></div><div><div className="text-xl font-extrabold text-[#1a4c70]">{String(pendentes.length).padStart(2,'0')} solicitações pendentes</div><p className="text-xs mt-0.5 text-[#6388a3]">Somente pedidos enviados para aprovação aparecem aqui.</p></div></div><Panel>{query.isPending?<Loading/>:query.isError?<div className="p-6"><ErrorBox error={query.error}/></div>:pendentes.length===0?<Empty title="Tudo em dia" description="Não existem solicitações aguardando decisão no momento."/>:<div className="table-wrap"><table className="sgac-table"><thead><tr><th>Número</th><th>Solicitante</th><th>Fornecedor</th><th>Valor</th><th>Solicitado em</th><th>Decisão</th></tr></thead><tbody>{pendentes.map(s=><tr key={s.id}><td className="font-bold text-[#3276a1]">#{String(s.id).padStart(4,'0')}</td><td>{s.solicitante}</td><td>{s.fornecedor}</td><td className="font-extrabold whitespace-nowrap">{moeda(s.valorTotal)}</td><td className="text-[#8697a7]"><Clock3 size={12} className="inline mr-1"/>{dataHora(s.criadaEm)}</td><td><Button variant="secondary" size="sm" onClick={()=>setSelected(s.id)}>Analisar <ArrowUpRight size={14}/></Button></td></tr>)}</tbody></table></div>}</Panel>{selected!==null&&<SolicitacaoDetail id={selected} notify={notify} onClose={()=>setSelected(null)}/>}</>
}
