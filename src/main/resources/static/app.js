let stompClient = null
let sessionId = null;
let connection = false;
let reconnect = null;
let getOnline = null;

let audio = new Audio(); // Создаём новый элемент Audio
audio.src = 'Untitled.mp3';

function onConnect () {
    document.querySelector('#send-button').disabled = false;
    connection = true
    stompClient.subscribe('/topic/messages', function (string) {
        showMessage(JSON.parse(string.body));
    });

    console.log('connected')

    askOnline();
    getOnline = setInterval(askOnline, 10000)
}

function onDisconnect() {
    document.querySelector('#send-button').disabled = true;
    clearTimeout(getOnline)
    connection = false
    reconnect = setInterval(connect,5000)
}

function connect() {
    clearTimeout(reconnect)
    let socket = new SockJS('/message-web-socket')
    stompClient = Stomp.over(socket)
    stompClient.connect({}, onConnect, onDisconnect);

    if (sessionId == null) {
        sessionId = getSession();
    }
}

function showMessage(message) {
    let text = message.text
    let author = message.author
    let id = message.id

    let newMessage = document.createElement("div")
    newMessage.className = 'message'

    newMessage.appendChild(createHeader(author, id))
    newMessage.append(createBody(text))

    let elem = document.getElementById('new-messages')
    elem.appendChild(newMessage)

    newMessage.scrollIntoView(true)

    let promise = audio.play();
}

function sendMessage() {
    let message =  $("#message").val()

    let frame =  {
        text : message,
        session : sessionId
    };
    let jSon = JSON.stringify(frame)

    stompClient.send("/app/hello", { }, jSon);
    $('#mess-form').trigger("reset");
}

function httpGet(theUrl)
{
    let xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
}

function getSession() {
    return httpGet("/chat/session")
}

function askOnline() {
    let answer = httpGet("/chat/online")

    /*if (answer.body == null) {
        console.log('no changes')
        return null;
    }*/

    let onlineList = JSON.parse(answer)

    let online = document.querySelector('#online')
    while (online.firstChild) {
        online.removeChild(online.firstChild);
    }

    for (let i = 0; i < onlineList.length; i += 1) {
        let text = document.createElement("a")
        text.append(onlineList[i])
        text.className = 'header-text'
        online.appendChild(text)
        online.appendChild(document.createElement("br"))
    }

}

function createHeader(author, id) {
    let messageNameBox = document.createElement("div");
    messageNameBox.className = 'message-name-box'
    let messageAuthor = document.createElement("a")
    messageAuthor.className = 'message-author'
    messageAuthor.append(author)
    messageAuthor.href = "/profile?id=" + id;
    messageNameBox.appendChild(messageAuthor)

    return messageNameBox
}

function createBody(text) {
    let messageTextBox = document.createElement("div")
    messageTextBox.className = 'message-text-box'
    let messageText = document.createElement("a")
    messageText.className = 'message-text'
    messageText.append(text)
    messageTextBox.appendChild(messageText)

    return messageTextBox
}



