document.getElementById('registerForm').addEventListener('submit', async e => {
  e.preventDefault();

  const username = document.getElementById('username').value.trim();
  const email = document.getElementById('registerEmail').value.trim();
  const password = document.getElementById('registerPassword').value.trim();

  try {
    const res = await fetch('/api/users/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, email, password })
    });

    if (!res.ok) {
      const errorText = await res.text();
      throw new Error(errorText || "Erreur lors de l'inscription");
    }

    alert('Inscription réussie ! Redirection vers la connexion...');
    window.location.href = 'index.html';
  } catch (err) {
    const errorBox = document.getElementById('registerError');
    errorBox.textContent = err.message;
    errorBox.style.display = 'block';
  }
});
