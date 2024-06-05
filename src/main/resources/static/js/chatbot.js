document.addEventListener("DOMContentLoaded", function() {
    var chatContainer = document.querySelector('.chatbot-container');

    function toggleChat() {
        var chatContent = chatContainer.querySelector('.chat-content');
        var toggleIcon = chatContainer.querySelector('#toggleIcon');
        if (chatContent.style.display === 'none' || chatContent.style.display === '') {
            chatContent.style.display = 'block';
            toggleIcon.textContent = 'minimize';
        } else {
            chatContent.style.display = 'none';
            toggleIcon.textContent = 'web_asset';
        }
    }

    function sendMessage() {
        var userInput = chatContainer.querySelector('#userInput');
        var chatBox = chatContainer.querySelector('#chatBox');
        var input = userInput.value;
        userInput.value = '';

        chatBox.innerHTML += '<div class="p-2 bg-gray-100 rounded-md mb-1">Tú: ' + input + '</div>';

        $.ajax({
            url: '/chatbot',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ text: input }),
            success: function(response) {
                chatBox.innerHTML += '<div class="p-2 bg-green-100 rounded-md mb-1">Bot: ' + response + '</div>';
                chatBox.scrollTop = chatBox.scrollHeight;
            },
            error: function() {
                chatBox.innerHTML += '<div class="p-2 bg-red-100 rounded-md mb-1">Bot: No pude obtener respuesta.</div>';
            }
        });
    }
    document.querySelector('#userInput').addEventListener('keypress', function(event) {
        if (event.keyCode === 13) { // 13 es el código de tecla para Enter
            event.preventDefault(); // Previene cualquier acción predeterminada de Enter
            sendMessage();
        }
    });
    chatContainer.querySelector('.header').onclick = toggleChat;
    chatContainer.querySelector('button').onclick = sendMessage;
});
