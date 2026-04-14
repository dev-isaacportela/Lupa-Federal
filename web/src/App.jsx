import { useEffect, useState, useCallback } from 'react'
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer,
  PieChart, Pie, Cell, Legend
} from 'recharts'
import { fetchDespesas, fetchFornecedores, fetchCategorias, fetchOrgaos, fetchAgentes } from './api'

const COLORS = ['#6366f1', '#22d3ee', '#f59e0b', '#10b981', '#f43f5e', '#a78bfa', '#34d399', '#fb923c']

const fmt     = (v) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL', notation: 'compact' }).format(v)
const fmtFull = (v) => new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(v)

function Card({ label, value }) {
  return (
    <div className="card">
      <span className="card-label">{label}</span>
      <span className="card-value">{value}</span>
    </div>
  )
}

export default function App() {
  const [despesas,    setDespesas]    = useState([])
  const [fornecedores, setFornecedores] = useState([])
  const [categorias,  setCategorias]  = useState([])
  const [orgaos,      setOrgaos]      = useState([])
  const [agentes,     setAgentes]     = useState([])
  const [loading,     setLoading]     = useState(false)
  const [erro,        setErro]        = useState(null)

  const [filtros, setFiltros] = useState({
    dataInicio: '',
    dataFim: '',
    idCategoriaDespesa: '',
    idOrgao: '',
  })

  const carregarReferencias = useCallback(async () => {
    const [fornList, catList, orgList, agList] = await Promise.all([
      fetchFornecedores(), fetchCategorias(), fetchOrgaos(), fetchAgentes()
    ])
    setFornecedores(Array.isArray(fornList) ? fornList : fornList.content ?? [])
    setCategorias(Array.isArray(catList)    ? catList  : catList.content  ?? [])
    setOrgaos(Array.isArray(orgList)        ? orgList  : orgList.content  ?? [])
    setAgentes(Array.isArray(agList)        ? agList   : agList.content   ?? [])
  }, [])

  const buscar = useCallback(async () => {
    setLoading(true)
    setErro(null)
    try {
      const data = await fetchDespesas(filtros)
      setDespesas(data.content ?? [])
    } catch (e) {
      setErro(e.message)
    } finally {
      setLoading(false)
    }
  }, [filtros])

  useEffect(() => { carregarReferencias() }, [carregarReferencias])

  const fornMap   = Object.fromEntries(fornecedores.map(f => [f.id, f.razaoSocial]))
  const catMap    = Object.fromEntries(categorias.map(c => [c.id, c.descricao]))
  const orgMap    = Object.fromEntries(orgaos.map(o => [o.id, o.nome]))
  const agenteMap = Object.fromEntries(agentes.map(a => [a.id, a.idOrgao]))

  const despesasFiltradas = filtros.idOrgao
    ? despesas.filter(d => agenteMap[d.idAgente] === Number(filtros.idOrgao))
    : despesas

  const totalValor = despesasFiltradas.reduce((s, d) => s + Number(d.valor), 0)

  const porMes = Object.values(
    despesasFiltradas.reduce((acc, d) => {
      const mes = d.dataEmissao?.slice(0, 7) ?? 'N/A'
      acc[mes] = acc[mes] ?? { mes, valor: 0 }
      acc[mes].valor += Number(d.valor)
      return acc
    }, {})
  ).sort((a, b) => a.mes.localeCompare(b.mes))

  const topFornecedores = Object.values(
    despesasFiltradas.reduce((acc, d) => {
      const nome = fornMap[d.idFornecedor] ?? `ID ${d.idFornecedor}`
      acc[d.idFornecedor] = acc[d.idFornecedor] ?? { nome: nome.slice(0, 30), valor: 0 }
      acc[d.idFornecedor].valor += Number(d.valor)
      return acc
    }, {})
  ).sort((a, b) => b.valor - a.valor).slice(0, 10)

  const porCategoria = Object.values(
    despesasFiltradas.reduce((acc, d) => {
      const nome = catMap[d.idCategoriaDespesa] ?? `ID ${d.idCategoriaDespesa}`
      acc[d.idCategoriaDespesa] = acc[d.idCategoriaDespesa] ?? { nome: nome.slice(0, 35), valor: 0 }
      acc[d.idCategoriaDespesa].valor += Number(d.valor)
      return acc
    }, {})
  ).sort((a, b) => b.valor - a.valor)

  const porOrgao = Object.values(
    despesas.reduce((acc, d) => {
      const idOrgao = agenteMap[d.idAgente]
      const nome = idOrgao ? (orgMap[idOrgao] ?? `Órgão ${idOrgao}`).slice(0, 30) : 'Desconhecido'
      acc[idOrgao ?? 'unknown'] = acc[idOrgao ?? 'unknown'] ?? { nome, valor: 0 }
      acc[idOrgao ?? 'unknown'].valor += Number(d.valor)
      return acc
    }, {})
  ).sort((a, b) => b.valor - a.valor).slice(0, 10)

  return (
    <div className="layout">
      <header className="header">
        <h1>Lupa Federal</h1>
        <span>Transparência de gastos públicos federais</span>
      </header>

      <section className="filters">
        <label>
          Data início
          <input type="date" value={filtros.dataInicio}
            onChange={e => setFiltros(f => ({ ...f, dataInicio: e.target.value }))} />
        </label>
        <label>
          Data fim
          <input type="date" value={filtros.dataFim}
            onChange={e => setFiltros(f => ({ ...f, dataFim: e.target.value }))} />
        </label>
        <label>
          Categoria
          <select value={filtros.idCategoriaDespesa}
            onChange={e => setFiltros(f => ({ ...f, idCategoriaDespesa: e.target.value }))}>
            <option value="">Todas</option>
            {categorias.map(c => <option key={c.id} value={c.id}>{c.descricao}</option>)}
          </select>
        </label>
        <label>
          Órgão
          <select value={filtros.idOrgao}
            onChange={e => setFiltros(f => ({ ...f, idOrgao: e.target.value }))}>
            <option value="">Todos</option>
            {orgaos.map(o => <option key={o.id} value={o.id}>{o.nome}</option>)}
          </select>
        </label>
        <button onClick={buscar} disabled={loading}>
          {loading ? 'Buscando…' : 'Buscar'}
        </button>
      </section>

      {erro && <div className="erro">{erro}</div>}

      {despesasFiltradas.length > 0 && (
        <>
          <section className="cards">
            <Card label="Total de despesas"    value={despesasFiltradas.length.toLocaleString('pt-BR')} />
            <Card label="Valor total"          value={fmtFull(totalValor)} />
            <Card label="Fornecedores únicos"  value={new Set(despesasFiltradas.map(d => d.idFornecedor)).size} />
            <Card label="Categorias"           value={new Set(despesasFiltradas.map(d => d.idCategoriaDespesa)).size} />
          </section>

          <section className="charts">
            <div className="chart-box">
              <h2>Gastos por mês</h2>
              <ResponsiveContainer width="100%" height={260}>
                <BarChart data={porMes}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                  <XAxis dataKey="mes" tick={{ fill: '#94a3b8', fontSize: 12 }} />
                  <YAxis tickFormatter={fmt} tick={{ fill: '#94a3b8', fontSize: 11 }} width={80} />
                  <Tooltip formatter={fmtFull} contentStyle={{ background: '#1e293b', border: 'none' }} />
                  <Bar dataKey="valor" fill="#6366f1" radius={[4, 4, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>

            <div className="chart-box">
              <h2>Top 10 fornecedores</h2>
              <ResponsiveContainer width="100%" height={260}>
                <BarChart data={topFornecedores} layout="vertical">
                  <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                  <XAxis type="number" tickFormatter={fmt} tick={{ fill: '#94a3b8', fontSize: 11 }} />
                  <YAxis type="category" dataKey="nome" width={160} tick={{ fill: '#94a3b8', fontSize: 11 }} />
                  <Tooltip formatter={fmtFull} contentStyle={{ background: '#1e293b', border: 'none' }} />
                  <Bar dataKey="valor" fill="#22d3ee" radius={[0, 4, 4, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>

            <div className="chart-box">
              <h2>Top 10 órgãos por gasto</h2>
              <ResponsiveContainer width="100%" height={260}>
                <BarChart data={porOrgao} layout="vertical">
                  <CartesianGrid strokeDasharray="3 3" stroke="#334155" />
                  <XAxis type="number" tickFormatter={fmt} tick={{ fill: '#94a3b8', fontSize: 11 }} />
                  <YAxis type="category" dataKey="nome" width={160} tick={{ fill: '#94a3b8', fontSize: 11 }} />
                  <Tooltip formatter={fmtFull} contentStyle={{ background: '#1e293b', border: 'none' }} />
                  <Bar dataKey="valor" fill="#10b981" radius={[0, 4, 4, 0]} />
                </BarChart>
              </ResponsiveContainer>
            </div>

            <div className="chart-box">
              <h2>Por categoria</h2>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie data={porCategoria} dataKey="valor" nameKey="nome" cx="50%" cy="50%"
                    outerRadius={100} label={({ percent }) => `${(percent * 100).toFixed(0)}%`}
                    labelLine={false}>
                    {porCategoria.map((_, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}
                  </Pie>
                  <Tooltip formatter={fmtFull} contentStyle={{ background: '#1e293b', border: 'none' }} />
                  <Legend formatter={(v) => <span style={{ color: '#94a3b8', fontSize: 12 }}>{v}</span>} />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </section>
        </>
      )}

      {!loading && despesasFiltradas.length === 0 && !erro && (
        <div className="empty">Aplique os filtros e clique em Buscar para visualizar os dados.</div>
      )}
    </div>
  )
}
