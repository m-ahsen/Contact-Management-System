import { apiClient } from '../../shared/api/apiClient'

export function registerUser(payload) {
  return apiClient.post('/api/v1/auth/register', payload, { skipAuth: true })
}

export function loginUser(payload) {
  return apiClient.post('/api/v1/auth/login', payload, { skipAuth: true })
}

export function fetchCurrentUser() {
  return apiClient.get('/api/v1/users/me')
}
