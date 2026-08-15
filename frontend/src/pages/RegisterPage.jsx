import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import RegisterForm from '../features/auth/components/RegisterForm'
import { registerUser } from '../features/auth/authService'

export default function RegisterPage() {
  const navigate = useNavigate()
  const [submitting, setSubmitting] = useState(false)
  const [serverError, setServerError] = useState(null)

  async function handleSubmit(payload) {
    setSubmitting(true)
    setServerError(null)
    try {
      await registerUser(payload)
      navigate('/login', { replace: true, state: { registered: true } })
    } catch (error) {
      setServerError(error instanceof Error ? error.message : 'Unable to register')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <main className="page">
      <h1>Create an account</h1>
      <p className="page__lead">Register with email or phone. You will sign in after registration.</p>
      <RegisterForm onSubmit={handleSubmit} submitting={submitting} serverError={serverError} />
      <p className="page__nav">
        Already have an account? <Link to="/login">Sign in</Link>
      </p>
    </main>
  )
}
