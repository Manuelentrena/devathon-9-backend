let client = null;
let savedToken = "";

function updateButtonStyles(connected) {
    document.getElementById("connectBtn").disabled = connected;
    document.getElementById("disconnectBtn").disabled = !connected;
    document.getElementById("connectBtn").classList.toggle("disabled", connected);
    document.getElementById("disconnectBtn").classList.toggle("disabled", !connected);
}

document.getElementById("connectBtn").addEventListener("click", () => {
    client = new StompJs.Client({
        brokerURL: getSocketUrl(),
        onConnect: (frame) => {
            updateButtonStyles(true);
            document.getElementById("frameBox").innerHTML = `<div class="json-container">${JSON.stringify(frame, null, 2)}</div>`;

            client.subscribe("/topic/num-players", (message) => {
                document.getElementById("numPlayersMessage").innerHTML = `<div class="json-container">${JSON.stringify(JSON.parse(message.body), null, 2)}</div>`;
            });

            client.subscribe("/user/queue/token-id", (message) => {
                const { token_id } = JSON.parse(message.body);
                savedToken = token_id;
                document.getElementById("tokenIdDisplay").value = token_id;
            });

            client.subscribe("/user/queue/errors", (message) => {
                console.log(JSON.parse(message.body))
                document.getElementById("errorsMessage").innerHTML = `<div class="json-container">${JSON.stringify(JSON.parse(message.body), null, 2)}</div>`;
            });

            client.publish({
                destination: "/app/num-players",
                body: "{}"
            });

            client.publish({
                destination: "/app/token-id",
                body: "{}",
                headers: {
                    token_id: getToken()
                }
            });

        },
        onDisconnect: () => {
            console.log("Disconnnect")
            updateButtonStyles(false);
            document.getElementById("frameBox").innerHTML = `<div class="json-container">Frame will appear here</div>`;
            document.getElementById("numPlayersMessage").innerHTML = `<div class="json-container">Waiting for data...</div>`;
            document.getElementById("errorsMessage").innerHTML = `<div class="json-container">Waiting for data...</div>`;
            // 🔁 Reset session id
            document.getElementById("tokenIdDisplay").value = "--- no token id ---";
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

function getSocketUrl() {
    const base = window.location.origin.replace(/^http/, "ws") + "/connect-game";
    return savedToken ? `${base}?token_id=${encodeURIComponent(savedToken)}` : base;
}

function getToken() {
    return savedToken;
}

function copyToken() {
    const text = document.getElementById("tokenIdDisplay").value;
    navigator.clipboard.writeText(text)
        .then(() => alert("✅ Token copiado al portapapeles"))
        .catch(() => alert("❌ Error al copiar"));
}
