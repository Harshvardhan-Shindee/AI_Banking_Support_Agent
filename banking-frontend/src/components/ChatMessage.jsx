import React from "react";

const ChatMessage = ({ type, text, typing }) => {
  return (
    <div className={`msg ${type}`}>

      <div className={`bubble ${typing ? "typing" : ""}`}>

        {typing ? (
          <div className="typing-dots">
            <span></span>
            <span></span>
            <span></span>
          </div>
        ) : (
          <span>{text}</span>
        )}

      </div>

    </div>
  );
};

export default ChatMessage;