import { apiClient } from '../../shared/api/apiClient'

export function changePassword(payload) {
  return apiClient.put('/api/v1/users/password', payload)
}
