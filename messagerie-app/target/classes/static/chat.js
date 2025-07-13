let stompClient = null;
let currentChannelId = null;
let currentUser = null;

// Initialisation de la connexion WebSocket
function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);

        // S'abonner au topic des messages
        stompClient.subscribe('/topic/messages', function(message) {
            const data = JSON.parse(message.body);
            console.log("Message reçu:", data);

            if (data.channelId == currentChannelId) {
                addMessage(`${data.senderUsername}: ${data.content}`);
            }
        });

        // Afficher le formulaire une fois connecté
        document.getElementById('sendForm').style.display = 'flex';
    }, function(error) {
        console.error('Erreur de connexion WebSocket:', error);
        setTimeout(connect, 5000); // Réessayer après 5 secondes
    });
}

// Initialisation du chat
function initChat(user, channelId) {
    currentUser = user;
    currentChannelId = channelId;

    if (!stompClient || !stompClient.connected) {
        connect();
    }

    // Charger les messages existants si nécessaire
    loadMessages(channelId);
}

// Chargement des messages existants
function loadMessages(channelId) {
    fetch(`/api/channels/${channelId}/messages`)
        .then(response => response.json())
        .then(messages => {
            messages.forEach(msg => {
                addMessage(`${msg.senderUsername}: ${msg.content}`);
            });
        })
        .catch(error => console.error('Erreur lors du chargement des messages:', error));
}

// Gestion de l'envoi de message
document.getElementById('sendForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const msgInput = document.getElementById('msgInput');
    const messageContent = msgInput.value.trim();

    if (messageContent && currentUser && currentChannelId && stompClient && stompClient.connected) {
        const messageDTO = {
            content: messageContent,
            senderId: currentUser.id,
            channelId: currentChannelId
        };

        try {
            stompClient.send('/app/chat', {}, JSON.stringify(messageDTO));
            msgInput.value = ''; // Vider le champ après envoi
        } catch (error) {
            console.error("Erreur lors de l'envoi du message:", error);
            alert("Erreur lors de l'envoi du message. Veuillez réessayer.");
        }
    } else {
        console.error("Conditions non remplies pour envoyer le message");
        if (!stompClient || !stompClient.connected) {
            alert("Non connecté au serveur de chat. Veuillez rafraîchir la page.");
        }
    }
});

// Gestion de l'appui sur Entrée
document.getElementById('msgInput').addEventListener('keydown', function(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        document.getElementById('sendForm').dispatchEvent(new Event('submit'));
    }
});

// Ajout d'un message à l'interface
function addMessage(text) {
    const messagesContainer = document.getElementById('messages');
    const messageElement = document.createElement('div');
    messageElement.className = 'message';
    messageElement.textContent = text;
    messagesContainer.appendChild(messageElement);
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

// Fonction pour changer de canal
function changeChannel(channelId) {
    currentChannelId = channelId;
    document.getElementById('messages').innerHTML = ''; // Vider les messages
    loadMessages(channelId); // Charger les messages du nouveau canal
}

