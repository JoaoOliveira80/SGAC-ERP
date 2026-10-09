import { useState } from 'react'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Pencil, Plus, Search } from 'lucide-react'
import { api } from '../lib/api'
import type { Departamento, Fornecedor, CentroCusto } from '../lib/types'
import type { PageProps } from '../App'
import { Empty, ErrorBox, Loading, PageHeader, Panel } from '../components/common'
import { Button } from '../components/ui/button'
import { Field, Input, Select } from '../components/ui/fields'

type Kind = 'departamentos'|'fornecedores'|'centros'
type Props = PageProps & { kind: Kind }
type Registro = Departamento|Fornecedor|CentroCusto
export default function Cadastros({kind,notify}:Props) {
  const qc=useQueryClient()
  const [showForm,setShowForm]=useState(false)
  const [editing,setEditing]=useState<Registro|null>(null)
  const [search,setSearch]=useState('')
  const [codigo,setCodigo]=useState('')
  const [nome,setNome]=useState('')
  const [email,setEmail]=useState('')
  const [deptId,setDeptId]=useState('')
  const query=useQuery<Registro[]>({
    queryKey:[kind],
    queryFn:async (): Promise<Registro[]> => {
      if(kind==='departamentos') return api.departamentos()
      if(kind==='fornecedores') return api.fornecedores()
      return api.centros()
    }
  })
  const deps=useQuery({queryKey:['departamentos'],queryFn:api.departamentos,enabled:kind==='centros'})
  const texts={departamentos:{title:'Departamentos',subtitle:'Unidades organizacionais e estruturas administrativas.',new:'Novo departamento',search:'Buscar departamento'},fornecedores:{title:'Fornecedores',subtitle:'Empresas parceiras e fornecedores homologados.',new:'Novo fornecedor',search:'Buscar fornecedor'},centros:{title:'Centros de custo',subtitle:'Organize custos e associe-os aos departamentos.',new:'Novo centro de custo',search:'Buscar centro de custo'}}[kind]
  const list: Registro[]=query.data||[]
  const found=list.filter(x=>JSON.stringify(x).toLowerCase().includes(search.toLowerCase()))
  const open=(item?:Registro)=>{setEditing(item||null);setShowForm(true);setCodigo(!item?'':kind==='fornecedores'?(item as Fornecedor).cnpj:(item as Departamento|CentroCusto).codigo);setNome(!item?'':kind==='fornecedores'?(item as Fornecedor).razaoSocial:(item as Departamento|CentroCusto).nome);setEmail(item&&kind==='fornecedores'?(item as Fornecedor).email||'':'');setDeptId(item&&kind==='centros'?String((item as CentroCusto).departamentoId):'')}
  const close=()=>{setShowForm(false);setEditing(null)}
  const invalidate=async()=>{await qc.invalidateQueries({queryKey:[kind]});if(kind==='centros')await qc.invalidateQueries({queryKey:['centros']});if(kind==='departamentos')await qc.invalidateQueries({queryKey:['departamentos']})}
  const save=useMutation<Registro,Error,void>({mutationFn:async (): Promise<Registro> => {
    const id=editing?.id
    if(kind==='departamentos'){const body={codigo:codigo.trim(),nome:nome.trim()};return id?api.editarDepartamento(id,body):api.criarDepartamento(body)}
    if(kind==='fornecedores'){const body={cnpj:codigo.replace(/\D/g,''),razaoSocial:nome.trim(),email:email.trim()};return id?api.editarFornecedor(id,body):api.criarFornecedor(body)}
    const body={codigo:codigo.trim(),nome:nome.trim(),departamentoId:Number(deptId)}
    return id?api.editarCentro(id,body):api.criarCentro(body)
  },onSuccess:async()=>{await invalidate();notify(editing?'Registro atualizado com sucesso!':'Cadastro realizado com sucesso!');close()},onError:e=>notify(e instanceof Error?e.message:'Não foi possível salvar','error')})
  const status=useMutation<Registro,Error,Registro>({mutationFn:async (item:Registro): Promise<Registro> => kind==='departamentos'?api.statusDepartamento(item.id,!item.ativo):kind==='fornecedores'?api.statusFornecedor(item.id,!item.ativo):api.statusCentro(item.id,!item.ativo),onSuccess:async()=>{await invalidate();notify('Status atualizado!')},onError:e=>notify(e instanceof Error?e.message:'Erro ao alterar status','error')})
  return <><PageHeader eyebrow="Estrutura administrativa" title={texts.title} description={texts.subtitle} action={<Button onClick={()=>open()}><Plus size={16}/>{texts.new}</Button>}/><Panel><div className="p-5"><div className="relative sm:max-w-[350px]"><Search size={16} className="absolute left-3 top-3 text-[#9eb0be]"/><Input aria-label={texts.search} placeholder={texts.search} value={search} onChange={e=>setSearch(e.target.value)} className="pl-10"/></div></div>{query.isPending?<Loading/>:query.isError?<div className="p-6"><ErrorBox error={query.error}/></div>:found.length===0?<Empty title="Nenhum registro encontrado" description="Cadastre um novo registro ou altere sua pesquisa."/>:<div className="table-wrap"><table className="sgac-table"><thead><tr><th>{kind==='fornecedores'?'CNPJ':'Código'}</th><th>{kind==='fornecedores'?'Razão social':'Nome'}</th><th>{kind==='centros'?'Departamento':kind==='fornecedores'?'E-mail':'Situação'}</th><th>Status</th><th>Ações</th></tr></thead><tbody>{found.map(item=><tr key={item.id}><td className="font-bold text-[#427d9f]">{kind==='fornecedores'?(item as Fornecedor).cnpj:(item as Departamento|CentroCusto).codigo}</td><td className="font-bold text-[#2e4b63]">{kind==='fornecedores'?(item as Fornecedor).razaoSocial:(item as Departamento|CentroCusto).nome}</td><td className="text-[#8297a7]">{kind==='fornecedores'?(item as Fornecedor).email||'—':kind==='centros'?(item as CentroCusto).departamentoNome:'Estrutura organizacional'}</td><td><span className={`text-[11px] px-3 py-1 rounded-full font-bold ${item.ativo?'bg-[#e2f5e9] text-[#277f53]':'bg-[#f1f3f5] text-[#768b9b]'}`}>{item.ativo?'Ativo':'Inativo'}</span></td><td><div className="flex gap-2"><Button size="sm" variant="ghost" onClick={()=>open(item)}><Pencil size={14}/> Editar</Button><Button size="sm" variant="outline" disabled={status.isPending} onClick={()=>status.mutate(item)}>{item.ativo?'Desativar':'Ativar'}</Button></div></td></tr>)}</tbody></table></div>}<div className="p-4 px-6 text-xs border-t border-[#edf1f5] text-[#96a6b5]">{found.length} registro(s)</div></Panel>
  {showForm&&<div className="fixed inset-0 z-40 grid place-items-center px-3"><button className="absolute inset-0 bg-[#102f45]/60" aria-label="Fechar formulário" onClick={close}/><form onSubmit={e=>{e.preventDefault();save.mutate()}} role="dialog" aria-modal="true" aria-label={texts.new} className="relative z-10 bg-white w-full max-w-lg rounded-[20px] shadow-xl p-6 sm:p-7"><h2 className="font-extrabold text-xl text-[#22415a]">{editing?'Editar':'Adicionar'} {kind==='fornecedores'?'fornecedor':kind==='centros'?'centro de custo':'departamento'}</h2><p className="mt-1 mb-6 text-xs text-[#98a9b6]">Os dados serão salvos diretamente no SQL Server.</p><div className="space-y-4"><Field label={kind==='fornecedores'?'CNPJ (14 dígitos)':'Código'}><Input required minLength={kind==='fornecedores'?14:2} maxLength={kind==='fornecedores'?14:20} value={codigo} onChange={e=>setCodigo(e.target.value)} placeholder={kind==='fornecedores'?'00000000000000':'CC-ADM'}/></Field><Field label={kind==='fornecedores'?'Razão social':'Nome'}><Input required minLength={3} maxLength={kind==='fornecedores'?160:120} value={nome} onChange={e=>setNome(e.target.value)} placeholder="Preencha o nome"/></Field>{kind==='fornecedores'&&<Field label="E-mail"><Input type="email" maxLength={160} value={email} onChange={e=>setEmail(e.target.value)} placeholder="contato@exemplo.com"/></Field>}{kind==='centros'&&<Field label="Departamento vinculado"><Select value={deptId} required onChange={e=>setDeptId(e.target.value)}><option value="">Selecione um departamento</option>{deps.data?.filter(d=>d.ativo||d.id===Number(deptId)).map(d=><option key={d.id} value={d.id}>{d.codigo} — {d.nome}</option>)}</Select></Field>}</div><div className="flex justify-end gap-2 mt-7"><Button type="button" variant="outline" onClick={close}>Cancelar</Button><Button type="submit" disabled={save.isPending}>{save.isPending?'Salvando...':'Salvar registro'}</Button></div></form></div>}
  </>
}
