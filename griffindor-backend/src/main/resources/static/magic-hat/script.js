const socketUrl = window.location.origin.replace(/^http/, "ws") + "/connect-game";
let client = null;

function updateButtonStyles(connected) {
    document.getElementById("connectBtn").disabled = connected;
    document.getElementById("disconnectBtn").disabled = !connected;
    document.getElementById("connectBtn").classList.toggle("disabled", connected);
    document.getElementById("disconnectBtn").classList.toggle("disabled", !connected);
}

document.getElementById("connectBtn").addEventListener("click", () => {
    client = new StompJs.Client({
        brokerURL: socketUrl,
        onConnect: (frame) => {
            updateButtonStyles(true);
            document.getElementById("frameBox").innerHTML = `<div class="json-container">${JSON.stringify(frame, null, 2)}</div>`;

            client.subscribe("/topic/num-players", (message) => {
                document.getElementById("numPlayersMessage").innerHTML = `<div class="json-container">${JSON.stringify(JSON.parse(message.body), null, 2)}</div>`;
            });

            client.subscribe("/user/queue/session-id", (message) => {
                const { session_id } = JSON.parse(message.body);
                console.log({session_id})
                const sessionIdInput = document.getElementById("sessionIdDisplay");
                sessionIdInput.value = session_id;
            });

            client.publish({
                destination: "/app/num-players",
                body: "{}"
            });

            client.publish({
                destination: "/app/session-id",
                body: "{}"
            });

        },
        onDisconnect: () => {
            updateButtonStyles(false);
            document.getElementById("frameBox").innerHTML = `<div class="json-container">Frame will appear here</div>`;
            document.getElementById("numPlayersMessage").innerHTML = `<div class="json-container">Waiting for data...</div>`;
            // 🔁 Reset session id
            document.getElementById("sessionIdDisplay").value = "--- no session id ---";
        },
        onStompError: (frame) => {
            document.getElementById("frameBox").innerHTML = `<div class="json-container">Error: ${frame.headers["message"]}</div>`;
        }
    });
    client.activate();
});

document.getElementById("disconnectBtn").addEventListener("click", () => {
    if (client) {
        client.deactivate();
    }
});

function toggleCard(id) {
    const content = document.getElementById(id);
    const button = id === "frameBox" ? document.getElementById("frameToggle") : document.getElementById("numPlayersToggle");
    if (content.style.display === "none") {
        content.style.display = "block";
        button.innerText = "-";
    } else {
        content.style.display = "none";
        button.innerText = "+";
    }
}
