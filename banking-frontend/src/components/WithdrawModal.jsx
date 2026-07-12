import { useState } from "react";
import api from "../services/api";


const WithdrawModal = ({ close, refresh, accno, showToast }) => {
  const [amount, setAmount] = useState("");
  const [loading, setLoading] = useState(false);
  const handleWithdraw = async () => {
    if (!amount || amount <= 0) {
      showToast("Enter valid amount", "error");
      return;
    }

    try {
      setLoading(true);

      await api.post("/transactions/withdraw", {
        accno,
        amount: Number(amount)
      });
      showToast("Withdraw successful", "success");
      refresh();   // reload balance
      close();     // close modal
      
    } catch (err) {
      console.error("Withdraw error:", err.response?.data || err.message);
      showToast("Withdraw failed", "error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <h3>Withdraw Money</h3>
        <input
          type="number"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          placeholder="Enter amount"
        />

        <button onClick={handleWithdraw} disabled={loading}>
          {loading ? "Processing..." : "Confirm Withdraw"}
        </button>

        <button onClick={close}>Cancel</button>
      </div>
    </div>
  );
};

export default WithdrawModal;