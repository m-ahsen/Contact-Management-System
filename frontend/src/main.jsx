import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import ErrorBoundary from './app/ErrorBoundary.jsx'

createRoot(document.getElementById('root'), {
  onUncaughtError: (error, errorInfo) => {
    console.error('Uncaught error:', error, errorInfo)
  },
  onCaughtError: (error, errorInfo) => {
    console.error('Caught error:', error, errorInfo)
  },
}).render(
  <StrictMode>
    <ErrorBoundary>
      <App />
    </ErrorBoundary>
  </StrictMode>,
)
