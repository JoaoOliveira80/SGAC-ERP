import type { CentroCusto, Departamento, Fornecedor, Historico, NovaSolicitacao, Solicitacao, SolicitacaoResumo } from './types'

export class ApiError extends Error {
  constructor(public status: number, message: string) { super(message); this.name = 'ApiError' }
}
async function request<T>(path: string, init?: RequestInit): Promise<T> {
  let response: Response
  try { response = await fetch(`/api${path}`, { ...init, headers: { 'Content-Type': 'application/json', ...init?.headers } }) }
  catch { throw new ApiError(0, 'Não foi possível conectar ao backend. Verifique se o Spring Boot está ativo na porta 8080.') }
  if (!response.ok) {
    const error = await response.json().catch(() => ({})) as { message?: string; detail?: string; error?: string }
    const fallback: Record<number,string> = { 400: 'Confira os campos preenchidos.', 404: 'Registro não encontrado.', 409: 'Operação em conflito com os dados existentes.', 422: 'A operação viola uma regra de negócio.' }
    throw new ApiError(response.status, error.detail || error.message || fallback[response.status] || `Erro HTTP ${response.status}`)
  }
  if (response.status === 204) return undefined as T
  return response.json() as Promise<T>
}
const json = (value: unknown) => JSON.stringify(value)
export const api = {
  departamentos: () => request<Departamento[]>('/departamentos'),
  criarDepartamento: (body: { codigo: string; nome: string }) => request<Departamento>('/departamentos', { method: 'POST', body: json(body) }),
  editarDepartamento: (id: number, body: { codigo: string; nome: string }) => request<Departamento>(`/departamentos/${id}`, { method: 'PUT', body: json(body) }),
  statusDepartamento: (id: number, ativo: boolean) => request<Departamento>(`/departamentos/${id}/status`, { method: 'PATCH', body: json({ ativo }) }),
  fornecedores: () => request<Fornecedor[]>('/fornecedores'),
  criarFornecedor: (body: { cnpj: string; razaoSocial: string; email: string }) => request<Fornecedor>('/fornecedores', { method: 'POST', body: json(body) }),
  editarFornecedor: (id: number, body: { cnpj: string; razaoSocial: string; email: string }) => request<Fornecedor>(`/fornecedores/${id}`, { method: 'PUT', body: json(body) }),
  statusFornecedor: (id: number, ativo: boolean) => request<Fornecedor>(`/fornecedores/${id}/status`, { method: 'PATCH', body: json({ ativo }) }),
  centros: () => request<CentroCusto[]>('/centros-custo'),
  criarCentro: (body: { codigo: string; nome: string; departamentoId: number }) => request<CentroCusto>('/centros-custo', { method: 'POST', body: json(body) }),
  editarCentro: (id: number, body: { codigo: string; nome: string; departamentoId: number }) => request<CentroCusto>(`/centros-custo/${id}`, { method: 'PUT', body: json(body) }),
  statusCentro: (id: number, ativo: boolean) => request<CentroCusto>(`/centros-custo/${id}/status`, { method: 'PATCH', body: json({ ativo }) }),
  solicitacoes: () => request<SolicitacaoResumo[]>('/solicitacoes'),
  solicitacao: (id: number) => request<Solicitacao>(`/solicitacoes/${id}`),
  historico: (id: number) => request<Historico[]>(`/solicitacoes/${id}/historico`),
  criarSolicitacao: (body: NovaSolicitacao) => request<Solicitacao>('/solicitacoes', { method: 'POST', body: json(body) }),
  enviar: (id: number) => request<Solicitacao>(`/solicitacoes/${id}/enviar`, { method: 'POST' }),
  aprovar: (id: number, responsavel: string, observacao: string) => request<Solicitacao>(`/solicitacoes/${id}/aprovar`, { method: 'POST', body: json({ responsavel, observacao }) }),
  rejeitar: (id: number, responsavel: string, motivo: string) => request<Solicitacao>(`/solicitacoes/${id}/rejeitar`, { method: 'POST', body: json({ responsavel, motivo }) }),
}
