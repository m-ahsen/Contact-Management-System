export default function ProfileDetails({ user }) {
  if (!user) {
    return <p className="page__lead">Unable to load profile.</p>
  }

  return (
    <section className="profile-card">
      <h2>Account</h2>
      <dl>
        <div>
          <dt>ID</dt>
          <dd>{user.id}</dd>
        </div>
        <div>
          <dt>Email</dt>
          <dd>{user.email || '—'}</dd>
        </div>
        <div>
          <dt>Phone</dt>
          <dd>{user.phone || '—'}</dd>
        </div>
      </dl>
    </section>
  )
}
