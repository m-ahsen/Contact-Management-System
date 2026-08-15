import { useState } from 'react'
import { validateAuthForm } from '../authValidation'
import './AuthForm.css'

export default function RegisterForm({ onSubmit, submitting, serverError }) {
  const [email, setEmail] = useState('')
  const [phone, setPhone] = useState('')
  const [password, setPassword] = useState('')
  const [errors, setErrors] = useState({})

  async function handleSubmit(event) {
    event.preventDefault()
    const nextErrors = validateAuthForm({ email, phone, password })
    setErrors(nextErrors)
    if (Object.keys(nextErrors).length > 0) {
      return
    }

    await onSubmit({
      email: email.trim() || null,
      phone: phone.trim() || null,
      password,
    })
    setPassword('')
  }

  return (
    <form className="auth-form" onSubmit={handleSubmit} noValidate>
      {serverError && <p className="auth-form__error">{serverError}</p>}
      {errors.form && <p className="auth-form__error">{errors.form}</p>}

      <label className="auth-form__field">
        Email
        <input
          type="email"
          name="email"
          autoComplete="email"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
        />
        {errors.email && <span className="auth-form__field-error">{errors.email}</span>}
      </label>

      <label className="auth-form__field">
        Phone
        <input
          type="tel"
          name="phone"
          autoComplete="tel"
          value={phone}
          onChange={(event) => setPhone(event.target.value)}
        />
        {errors.phone && <span className="auth-form__field-error">{errors.phone}</span>}
      </label>

      <label className="auth-form__field">
        Password
        <input
          type="password"
          name="password"
          autoComplete="new-password"
          value={password}
          onChange={(event) => setPassword(event.target.value)}
        />
        {errors.password && <span className="auth-form__field-error">{errors.password}</span>}
      </label>

      <p className="auth-form__hint">Provide email or phone, plus a password with at least 8 characters, one letter, and one digit.</p>

      <button type="submit" disabled={submitting}>
        {submitting ? 'Creating account…' : 'Create account'}
      </button>
    </form>
  )
}
