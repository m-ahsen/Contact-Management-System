import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import ErrorBoundary from './app/ErrorBoundary.jsx'
import './features/auth/components/AuthForm.css'
import './features/profile/components/Profile.css'

createRoot(document.getElementById('root'), {
  // Caught render errors are reported once in ErrorBoundary.componentDidCatch.
  onUncaughtError: (error, errorInfo) => {
    console.error('Uncaught error:', error, errorInfo)
  },
}).render(
  <StrictMode>
    <ErrorBoundary>
      <App />
    </ErrorBoundary>
  </StrictMode>,
)
