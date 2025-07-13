let currentUser = null;

document.getElementById('loginForm').addEventListener('submit', async e => {
  e.preventDefault();

  const email = document.getElementById('email').value.trim();
  const password = document.getElementById('password').value.trim();

  try {
    const res = await fetch('/api/users/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });

    if (!res.ok) throw new Error('Identifiants invalides');

    currentUser = await res.json();

    // Redirection vers la page des salons après connexion réussie
    window.location.href = 'channels.html';
  } catch (err) {
    const errorBox = document.getElementById('loginError');
    errorBox.textContent = err.message;
    errorBox.style.display = 'block';
  }
});
