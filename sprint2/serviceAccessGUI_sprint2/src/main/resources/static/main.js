var socket = connect();

function connect() {
    const host = document.location.host; //localhost
    const pathname = document.location.pathname;
    const addr = "ws://" + host + pathname + "accessgui";

    //-- socket connection
    if (socket !== undefined && socket.readyState !== WebSocket.CLOSED) {
        alert("Alert: WebSocket connection already established");
    }
    socket = new WebSocket(addr); 
    socket.onopen = function (event) {
        addMessageToWindow("Connection to the server successful");
    };

    //--message handling
    socket.onmessage = function (event) { 
        console.log("socket.onmessage="+event.data)
        let [type, payload] = event.data.split("/");
        if (payload !== undefined)
            switch (type) {
                case "update":
                    //updateMsg(String data)
                    setPeso(payload.split(","))
                    break;

                case "ticket":
                    //AccessGUI: ticketMsg(String ticket, String requestId)
                    addMessageToWindow("Ticket and Timestamp: " + payload)
                    break;

                case "notify":
                    //AccessGUI: notifyMsg(String message, String requestId)
                    if (payload === "chargetaken") {
                        addMessageToWindow("Charge taken")
                    } else {
                        addMessageToWindow(payload.replace("accepted", "accettato"))
                    }
                    break;
                case "error":
                    //AccessGUI: errorMsg(String error, String requestId)
                    addMessageToWindow("Errore! " + payload)
                    break;
                default:
                    addMessageToWindow("" + `${event.data}`)
                    break;
            }
    };
    return socket;
}
const weight = document.getElementById("peso");
const ticket = document.getElementById("ticket");
const messageWindow = document.getElementById("messageArea");
const progress = document.getElementById("progress");
const curSpan = document.getElementById("cur")
const maxSpan = document.getElementById("max")

function setPeso(cur, max) {
    const perc = cur / max * 100
    progress.style.setProperty("--value", Math.ceil(perc).toString());
    progress.setAttribute("aria-valuenow", perc.toString());
    curSpan.innerHTML = cur.toString()
    maxSpan.innerHTML = max.toString()
    addMessageToWindow("cur=" + cur+" max="+max)
}
function submitPressedPeso() {
    if (isNaN(weight.value) || weight.value.length === 0) {
        addMessageToWindow("Weight must be a valid number!")
    } else {
        sendMessage("storerequest/" + weight.value);
        addMessageToWindow("storerequest/" + weight.value)
    }
    weight.value = "";
}
function submitPressedTicket() {
    sendMessage("dischargefood/" + ticket.value);
    addMessageToWindow("submitPressedTicket: "+ ticket.value)
    ticket.value = "";
}
function sendMessage(message) {
    socket.send(message);
}

function addMessageToWindow(message) {
    messageWindow.innerHTML += "<div class=\"testo\">" + message + "</div>"
}