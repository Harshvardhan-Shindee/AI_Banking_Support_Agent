import React from "react";

const ChatInput = ({
    input,
    setInput,
    sendMessage,
    startListening,
    stopListening,
    isVoiceMode
}) => {
    return (
        <div className="glass-input">

            <input
                autoFocus
                placeholder={isVoiceMode ? "Listening..." : "Ask anything..."}
                value={input}
                onChange={(e) => setInput(e.target.value)}
                onKeyDown={(e) => e.key === "Enter" && sendMessage()}
            />

            <button
                className={`icon ${isVoiceMode ? "listening" : ""}`}
                onClick={isVoiceMode ? stopListening : startListening}
            >
                {isVoiceMode ? "⏸" : "🎤"}
            </button>

            <button 
                className={`send ${!input.trim() ? "disabled" : ""}`}
                onClick={sendMessage}
                disabled={!input.trim()}
            >
                ➤
            </button>

        </div>
    );
};

export default ChatInput;