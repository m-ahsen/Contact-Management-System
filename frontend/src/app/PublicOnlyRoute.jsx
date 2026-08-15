import { Navigate } from 'react-router-dom'
import { useAuth } from '../features/auth/AuthContext'

export default function PublicOnlyRoute({ children }) {
  const { isAuthenticated, loading } = useAuth()

  if (loading) {
    return (
      <main className="page">
        <p className="page__lead">Loading…</p>
      </main>
    )
  }

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />
  }

  return children
}
