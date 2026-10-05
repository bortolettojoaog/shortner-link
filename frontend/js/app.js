const form = document.getElementById("shorten-form");
const submitBtn = document.getElementById("submit-btn");
const resultBox = document.getElementById("result");
const shortUrlInput = document.getElementById("short-url");
const openLink = document.getElementById("open-link");
const copyBtn = document.getElementById("copy-btn");
const messageEl = document.getElementById("message");

function showMessage(text, type) {
    messageEl.textContent = text;
    messageEl.className = `message ${type}`;
}

function hideMessage() {
    messageEl.className = "message hidden";
}

function hideResult() {
    resultBox.className = "result hidden";
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    hideMessage();
    hideResult();

    const originalUrl = document.getElementById("originalUrl").value.trim();
    const username = document.getElementById("username").value.trim() || "anonymous";
    const notificationType = document.getElementById("notificationType").value;

    if (!originalUrl) {
        showMessage("Informe uma URL para encurtar.", "error");
        return;
    }

    submitBtn.disabled = true;
    submitBtn.querySelector("span").textContent = "Encurtando...";

    try {
        const response = await fetch("/shortner-link", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ originalUrl, username, notificationType }),
        });

        const contentType = response.headers.get("content-type") || "";
        const payload = contentType.includes("application/json")
            ? await response.json()
            : { message: await response.text() };

        if (!response.ok) {
            throw new Error(payload.message || "Não foi possível encurtar o link.");
        }

        const shortUrl = `${window.location.origin}/${payload.shortCode}`;
        shortUrlInput.value = shortUrl;
        openLink.href = shortUrl;
        resultBox.className = "result";
        showMessage("Link encurtado com sucesso!", "success");
    } catch (error) {
        showMessage(error.message || "Erro ao encurtar o link.", "error");
    } finally {
        submitBtn.disabled = false;
        submitBtn.querySelector("span").textContent = "Encurtar link";
    }
});

copyBtn.addEventListener("click", async () => {
    if (!shortUrlInput.value) return;
    try {
        await navigator.clipboard.writeText(shortUrlInput.value);
        copyBtn.textContent = "Copiado!";
        setTimeout(() => (copyBtn.textContent = "Copiar"), 1500);
    } catch (error) {
        shortUrlInput.select();
        document.execCommand("copy");
    }
});
