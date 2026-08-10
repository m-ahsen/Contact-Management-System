import { Component } from 'react'

export default class ErrorBoundary extends Component {
  constructor(props) {
    super(props)
    this.state = { hasError: false }
  }

  static getDerivedStateFromError() {
    return { hasError: true }
  }

  componentDidCatch(error, errorInfo) {
    // Single reporting path for errors caught by this boundary (do not also log in createRoot.onCaughtError).
    console.error('Caught render error:', error, errorInfo)
  }

  render() {
    if (this.state.hasError) {
      return (
        <main style={{ padding: '2rem', fontFamily: 'system-ui, sans-serif' }}>
          <h1>Something went wrong</h1>
          <p>Please refresh the page. If the problem continues, contact support.</p>
        </main>
      )
    }

    return this.props.children
  }
}
