import { Navigate } from 'react-router-dom'
import { useAuth } from '../features/auth/AuthContext'

export default function HomePage() {
  const { isAuthenticated, loading } = useAuth()

  if (loading) {
    return (
      <main className="page">
        <p className="page__lead">Loading…</p>
      </main>
    )
  }

  return <Navigate to={isAuthenticated ? '/dashboard' : '/login'} replace />
}
