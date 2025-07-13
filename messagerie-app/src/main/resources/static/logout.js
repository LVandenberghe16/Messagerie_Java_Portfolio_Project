document.getElementById('logoutBtn').addEventListener('click', () => {
  // Exemple : vider le token ou localStorage selon votre logique
  localStorage.clear();
  window.location.href = 'index.html';
});
