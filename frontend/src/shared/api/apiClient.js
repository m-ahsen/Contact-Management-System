import { getAccessToken } from '../auth/tokenStorage'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

export class ApiError extends Error {
  constructor(message, status, fieldErrors = []) {
    super(message)
    this.name = 'ApiError'
    this.status = status
    this.fieldErrors = fieldErrors
  }
}

async function request(path, options = {}) {
  const { headers: optionHeaders, body, skipAuth = false, ...rest } = options
  const token = skipAuth ? null : getAccessToken()

  const headers = {
    Accept: 'application/json',
    ...(body !== undefined ? { 'Content-Type': 'application/json' } : {}),
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...(optionHeaders || {}),
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...rest,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  })

  const text = await response.text()
  const payload = text ? parseJson(text) : null

  if (!response.ok) {
    const message = payload?.message || `Request failed with status ${response.status}`
    throw new ApiError(message, response.status, payload?.fieldErrors || [])
  }

  return payload
}

function parseJson(text) {
  try {
    return JSON.parse(text)
  } catch {
    return null
  }
}

export const apiClient = {
  get: (path, options) => request(path, { ...options, method: 'GET' }),
  post: (path, body, options) => request(path, { ...options, method: 'POST', body }),
  put: (path, body, options) => request(path, { ...options, method: 'PUT', body }),
}

export default apiClient
