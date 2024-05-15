var socket = connect();

 // mapping celle
 const defaultCellValues = {
    cella00: "home",
    cella01: "",
    cella02: "",
    cella03: "",
    cella04: "door",
    cella05: "X",
    cella06: "",
    cella10: "",
    cella11: "",
    cella12: "",
    cella13: "",
    cella14: "X",
    cella15: "X",
    cella16: "",
    cella20: "",
    cella21: "",
    cella22: "X",
    cella23: "",
    cella24: "",
    cella25: "",
    cella26: "",
    cella30: "",
    cella31: "",
    cella32: "",
    cella33: "",
    cella34: "",
    cella35: "",
    cella36: "",
    cella40: "",
    cella41: "",
    cella42: "",
    cella43: "ice",
    cella44: "",
    cella45: "",
    cella46: "",
    cella50: "X",
    cella51: "X",
    cella52: "X",
    cella53: "X",
    cella54: "X",
    cella55: "X",
    cella56: "X",
};

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
        addDebugToWindow("Connection to the server successful");

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
                    setTrolleyStatus(payload)
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

const imageMapping = {
    "home": "home.png",
    "door": "door.png",
    "ice": "ice.png",
    "X": "x.png",
};

// img based on cell value
function setImageForCell(cellId, value) {
    const cell = document.getElementById(cellId);
    if (cell) {
        cell.innerHTML = "";
        if (imageMapping.hasOwnProperty(value)) {
            const img = document.createElement("img");
            img.src = imageMapping[value];
            img.alt = value;
            cell.appendChild(img);
        } else {
            cell.innerText = value;
        }
    }
}

function setImagesForAllCells() {
    for (const cellId in defaultCellValues) {
        setImageForCell(cellId, defaultCellValues[cellId]);
    }
}

window.onload = function() {
    setImagesForAllCells();
};



function setTrolleyPosizione(pos) {
    messageWindowPosizione.innerHTML += "<div class=\"testo\">" + pos + "</div>"
    addDebugToWindow("changed trolley position: "+pos)
    const pos_cleaned = pos.replace(/[()]/g, '');
    const [x, y] = pos_cleaned.split(",");
    const current_id_cella = "cella" + x + y;

    setImagesForAllCells()

    for (const id in defaultCellValues) {
        const cella = document.getElementById(id);
        if (current_id_cella == id) {
   
            cella.innerText = "";

            const img = document.createElement("img");
            img.src = "robot.png"; 
            cella.appendChild(img);
        }
    }
}



function setTrolleyStatus(status) {
   
    messageWindowTrolley.innerHTML = ""; // clean window
    if (status === "(0,4)") {
        const img = document.createElement("img");
        img.src = "door.png";
        messageWindowTrolley.appendChild(img);
    }
    else if(status === "(4,3)"){
        const img = document.createElement("img");
        img.src = "ice.png";
        messageWindowTrolley.appendChild(img);
    } else if(status === "(0,0)"){
        const img = document.createElement("img");
        img.src = "home.png";
        messageWindowTrolley.appendChild(img);
    }
}



function addDebugToWindow(message) {
    messageWindowDebug.innerHTML += "<div class=\"testo\">" + message + "</div>"
}
