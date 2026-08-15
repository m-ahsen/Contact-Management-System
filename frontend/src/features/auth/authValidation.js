const EMAIL_PATTERN = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
const PHONE_PATTERN = /^\+?[1-9]\d{9,14}$/
const PASSWORD_PATTERN = /^(?=.*[A-Za-z])(?=.*\d).{8,72}$/

export function isBlank(value) {
  return value == null || String(value).trim() === ''
}

export function validateEmailOrPhone({ email, phone }) {
  const errors = {}
  const hasEmail = !isBlank(email)
  const hasPhone = !isBlank(phone)

  if (!hasEmail && !hasPhone) {
    errors.form = 'Email or phone is required'
  }
  if (hasEmail && !EMAIL_PATTERN.test(email.trim())) {
    errors.email = 'Email must be valid'
  }
  if (hasPhone && !PHONE_PATTERN.test(phone.trim().replaceAll(' ', ''))) {
    errors.phone = 'Phone must be 10-15 digits and may start with +'
  }
  return errors
}

export function validatePassword(password, field = 'password') {
  const errors = {}
  if (isBlank(password) || !PASSWORD_PATTERN.test(password)) {
    errors[field] = 'Password must be 8-72 characters and include at least one letter and one digit'
  }
  return errors
}

export function validateAuthForm({ email, phone, password }) {
  return {
    ...validateEmailOrPhone({ email, phone }),
    ...validatePassword(password),
  }
}

export function validateChangePassword({ currentPassword, newPassword }) {
  const errors = {}
  if (isBlank(currentPassword)) {
    errors.currentPassword = 'Current password is required'
  }
  return {
    ...errors,
    ...validatePassword(newPassword, 'newPassword'),
  }
}
