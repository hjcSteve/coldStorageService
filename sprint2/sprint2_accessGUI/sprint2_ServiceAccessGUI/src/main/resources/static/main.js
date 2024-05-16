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
                    addDebugToWindow("Payload received: " + payload)
                    setWeight(...payload.split(","))
                    break;

                case "ticket":
                    //AccessGUI: ticketMsg(String ticket, String requestId)
                    // Divide il payload in ticket e numero
                    var parts = payload.split(",");
                    var ticketNum = parts[0];
                    var timestamp = parts[1];

                    //AccessGUI: ticketMsg(String ticket, String requestId)
                    addTicketToWindow("Ticket=" + ticketNum + " (Timestamp="+ timestamp+")")
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
const messageWindowTicket = document.getElementById("messageAreaTicket");
const messageWindowDebug = document.getElementById("messageAreaDebug");
const progress = document.getElementById("progress");
const curSpan = document.getElementById("cur")
const resSpan = document.getElementById("res")
const maxSpan = document.getElementById("max")

function setWeight(cur ,res , max) {
    const perc1 = (parseFloat(cur)+parseFloat(res)) / parseFloat(max) * 100
    addDebugToWindow(cur + "," + res + "," + max)
    progress.style.setProperty("--value", Math.ceil(perc1).toString() + "%");
    progress.setAttribute("aria-valuenow", perc1.toString() + "%");


    curSpan.innerHTML = cur.toString()
    resSpan.innerHTML = res.toString()
    maxSpan.innerHTML = max.toString()
    addDebugToWindow("cur=" + cur+" max="+max)
}
function submitPressedPeso() {
    if (isNaN(weight.value) || weight.value.length === 0) {
        addMessageToWindow("Weight must be a valid number!")
    } else {
        sendMessage("storerequest/" + weight.value);
        addDebugToWindow("storerequest/" + weight.value)
    }
    weight.value = "";
}
function submitPressedTicket() {
    sendMessage("dischargefood/" + ticket.value);
    addDebugToWindow("submitPressedTicket: "+ ticket.value)
    ticket.value = "";
}
function sendMessage(message) {
    socket.send(message);
}

function addMessageToWindow(message) {
    messageWindow.innerHTML += "<div class=\"testo\">" + message + "</div>"
}
function addTicketToWindow(message) {
    messageWindowTicket.innerHTML += "<div class=\"testo\">" + message + "</div>"
}
function addDebugToWindow(message) {
    messageWindowDebug.innerHTML += "<div class=\"testo\">" + message + "</div>"
}