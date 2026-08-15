import { Link } from 'react-router-dom'
import { useAuth } from '../features/auth/AuthContext'

export default function DashboardPage() {
  const { currentUser } = useAuth()
  const identity = currentUser?.email || currentUser?.phone || 'there'

  return (
    <main className="page">
      <h1>Dashboard</h1>
      <p className="page__lead">Welcome, {identity}. Contact management arrives in a later phase.</p>
      <p className="page__nav">
        <Link to="/profile">Open profile</Link>
      </p>
    </main>
  )
}
