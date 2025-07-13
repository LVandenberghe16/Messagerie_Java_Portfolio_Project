function openPrivateModal() {
  document.getElementById('privateModal').style.display = 'flex';
  displayPrivateUserList();
}

function closePrivateModal() {
  document.getElementById('privateModal').style.display = 'none';
}

function displayPrivateUserList() {
  const list = document.getElementById('privateUserList');
  const search = document.getElementById('privateSearch').value.toLowerCase();
  list.innerHTML = '';
  users
    .filter(u => u.id !== currentUser.id && u.username.toLowerCase().includes(search))
    .forEach(u => {
      const div = document.createElement('div');
      div.style = 'padding: 6px 0; border-bottom: 1px solid #eee; display: flex; justify-content: space-between;';
      div.innerHTML = `<span>${u.username}</span><button onclick="createPrivateWith('${u.username}')">Créer</button>`;
      list.appendChild(div);
    });
}

async function createPrivateWith(username2) {
  const username1 = currentUser.username;
  const msg = document.getElementById('privateCreateMsg');
  const res = await fetch(`/channels/private/${username1}/${username2}`);
  if (res.ok) {
    msg.textContent = "✅ Salon privé créé ou récupéré.";
    msg.style.color = 'green';
    closePrivateModal();
    await loadChannels();
  } else {
    msg.textContent = "❌ Erreur lors de la création.";
    msg.style.color = 'red';
  }
  setTimeout(() => msg.textContent = '', 3000);
}

document.getElementById('privateSearch').addEventListener('input', displayPrivateUserList);
