import { useState } from 'react'
import { validateChangePassword } from '../../auth/authValidation'
import '../../auth/components/AuthForm.css'

const EMPTY_FORM = { currentPassword: '', newPassword: '' }

export default function ChangePasswordForm({ onSubmit, submitting, serverError, successMessage }) {
  const [form, setForm] = useState(EMPTY_FORM)
  const [errors, setErrors] = useState({})

  function updateField(event) {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  function resetForm() {
    setForm(EMPTY_FORM)
    setErrors({})
  }

  async function handleSubmit(event) {
    event.preventDefault()
    const nextErrors = validateChangePassword(form)
    setErrors(nextErrors)
    if (Object.keys(nextErrors).length > 0) {
      return
    }

    await onSubmit(form)
    setForm(EMPTY_FORM)
  }

  return (
    <form className="auth-form" onSubmit={handleSubmit} noValidate>
      <h2>Change password</h2>
      {successMessage && <p className="auth-form__ok">{successMessage}</p>}
      {serverError && <p className="auth-form__error">{serverError}</p>}

      <label className="auth-form__field">
        Current password
        <input
          type="password"
          name="currentPassword"
          autoComplete="current-password"
          value={form.currentPassword}
          onChange={updateField}
        />
        {errors.currentPassword && (
          <span className="auth-form__field-error">{errors.currentPassword}</span>
        )}
      </label>

      <label className="auth-form__field">
        New password
        <input
          type="password"
          name="newPassword"
          autoComplete="new-password"
          value={form.newPassword}
          onChange={updateField}
        />
        {errors.newPassword && <span className="auth-form__field-error">{errors.newPassword}</span>}
      </label>

      <div className="auth-form__actions">
        <button type="submit" disabled={submitting}>
          {submitting ? 'Saving…' : 'Save password'}
        </button>
        <button type="button" onClick={resetForm} disabled={submitting}>
          Reset
        </button>
        <button type="button" onClick={resetForm} disabled={submitting}>
          Cancel
        </button>
      </div>
    </form>
  )
}
