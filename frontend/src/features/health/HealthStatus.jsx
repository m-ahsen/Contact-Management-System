import { useEffect, useState } from 'react'
import { fetchHealth } from './healthApi'
import './HealthStatus.css'

export default function HealthStatus() {
  const [status, setStatus] = useState(null)
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    let cancelled = false

    async function loadHealth() {
      setLoading(true)
      setError(null)

      try {
        const data = await fetchHealth()
        if (!cancelled) {
          setStatus(data.status)
        }
      } catch (err) {
        if (!cancelled) {
          setStatus(null)
          setError(err instanceof Error ? err.message : 'Backend request failed')
        }
      } finally {
        if (!cancelled) {
          setLoading(false)
        }
      }
    }

    loadHealth()

    return () => {
      cancelled = true
    }
  }, [])

  return (
    <section className="health-status" aria-live="polite">
      <h2>Backend health</h2>
      {loading && <p className="health-status__muted">Checking API…</p>}
      {!loading && status && (
        <p className="health-status__ok">
          Status: <strong>{status}</strong>
        </p>
      )}
      {!loading && error && (
        <p className="health-status__error">
          Backend request failed: {error}
        </p>
      )}
    </section>
  )
}
