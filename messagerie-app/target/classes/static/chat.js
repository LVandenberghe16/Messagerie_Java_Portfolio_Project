let stompClient;
let currentChannelId = null;

function initChat() {
  const socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, () => {
    if (currentChannelId) {
      stompClient.subscribe(`/topic/channel/${currentChannelId}`, message => {
        const data = JSON.parse(message.body);
        addMessage(`${data.sender.username}: ${data.content}`);
      });
    }
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
    stompClient.send('/app/chat.sendMessage', {}, JSON.stringify({
      content: msg,
      sender: { id: currentUser.id },
      channel: { id: parseInt(currentChannelId) }
    }));
  }
});

function addMessage(text) {
  const div = document.createElement('div');
  div.textContent = text;
  const container = document.getElementById('messages');
  container.appendChild(div);
  container.scrollTop = container.scrollHeight;
}
