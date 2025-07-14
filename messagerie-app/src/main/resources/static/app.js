'use strict';

let stompClient = null;
let currentSubscription = null;
let currentChannelId = null;

// ========== Auth/Démo ==========

async function doLogin(email, password) {
  const res = await fetch('/users/login', {
    method: 'POST',
    credentials: 'include',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  if (!res.ok) throw new Error(`Login failed (${res.status})`);
  // On suppose que session en cookie HTTP
  return loadCurrentUser();
}

// ========== User ==========

async function loadCurrentUser() {
  try {
    const res = await fetch('/users/me', { credentials: 'include' });
    if (!res.ok) throw new Error();
    const user = await res.json();
    window.currentUser = user;
    document.getElementById('userLabel').textContent = user.username;
  } catch {
    window.currentUser = { id:null, username: "Invité" };
    document.getElementById('userLabel').textContent = 'Invité';
  }
}

// ========== Channel ==========

async function loadChannels() {
  try {
    const res = await fetch('/channels', { credentials: 'include' });
    if (!res.ok) throw new Error();
    const channels = await res.json();
    const select = document.getElementById('channelSelect');
    select.innerHTML = '';
    channels.forEach(ch => {
      const opt = document.createElement('option');
      opt.value = ch.id;
      opt.textContent = ch.name;
      select.appendChild(opt);
    });
    if (channels.length && !currentChannelId) {
      currentChannelId = channels[0].id;
      select.value = currentChannelId;
      changeChannel(currentChannelId);
    }
  } catch (e) {
    alert("Erreur chargement salons");
  }
}

// ========== WebSocket / STOMP ==========

function connectWebSocket() {
  console.log('WS: On connect...');
  const socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);
  stompClient.debug = msg => {}; // enlever console.log pour perf
  stompClient.connect({}, function(frame) {
    document.getElementById('sendForm').style.display = 'flex';
    if (currentChannelId) {
      changeChannel(currentChannelId);
    }
  }, err => {
    console.error("STOMP ERROR", err);
    setTimeout(connectWebSocket, 5000);
  });
}

// ========== Message ==========

async function changeChannel(id) {
  if (currentSubscription) currentSubscription.unsubscribe();
  currentChannelId = id;

  // Efface affichage
  document.getElementById('messages').innerHTML = '';

  // Charge historique
  try {
    const res = await fetch(`/channels/${id}/messages`, { credentials: 'include' });
    if (!res.ok) throw new Error();
    const msgs = await res.json();
    msgs.forEach(m => addMessageObj(m));
  } catch (e) {
    addMessage("Erreur historique messages");
  }

  // Subscribe pour recevoir les nouveaux messages
  if (stompClient && stompClient.connected) {
    currentSubscription = stompClient.subscribe(
      `/topic/channel/${id}`,
      msg => {
        const data = JSON.parse(msg.body);
        addMessageObj(data);
      },
      { id: `sub-${id}` }
    );
  }
}

// Ajout message (objet)
function addMessageObj(m) {
  // Si tu veux surligner tes propres messages
  let me = (m.senderId === window.currentUser.id);
  let heure = m.timestamp ? (`<span class='msg-date'>${tsFormat(m.timestamp)}</span>`) : '';
  let txt = `<span class='${me ? "own" : ""}'>${me ? "Moi" : m.senderUsername}</span>: ${m.content} ${heure}`;
  addMessage(txt);
}

function tsFormat(ts) {
  let d = new Date(ts);
  return d.toLocaleTimeString();
}

function addMessage(txt) {
  const div = document.createElement('div');
  div.className = 'message';
  div.innerHTML = txt;
  const box = document.getElementById('messages');
  box.appendChild(div);
  box.scrollTop = box.scrollHeight;
}

// ========== DOM/Boutons ==========

window.addEventListener('DOMContentLoaded', async () => {
  try {
    await doLogin('test@demo.fr', 'demo'); // valeur à remplacer !
  } catch(e){}

  await loadCurrentUser();
  document.getElementById('sendForm').style.display = 'none';
  await loadChannels();
  connectWebSocket();

  document.getElementById('channelSelect').addEventListener('change', e => changeChannel(+e.target.value));

  document.getElementById('publicForm').addEventListener('submit', async e => {
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
      if (!res.ok) throw new Error();
      input.value = '';
      await loadChannels();
    } catch {}
  });

  document.getElementById('sendForm').addEventListener('submit', e => {
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

// ========== Fonctions pour le modal privé (bonus/facultatif) ==========
function openPrivateModal() {
  document.getElementById('privateModal').style.display = "flex";
}
function closePrivateModal() {
  document.getElementById('privateModal').style.display = "none";
}
