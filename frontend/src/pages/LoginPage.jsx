import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import LoginForm from '../features/auth/components/LoginForm'
import { useAuth } from '../features/auth/AuthContext'

export default function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const [submitting, setSubmitting] = useState(false)
  const [serverError, setServerError] = useState(null)
  const registered = Boolean(location.state?.registered)

  async function handleSubmit(credentials) {
    setSubmitting(true)
    setServerError(null)
    try {
      await login(credentials)
      navigate('/dashboard', { replace: true })
    } catch (error) {
      setServerError(error instanceof Error ? error.message : 'Unable to sign in')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <main className="page">
      <h1>Sign in</h1>
      <p className="page__lead">Use your email or phone and password.</p>
      {registered && <p className="auth-form__ok">Account created. Sign in to continue.</p>}
      <LoginForm onSubmit={handleSubmit} submitting={submitting} serverError={serverError} />
      <p className="page__nav">
        Need an account? <Link to="/register">Register</Link>
      </p>
    </main>
  )
}
