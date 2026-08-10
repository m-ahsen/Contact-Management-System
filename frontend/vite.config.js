import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// appType: 'spa' enables history-API fallback so direct navigation
// to client routes (e.g. /health) serves index.html in dev and preview.
export default defineConfig({
  plugins: [react()],
  appType: 'spa',
})
