import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../features/auth/AuthContext'
import { fetchCurrentUser } from '../features/auth/authService'
import { changePassword } from '../features/profile/profileApi'
import ProfileDetails from '../features/profile/components/ProfileDetails'
import ChangePasswordForm from '../features/profile/components/ChangePasswordForm'
import '../features/profile/components/Profile.css'

export default function ProfilePage() {
  const { logout } = useAuth()
  const navigate = useNavigate()
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)
  const [loadError, setLoadError] = useState(null)
  const [submitting, setSubmitting] = useState(false)
  const [serverError, setServerError] = useState(null)
  const [successMessage, setSuccessMessage] = useState(null)

  useEffect(() => {
    let cancelled = false

    async function loadProfile() {
      setLoading(true)
      setLoadError(null)
      try {
        const profile = await fetchCurrentUser()
        if (!cancelled) {
          setUser(profile)
        }
      } catch (error) {
        if (!cancelled) {
          setLoadError(error instanceof Error ? error.message : 'Unable to load profile')
        }
      } finally {
        if (!cancelled) {
          setLoading(false)
        }
      }
    }

    loadProfile()
    return () => {
      cancelled = true
    }
  }, [])

  async function handleChangePassword(payload) {
    setSubmitting(true)
    setServerError(null)
    setSuccessMessage(null)
    try {
      const response = await changePassword(payload)
      setSuccessMessage(response.message)
    } catch (error) {
      setServerError(error instanceof Error ? error.message : 'Unable to change password')
    } finally {
      setSubmitting(false)
    }
  }

  function handleLogout() {
    logout()
    navigate('/login', { replace: true })
  }

  return (
    <main className="page">
      <h1>Profile</h1>
      <p className="page__lead">Your account details are loaded from the server.</p>
      {loading && <p className="page__lead">Loading profile…</p>}
      {!loading && loadError && <p className="auth-form__error">{loadError}</p>}
      {!loading && !loadError && <ProfileDetails user={user} />}
      <ChangePasswordForm
        onSubmit={handleChangePassword}
        submitting={submitting}
        serverError={serverError}
        successMessage={successMessage}
      />
      <p className="page__nav">
        <button type="button" className="link-button" onClick={handleLogout}>
          Log out
        </button>
      </p>
    </main>
  )
}
