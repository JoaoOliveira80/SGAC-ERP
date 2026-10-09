import { useState } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Check, Clock3, FileText, Send, X } from 'lucide-react'
import { api } from '../lib/api'
import { dataHora, moeda } from '../lib/utils'
import { ErrorBox, Loading, Pill } from './common'
import { Button } from './ui/button'
import { Field, Input, Textarea } from './ui/fields'

type Props = { id: number; onClose: () => void; notify: (message: string, type?: 'ok' | 'error') => void }
export default function SolicitacaoDetail({ id, onClose, notify }: Props) {
  const qc = useQueryClient()
  const detail = useQuery({ queryKey: ['solicitacao', id], queryFn:()=>api.solicitacao(id) })
  const historico = useQuery({ queryKey: ['historico',id], queryFn:()=>api.historico(id) })
  const [acao,setAcao] = useState<'aprovar'|'rejeitar'|null>(null)
  const [responsavel,setResponsavel] = useState('')
  const [observacao,setObservacao] = useState('')
  const action = useMutation({ mutationFn: async (kind: 'enviar'|'aprovar'|'rejeitar') => kind === 'enviar' ? api.enviar(id) : kind === 'aprovar' ? api.aprovar(id, responsavel,observacao) : api.rejeitar(id, responsavel,observacao), onSuccess:async()=>{await Promise.all([qc.invalidateQueries({queryKey:['solicitacoes']}),qc.invalidateQueries({queryKey:['solicitacao',id]}),qc.invalidateQueries({queryKey:['historico',id]})]);setAcao(null);setObservacao('');notify('Status atualizado com sucesso!')}, onError:(e)=>notify(e instanceof Error ? e.message : 'Erro ao atualizar solicitação','error') })
  const solicitarAcao=(kind:'enviar'|'aprovar'|'rejeitar')=>{if(kind==='enviar'){action.mutate(kind);return} setAcao(kind)}
  const s=detail.data
  return <div className="fixed inset-0 z-40 flex items-center justify-center p-3 sm:p-6"><button aria-label="Fechar detalhes" className="absolute inset-0 bg-[#0b2337]/65 backdrop-blur-[2px]" onClick={onClose}/><section role="dialog" aria-modal="true" aria-label={`Solicitação ${id}`} className="relative w-full max-w-[760px] max-h-[90vh] overflow-y-auto rounded-[22px] bg-white shadow-2xl">
    <header className="sticky top-0 z-10 bg-white border-b border-[#e9eef2] px-6 py-5 flex justify-between items-center"><div><div className="text-[11px] font-extrabold tracking-[.12em] uppercase text-[#4090b8]">Detalhe da aquisição</div><h2 className="text-xl font-extrabold text-[#18354b] mt-1">Solicitação #{String(id).padStart(4,'0')}</h2></div><Button variant="ghost" size="icon" onClick={onClose} aria-label="Fechar"><X size={20}/></Button></header>
    <div className="p-6 space-y-6">{detail.isPending?<Loading/>:detail.isError?<ErrorBox error={detail.error}/>:s&&<>
      <div className="flex flex-wrap gap-3 items-center"><Pill status={s.status}/><span className="text-xs text-[#8a9ba9]">Criada em {dataHora(s.criadaEm)}</span></div>
      <div className="grid sm:grid-cols-3 gap-3">{[{label:'Valor total',value:moeda(s.valorTotal)},{label:'Solicitante',value:s.solicitante},{label:'Centro de custo',value:s.centroCusto}].map(x=><div key={x.label} className="rounded-xl border border-[#e7eff4] bg-[#f8fbfd] p-4"><p className="text-[11px] text-[#8b9fad] mb-2">{x.label}</p><p className="font-bold text-[#204057] text-[13px]">{x.value}</p></div>)}</div>
      <div><h3 className="text-xs uppercase tracking-[.10em] font-extrabold text-[#658096] mb-2">Dados da aquisição</h3><div className="text-sm text-[#38536b]">Fornecedor: <strong>{s.fornecedor}</strong></div><p className="mt-2 text-sm text-[#637c91]">{s.justificativa}</p></div>
      <div><h3 className="text-xs uppercase tracking-[.10em] font-extrabold text-[#658096] mb-3">Itens ({s.itens.length})</h3><div className="table-wrap rounded-xl border border-[#e7eef4]"><table className="sgac-table"><thead><tr><th>Descrição</th><th>Quantidade</th><th>Unitário</th><th>Subtotal</th></tr></thead><tbody>{s.itens.map(item=><tr key={item.id}><td className="font-semibold">{item.descricao}</td><td>{item.quantidade}</td><td>{moeda(item.valorUnitario)}</td><td className="font-bold">{moeda(item.subtotal)}</td></tr>)}</tbody></table></div></div>
      <div><h3 className="text-xs uppercase tracking-[.10em] font-extrabold text-[#658096] flex items-center gap-2 mb-3"><Clock3 size={15}/> Histórico de decisões</h3>{historico.isPending?<Loading/>:historico.isError?<ErrorBox error={historico.error}/>:<div className="space-y-0 border-l-2 border-[#dbe7ee] ml-2">{historico.data?.map(evt=><div key={evt.id} className="relative pl-5 pb-4 last:pb-0"><span className="absolute -left-[6px] top-1 size-2.5 rounded-full bg-[#318bb8] border-2 border-white"/><p className="text-sm font-bold text-[#31516a]">{evt.statusAnterior?`${evt.statusAnterior} → `:''}{evt.statusNovo}</p><p className="text-xs text-[#778d9c] mt-1">{evt.responsavel} · {dataHora(evt.registradoEm)}</p>{evt.observacao&&<p className="text-xs text-[#677f91] mt-1">{evt.observacao}</p>}</div>)}</div>}</div>
      {s.status==='RASCUNHO'&&<div className="pt-3 border-t border-[#edf1f5]"><Button disabled={action.isPending} onClick={()=>solicitarAcao('enviar')}><Send size={16}/> Enviar para aprovação</Button></div>}
      {s.status==='PENDENTE'&&<div className="border-t border-[#edf1f5] pt-5"><h3 className="text-sm font-extrabold mb-3">Decisão da solicitação</h3>{!acao?<div className="flex gap-2 flex-wrap"><Button onClick={()=>solicitarAcao('aprovar')}><Check size={16}/> Aprovar</Button><Button variant="danger" onClick={()=>solicitarAcao('rejeitar')}><X size={16}/> Rejeitar</Button></div>:<form className="space-y-3 bg-[#f6f9fc] p-4 rounded-xl" onSubmit={e=>{e.preventDefault();action.mutate(acao)}}><Field label="Responsável pela decisão"><Input maxLength={120} required value={responsavel} onChange={e=>setResponsavel(e.target.value)} placeholder="Seu nome"/></Field><Field label={acao==='rejeitar'?'Motivo da rejeição (mínimo 10 caracteres)':'Observação (opcional)'}><Textarea maxLength={500} required={acao==='rejeitar'} minLength={acao==='rejeitar'?10:undefined} value={observacao} onChange={e=>setObservacao(e.target.value)}/></Field><div className="flex gap-2"><Button type="submit" disabled={action.isPending}>{acao==='aprovar'?'Confirmar aprovação':'Confirmar rejeição'}</Button><Button type="button" variant="outline" onClick={()=>setAcao(null)}>Cancelar</Button></div></form>}</div>}
      {s.status!=='RASCUNHO' && s.status!=='PENDENTE' && <div className="flex items-center gap-2 text-sm text-[#8397a7] border-t pt-4"><FileText size={16}/> Solicitação finalizada. O histórico permanece disponível para consulta.</div>}
    </>}</div>
  </section></div>
}
