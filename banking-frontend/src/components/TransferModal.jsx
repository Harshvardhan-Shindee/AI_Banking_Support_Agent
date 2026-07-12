import { useState } from "react";
import api from "../services/api";

const TransferModal = ({ close, refresh, accno, showToast }) => {
  const [receiverAccno, setReceiverAccno] = useState("");
  const [amount, setAmount] = useState("");
  const [loading, setLoading] = useState(false);

  const handleTransfer = async () => {
    if (!receiverAccno || !amount) {
      showToast("All fields required", "error");
      return;
    }

    if (Number(receiverAccno) === accno) {
      showToast("Cannot transfer to same account", "error");
      return;
    }

    if (Number(amount) <= 0) {
      showToast("Invalid amount", "error");
      return;
    }

    try {
      setLoading(true);

      await api.post("/transactions/transfer", {
        senderAccno: accno,
        receiverAccno: Number(receiverAccno),
        amount: Number(amount),
      });
      showToast("Transfer successful", "success");
      refresh();   // reload balance
      close();     // close modal
    } catch (err) {
      console.error("Transfer error:", err.response?.data || err.message);
      showToast("Transfer failed", "error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <h3>Transfer Money</h3>
        <input
          type="number"
          placeholder="Receiver Account No"
          value={receiverAccno}
          onChange={(e) => setReceiverAccno(e.target.value)}
        />

        <input
          type="number"
          placeholder="Amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />

        <button onClick={handleTransfer} disabled={loading}>
          {loading ? "Processing..." : "Confirm Transfer"}
        </button>

        <button onClick={close}>Cancel</button>
      </div>
    </div>
  );
};

export default TransferModal;