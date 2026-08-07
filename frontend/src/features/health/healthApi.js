import { apiClient } from '../../shared/api/apiClient';

export function fetchHealth() {
  return apiClient.get('/api/v1/health');
}
