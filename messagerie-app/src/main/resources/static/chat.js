let stompClient;
let currentChannelId = null;

function initChat() {
  const socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, () => {
    stompClient.subscribe('/topic/messages', message => {
      const data = JSON.parse(message.body);
      if (data.channelId == currentChannelId) {
        addMessage(`${data.senderUsername}: ${data.content}`);
      }
    });
    document.getElementById('sendForm').style.display = 'flex';
  });
}

document.getElementById('sendForm').addEventListener('submit', e => {
  e.preventDefault();
  const msg = document.getElementById('msgInput').value.trim();
  if (msg && currentUser && currentChannelId) {
    const messageDTO = {
      content: msg,
      senderId: currentUser.id,
      channelId: parseInt(currentChannelId)
    };
    stompClient.send('/app/chat', {}, JSON.stringify(messageDTO));
    stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(messageDTO));
  }
});

function addMessage(text) {
  const div = document.createElement('div');
  div.textContent = text;
  const container = document.getElementById('messages');
  container.appendChild(div);
  container.scrollTop = container.scrollHeight;
}
