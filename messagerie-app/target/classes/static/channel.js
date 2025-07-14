window.stompClient = null;
window.currentChannelId = null;
window.currentUser = { id: 1, username: "demoUser" }; // À remplacer dynamiquement selon ta logique serveur

let currentSubscription = null;

async function loadChannels() {
  try {
    const res = await fetch('/channels', {
      credentials: 'include'
    });
    if (!res.ok) throw new Error('Erreur chargement salons');
    const channels = await res.json();

    const select = document.getElementById('channelSelect');
    select.innerHTML = '';

    channels.forEach(channel => {
      const option = document.createElement('option');
      option.value = channel.id;
      option.textContent = channel.name;
      select.appendChild(option);
    });

    if (channels.length > 0) {
      window.currentChannelId = channels[0].id;
      loadPreviousMessages(window.currentChannelId);
      subscribeToCurrentChannel();
    }
  } catch (error) {
    console.error(error);
  }
}

async function loadPreviousMessages(channelId) {
  try {
    const res = await fetch(`/messages/channel/${channelId}`, {
      credentials: 'include'
    });
    const messages = await res.json();
    const container = document.getElementById('messages');
    container.innerHTML = '';
    messages.forEach(m => addMessage(`${m.senderUsername || 'Anonyme'}: ${m.content}`));
  } catch (err) {
    console.error(err);
  }
}

function addMessage(text) {
  const div = document.createElement('div');
  div.textContent = text;
  const container = document.getElementById('messages');
  container.appendChild(div);
  container.scrollTop = container.scrollHeight;
}

function initChat() {
  const socket = new SockJS('/ws');
  window.stompClient = Stomp.over(socket);
  window.stompClient.connect({}, () => {
    console.log("Connecté à WebSocket");
    subscribeToCurrentChannel();
  });
}

function subscribeToCurrentChannel() {
  if (currentSubscription) {
    currentSubscription.unsubscribe();
  }
  if (window.currentChannelId) {
    currentSubscription = window.stompClient.subscribe(`/topic/channel/${window.currentChannelId}`, message => {
      const data = JSON.parse(message.body);
      addMessage(`${data.sender.username}: ${data.content}`);
    });
  }
}

document.getElementById('channelSelect').addEventListener('change', e => {
  window.currentChannelId = parseInt(e.target.value);
  loadPreviousMessages(window.currentChannelId);
  if (window.stompClient && window.stompClient.connected) {
    subscribeToCurrentChannel();
  }
});

document.getElementById('publicForm').addEventListener('submit', async e => {
  e.preventDefault();
  const name = document.getElementById('publicChannelName').value.trim();

  if (!name) return;

  try {
    const res = await fetch('/channels', {
      credentials: 'include',
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, isPrivate: false })
    });

    if (!res.ok) throw new Error("Erreur lors de la création du salon");

    await loadChannels();
    document.getElementById('publicChannelName').value = '';
  } catch (err) {
    console.error(err);
    alert(err.message);
  }
});

document.getElementById('sendForm').addEventListener('submit', e => {
  e.preventDefault();
  const msg = document.getElementById('msgInput').value.trim();
  if (msg && window.currentUser && window.currentChannelId) {
    window.stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({
      content: msg,
      sender: { id: window.currentUser.id },
      channel: { id: parseInt(window.currentChannelId) }
    }));
    document.getElementById('msgInput').value = '';
  }
});

// Initialisation globale
window.addEventListener('DOMContentLoaded', () => {
  loadChannels();
  initChat();
});
