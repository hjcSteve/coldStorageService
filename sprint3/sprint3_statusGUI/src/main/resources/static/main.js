var socket = connect();

function connect() {
    const host = document.location.host; //localhost
    const pathname = document.location.pathname;
    const addr = "ws://" + host + pathname + "statusgui";

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
                case "rejected":
                    //updateMsg(String data)
                    addDebugToWindow("Ticket Rejected: " + payload)
                    setRejected(...payload.split(","))
                    break;

                case "robotstate":
                    addDebugToWindow("robotstate: " + payload)
                    setTrolleyStatus(payload)
                    break;

                case "robotpos":
                    addDebugToWindow("robotpos: " + payload)
                    setTrolleyPosizione(payload)
                    break;

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
const numRejected = document.getElementById("messageAreaTicket");

const messageWindowTrolley = document.getElementById("messageAreaTrolley");
const messageWindowTicket = document.getElementById("messageAreaTicket");
const messageWindowDebug = document.getElementById("messageAreaDebug");
const messageWindowPosizione = document.getElementById("messageAreaPosizione");

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
function setRejected(num,x,y) {
    messageWindowTrolley.innerHTML += "<div class=\"testo\">" + num + "</div>"
}

// mappatura celle
const defaultCellValues = {
    cella00: "1",
    cella01: "1",
    cella02: "1",
    cella03: "1",
    cella04: "X",
    cella05: "X",
    cella06: "1",
    cella10: "1",
    cella11: "1",
    cella12: "1",
    cella14: "1",
    cella14: "X",
    cella15: "X",
    cella16: "1",
    cella20: "1",
    cella21: "1",
    cella22: "X",
    cella23: "1",
    cella24: "1",
    cella25: "1",
    cella26: "1",
    cella30: "1",
    cella31: "1",
    cella32: "1",
    cella33: "1",
    cella34: "1",
    cella35: "1",
    cella36: "1",
    cella40: "1",
    cella41: "1",
    cella42: "1",
    cella43: "1",
    cella44: "1",
    cella45: "1",
    cella46: "1",
    cella50: "X",
    cella51: "X",
    cella52: "X",
    cella53: "X",
    cella54: "X",
    cella55: "X",
    cella56: "X",
};

function setTrolleyPosizione(pos) {
    messageWindowPosizione.innerHTML += "<div class=\"testo\">" + pos + "</div>"
    addDebugToWindow("changed trolley position: "+pos)
    const pos_cleaned = pos.replace(/[()]/g, '');
       const [x, y] = pos_cleaned.split(",");
    const current_id_cella = "cella" + x + y;

    for (const id in defaultCellValues) {
        const cella = document.getElementById(id);
        if (current_id_cella==id) {
            cella.innerText = "r";
        } else {
            cella.innerText = defaultCellValues[id] || "";
        }
    }



    const cella = document.getElementById(id_cella);
    if (cella) {
        cella.innerText = "r";  //cella valore di default senza "r"
    }
    else {
        cella.innerText = defaultCellValues[id_cella] || "";  //cella valore di default senza "r"
    }
}

function setTrolleyStatus(status) {
    messageWindowTrolley.innerHTML += "<div class=\"testo\">" + status + "</div>"
    addDebugToWindow("current trolley status: "+status)
}


function addDebugToWindow(message) {
    messageWindowDebug.innerHTML += "<div class=\"testo\">" + message + "</div>"
}