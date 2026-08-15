import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { clearAccessToken, getAccessToken, setAccessToken } from '../../shared/auth/tokenStorage'
import { fetchCurrentUser, loginUser } from './authService'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    let cancelled = false

    async function bootstrap() {
      if (!getAccessToken()) {
        if (!cancelled) {
          setLoading(false)
        }
        return
      }

      try {
        const user = await fetchCurrentUser()
        if (!cancelled) {
          setCurrentUser(user)
        }
      } catch {
        clearAccessToken()
        if (!cancelled) {
          setCurrentUser(null)
        }
      } finally {
        if (!cancelled) {
          setLoading(false)
        }
      }
    }

    bootstrap()
    return () => {
      cancelled = true
    }
  }, [])

  const login = useCallback(async (credentials) => {
    const response = await loginUser(credentials)
    setAccessToken(response.token)
    setCurrentUser(response.user)
    return response
  }, [])

  const logout = useCallback(() => {
    clearAccessToken()
    setCurrentUser(null)
  }, [])

  const value = useMemo(
    () => ({
      isAuthenticated: Boolean(currentUser),
      currentUser,
      login,
      logout,
      loading,
    }),
    [currentUser, loading, login, logout],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return context
}
