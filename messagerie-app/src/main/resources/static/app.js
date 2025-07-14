'use strict';

let stompClient = null;
let currentSubscription = null;
let currentChannelId = null;

// Exemple minimal de login-front
/*
async function doLogin(email, password) {
  const res = await fetch('/users/login', {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  if (!res.ok) throw new Error(`Login failed (${res.status})`);
  const user = await res.json();
  console.log('🔐 Logged in as', user.username);
  // maintenant la session est en place côté serveur, on peut appeler /users/me
  return loadCurrentUser();
}*/

// puis, par exemple au chargement de la page :
document.addEventListener('DOMContentLoaded', async () => {
  try {
    await doLogin('mon@mail.com', 'monPassword'); 
    // ou bien affichez un vrai formulaire de login
    await loadChannels();
    connectWebSocket();
  } catch (e) {
    console.error(e);
    // afficher un message d’erreur de login / rediriger sur login.html, etc.
  }
});

// 1) Récupère l’utilisateur connecté côté back
async function loadCurrentUser() {
  try {
    const res = await fetch('/users/me', {
      credentials: 'include'
    });
    console.log('GET /users/me →', res.status);
    if (!res.ok) throw new Error(`Impossible de récupérer l'utilisateur (${res.status})`);
    const user = await res.json();
    window.currentUser = user;
    document.getElementById('userLabel').textContent = user.username;
  } catch (err) {
    console.warn('Utilisateur non authentifié, on affiche “Invité”');
    document.getElementById('userLabel').textContent = 'Invité';
    // ex. redirection sur /login si 401
  }
}

// 2) Charge la liste des salons
async function loadChannels() {
  try {
    const res = await fetch('/channels', {
      credentials: 'include'
    });
    console.log('GET /channels →', res.status);
    if (!res.ok) throw new Error(`Erreur chargement salons (${res.status})`);
    const channels = await res.json();
    console.log('Salons reçus:', channels);
    const select = document.getElementById('channelSelect');
    select.innerHTML = ''; // reset
    channels.forEach(ch => {
      const opt = document.createElement('option');
      opt.value = ch.id;
      opt.textContent = ch.name;
      select.appendChild(opt);
    });
    if (channels.length) {
      changeChannel(channels[0].id);
    }
  } catch (e) {
    console.error('loadChannels error:', e);
  }
}

// 4) Change de salon : unsubscribe, charge anciens messages, puis subscribe
async function changeChannel(id) {
  currentChannelId = id;

  // Unsubscribe précédent
  if (currentSubscription) {
    try { currentSubscription.unsubscribe(); }
    catch (e) { console.warn('unsubscribe failed', e); }
  }

  // Vide la zone de messages
  const box = document.getElementById('messages');
  box.innerHTML = '';

  // Fetch anciens messages
  try {
    const res = await fetch(`/channels/${id}/messages`, {
      credentials: 'include'
    });
    console.log(`GET /channels/${id}/messages →`, res.status);
    if (!res.ok) throw new Error(`Erreur fetching messages (${res.status})`);
    const msgs = await res.json();
    msgs.forEach(m => addMessage(`${m.senderUsername}: ${m.content}`));
  } catch (e) {
    console.error('changeChannel - load messages:', e);
  }

  // (Re)subscribe en WebSocket
  if (stompClient && stompClient.connected) {
    currentSubscription = stompClient.subscribe(
      `/topic/channel/${id}`,
      msg => {
        const data = JSON.parse(msg.body);
        addMessage(`${data.sender.username}: ${data.content}`);
      },
      { id: `sub-${id}` }
    );
    console.log(`Abonné à /topic/channel/${id}`);
  }
}

// 5) Initialise la connexion STOMP over SockJS avec debug & reconnexion
function connectWebSocket() {
  console.log('→ connectWebSocket() called');
  console.log('window.SockJS =', window.SockJS);
  console.log('window.Stomp =', window.Stomp);

  const socket = new SockJS('/ws');
  socket.onopen    = () => console.log('SockJS: connection OPEN');
  socket.onclose   = () => console.log('SockJS: connection CLOSED');
  socket.onerror   = e => console.error('SockJS ERROR', e);

  stompClient = Stomp.over(socket);
  console.log('stompClient =', stompClient);
  stompClient.debug = msg => console.log('[STOMP]', msg);

  stompClient.connect(
    {},
    frame => {
      console.log('STOMP CONNECTED frame=', frame);
      document.getElementById('sendForm').style.display = 'flex';
      if (currentChannelId) {
        changeChannel(currentChannelId);
      }
    },
    error => {
      console.error('STOMP CONNECT ERROR:', error);
      setTimeout(connectWebSocket, 5000);
    }
  );
}

// 7) Ajoute un message à l'affichage
function addMessage(txt) {
  const div = document.createElement('div');
  div.className = 'message';
  div.textContent = txt;
  const box = document.getElementById('messages');
  box.appendChild(div);
  box.scrollTop = box.scrollHeight;
}

// 3) À l'initialisation du DOM
window.addEventListener('DOMContentLoaded', async () => {
  // 3.a) Charge user + affiche le username
  await loadCurrentUser();

  // 3.b) Cache le form d'envoi tant que WS pas connecté
  document.getElementById('sendForm').style.display = 'none';

  // 3.c) Charge les salons dans le <select>
  await loadChannels();

  // 3.d) Démarre la WebSocket
  connectWebSocket();

  // 3.e) Écoute le changement de salon
  document
    .getElementById('channelSelect')
    .addEventListener('change', e => changeChannel(+e.target.value));

  // 8) Gestion du formulaire “Créer un salon public”
  document
    .getElementById('publicForm')
    .addEventListener('submit', async e => {
      e.preventDefault();
      const input = document.getElementById('publicChannelName');
      const name = input.value.trim();
      if (!name) return;
      try {
        const res = await fetch('/channels', {
          method: 'POST',
          credentials: 'include',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ name, isPrivate: false })
        });
        console.log('POST /channels →', res.status);
        if (!res.ok) {
          const text = await res.text();
          console.error('Erreur création salon:', res.status, text);
          return;
        }
        input.value = '';
        await loadChannels();
      } catch (err) {
        console.error('fetch /channels failed:', err);
      }
    });

  // 6) Envoi de message via STOMP
  document
    .getElementById('sendForm')
    .addEventListener('submit', e => {
      e.preventDefault();
      const input = document.getElementById('msgInput');
      const content = input.value.trim();
      if (!content || !stompClient || !stompClient.connected) return;
      const payload = {
        content,
        senderId: window.currentUser.id,
        channelId: currentChannelId
      };
      stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(payload));
      input.value = '';
    });
});
