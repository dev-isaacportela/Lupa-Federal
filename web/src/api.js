const BASE = `${import.meta.env.VITE_API_URL ?? ''}/api/v1`

async function get(path, params = {}) {
  const query = new URLSearchParams(params).toString()
  const res = await fetch(`${BASE}${path}${query ? '?' + query : ''}`)
  if (!res.ok) throw new Error(`Erro ${res.status} em ${path}`)
  return res.json()
}

export const fetchCategorias   = () => get('/categorias-despesas')
export const fetchFornecedores = () => get('/fornecedores')
export const fetchOrgaos       = () => get('/orgaos')
export const fetchAgentes      = () => get('/agentes')

export function fetchDespesas(filtros = {}) {
  const params = { size: 500, sort: 'dataEmissao,asc' }
  if (filtros.dataInicio)         params.dataInicio         = filtros.dataInicio
  if (filtros.dataFim)            params.dataFim            = filtros.dataFim
  if (filtros.idCategoriaDespesa) params.idCategoriaDespesa = filtros.idCategoriaDespesa
  return get('/despesas', params)
}
