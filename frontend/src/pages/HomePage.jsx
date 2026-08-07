import { Link } from 'react-router-dom';
import HealthStatus from '../features/health/HealthStatus';

export default function HomePage() {
  return (
    <main className="page">
      <h1>Contact Management System</h1>
      <p className="page__lead">
        Phase 1 foundation — verifying frontend ↔ backend communication.
      </p>
      <HealthStatus />
      <p className="page__nav">
        <Link to="/health">Open health page</Link>
      </p>
    </main>
  );
}
