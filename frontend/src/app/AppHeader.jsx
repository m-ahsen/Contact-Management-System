import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../features/auth/AuthContext'

export default function AppHeader() {
  const { isAuthenticated, logout } = useAuth()
  const navigate = useNavigate()

  if (!isAuthenticated) {
    return null
  }

  function handleLogout() {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <header className="site-header">
      <nav>
        <Link to="/dashboard">Dashboard</Link>
        <Link to="/profile">Profile</Link>
        <Link to="/health">Health</Link>
      </nav>
      <button type="button" onClick={handleLogout}>
        Log out
      </button>
    </header>
  )
}
