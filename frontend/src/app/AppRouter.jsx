import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import HomePage from '../pages/HomePage'
import HealthPage from '../pages/HealthPage'

// BrowserRouter needs SPA history fallback for direct loads of /health.
// Covered by vite appType: 'spa', frontend/public/_redirects, and frontend/vercel.json.
export default function AppRouter() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/health" element={<HealthPage />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
