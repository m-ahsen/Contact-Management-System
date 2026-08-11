import { Link } from 'react-router-dom'
import HealthStatus from '../features/health/HealthStatus'

export default function HealthPage() {
  return (
    <main className="page">
      <h1>Health</h1>
      <p className="page__lead">
        Direct view of <code>GET /api/v1/health</code>.
      </p>
      <HealthStatus />
      <p className="page__nav">
        <Link to="/">Back to home</Link>
      </p>
    </main>
  )
}
