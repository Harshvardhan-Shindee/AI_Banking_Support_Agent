import { useState, useEffect, useRef } from "react";
import ChatMessage from "./ChatMessage";
import ChatInput from "./ChatInput";
import "../styles/chat.css";

const ChatBotPanel = () => {

    const [open, setOpen] = useState(() => {
        const saved = localStorage.getItem("chat_open");
        return saved ? JSON.parse(saved) : false; // default CLOSED
    });

    useEffect(() => {
        localStorage.setItem("chat_open", JSON.stringify(open));
    }, [open]);

    const [messages, setMessages] = useState([
        { type: "bot", text: "Hi 👋 I'm your banking assistant" }
    ]);

    const [darkMode, setDarkMode] = useState(true);

    const [isVoiceMode, setIsVoiceMode] = useState(false);
    const [listening, setListening] = useState(false);
    const [liveText, setLiveText] = useState("");

    const [input, setInput] = useState("");
    const [typing, setTyping] = useState(false);
    const [voiceEnabled, setVoiceEnabled] = useState(true);

    const recognitionRef = useRef(null);
    const endRef = useRef(null);
    const firstLoad = useRef(true);
    const isSending = useRef(false); // 🔥 prevent spam

    /* ================= SPEECH RECOGNITION ================= */
    useEffect(() => {
        if ("webkitSpeechRecognition" in window) {
            const r = new window.webkitSpeechRecognition();

            r.continuous = false;
            r.interimResults = true;
            r.lang = "en-IN";

            r.onstart = () => {
                setListening(true);
                setLiveText("");
            };

            r.onend = () => {
                setListening(false);
                setIsVoiceMode(false);
            };

            r.onresult = (e) => {
                let text = "";
                for (let i = e.resultIndex; i < e.results.length; i++) {
                    text += e.results[i][0].transcript;
                }
                setLiveText(text);
            };

            recognitionRef.current = r;
        }
    }, []);

    /* 🔥 Sync live voice text */
    useEffect(() => {
        if (isVoiceMode && listening) {
            setInput(liveText);
        }
    }, [liveText, isVoiceMode, listening]);

    /* ================= SCROLL ================= */
    useEffect(() => {
        endRef.current?.scrollIntoView({
            behavior: firstLoad.current ? "smooth" : "auto"
        });
        firstLoad.current = false;
    }, [messages]);

    /* ================= VOICE CONTROL ================= */
    const startListening = () => {
        if (!recognitionRef.current) return;
        setIsVoiceMode(true);
        recognitionRef.current.start();
    };

    const stopListening = () => {
        recognitionRef.current?.stop();
        setIsVoiceMode(false);
    };

    /* ================= TEXT TO SPEECH ================= */
    const speak = (text) => {
        if (!voiceEnabled) return;

        window.speechSynthesis.cancel();

        const speech = new SpeechSynthesisUtterance(text);
        speech.rate = 1;
        speech.pitch = 1;
        speech.volume = 1;

        window.speechSynthesis.speak(speech);
    };

    /* ================= SEND MESSAGE ================= */
    const sendMessage = async () => {
        if (!input.trim() || isSending.current) return;

        isSending.current = true;

        // stop mic if active
        if (isVoiceMode) {
            recognitionRef.current?.stop();
            setIsVoiceMode(false);
        }

        const text = input;

        setMessages((prev) => [...prev, { type: "user", text }]);
        setInput("");
        setTyping(true);

        try {
            const res = await fetch("http://localhost:8002/chat", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify({ query: text })
            });

            const data = await res.json();

            setMessages((prev) => [
                ...prev,
                { type: "bot", text: data.reply }
            ]);

            speak(data.reply);

        } catch (err) {
            setMessages((prev) => [
                ...prev,
                { type: "bot", text: "Server error 😔" }
            ]);
        } finally {
            setTyping(false);
            isSending.current = false;
        }
    };

    /* ================= FLOAT BUTTON ================= */
    if (!open) {
        return (
            <div className="ai-float-btn" onClick={() => setOpen(true)}>
                🤖
            </div>
        );
    }

    /* ================= UI ================= */
    return (
        <>
        <div
            className="ai-close-floating"
            onClick={() => setOpen(false)}
        >
            ✕
        </div>

        <div className={`ai-panel ${darkMode ? "dark" : "light"}`}>

            {/* HEADER */}
            <div className="ai-header">

                <div className="left">
                    <div className="title">Assistant</div>
                    <div className="status">Online</div>
                </div>

                <div className="right">

                    <label className="theme-switch">
                        <input
                            type="checkbox"
                            checked={darkMode}
                            onChange={() => setDarkMode(!darkMode)}
                        />
                        <span className="slider"></span>
                    </label>

                    <button
                        className={`voice-toggle ${voiceEnabled ? "active" : ""}`}
                        onClick={() => setVoiceEnabled(!voiceEnabled)}
                    >
                        🔊
                    </button>

                

                </div>

            </div>
            
            {/* MESSAGES */}
            <div className="ai-messages">

                {messages.map((m, i) => (
                    <ChatMessage key={i} type={m.type} text={m.text} />
                ))}

                {typing && <ChatMessage type="bot" typing />}

                <div ref={endRef}></div>
            </div>

            {/* INPUT */}
            <ChatInput
                input={input}
                setInput={setInput}
                sendMessage={sendMessage}
                startListening={startListening}
                stopListening={stopListening}
                isVoiceMode={isVoiceMode}
            />

        </div>
        </>
    );
    
};

export default ChatBotPanel;