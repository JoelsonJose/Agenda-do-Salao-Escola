export default function StatusBadge({ status }) {
  const cancelado = status === 'CANCELADO'
  return (
    <span className={cancelado ? 'status-badge status-badge--muted' : 'status-badge'}>
      <span aria-hidden="true" className="status-badge__dot" />
      {cancelado ? 'Cancelado' : 'Confirmado'}
    </span>
  )
}
