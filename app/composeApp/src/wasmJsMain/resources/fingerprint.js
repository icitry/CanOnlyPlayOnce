async function hashFingerprint(data) {
    const buffer = await crypto.subtle.digest("SHA-256", new TextEncoder().encode(data));
    return Array.from(new Uint8Array(buffer)).map(b => b.toString(16).padStart(2, "0")).join("");
}

async function getCanvasFingerprint() {
    const canvas = new OffscreenCanvas(200, 100);
    const context = canvas.getContext("2d");

    context.textBaseline = "top";
    context.font = "16px Arial";
    context.fillStyle = "black";
    context.fillText("Hello, World!", 10, 10);
    context.fillStyle = "rgba(255, 0, 0, 0.5)";
    context.fillRect(20, 20, 50, 50);

    const bitmap = canvas.transferToImageBitmap();
    const fingerprint = bitmap.width + "," + bitmap.height;
    const hash = await hashFingerprint(fingerprint);
    return hash;
}

async function getFontFingerprint() {
    const testFonts = ["Arial", "Verdana", "Courier New", "Comic Sans MS", "Impact"];
    const canvas = document.createElement("canvas");
    const context = canvas.getContext("2d");

    const baseFont = "monospace";
    const testString = "abcdefghijklmnopqrstuvwxyz0123456789";

    const availableFonts = testFonts.filter(font => {
        context.font = `16px ${baseFont}`;
        const baseWidth = context.measureText(testString).width;

        context.font = `16px ${font}, ${baseFont}`;
        return context.measureText(testString).width !== baseWidth;
    });

    const fingerprint = availableFonts.join(",");
    const hash = await hashFingerprint(fingerprint);
    return hash;
}

async function getAudioFingerprint() {
    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    const oscillator = audioContext.createOscillator();
    const analyser = audioContext.createAnalyser();
    const gain = audioContext.createGain();

    oscillator.type = "triangle";
    oscillator.frequency.value = 10000;
    gain.gain.value = 0;

    oscillator.connect(gain);
    gain.connect(analyser);
    analyser.connect(audioContext.destination);
    oscillator.start(0);

    const fingerprint = await new Promise((resolve) => {
        setTimeout(() => {
            const data = new Uint8Array(analyser.frequencyBinCount);
            analyser.getByteFrequencyData(data);
            resolve(Array.from(data).join(","));
        }, 500);
    });

    oscillator.stop();
    audioContext.close();

    const hash = await hashFingerprint(fingerprint);
    return hash;
}

export async function getCombinedFingerprint() {
    const [canvasHash, fontHash, audioHash] = await Promise.all([
        getCanvasFingerprint(),
        getFontFingerprint(),
        getAudioFingerprint(),
    ]);

    const combined = `${canvasHash}|${fontHash}|${audioHash}`;
    const finalHash = await hashFingerprint(combined);
    return finalHash;
}
