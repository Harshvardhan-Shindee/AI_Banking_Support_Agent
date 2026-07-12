import { useState } from "react";
import api from "../services/api";

const DepositModal = ({ close, refresh, accno, showToast }) => {
  const [amount, setAmount] = useState("");
  const [loading, setLoading] = useState(false);

  const handleDeposit = async () => {
    if (!amount || amount <= 0) {
      showToast("Enter valid amount", "error");
      return;
    }
    
    try {
      await api.post("/transactions/deposit", {
        accno,
        amount
      });

      showToast("Deposit successful", "success");
      refresh();
      close();

    } catch (err) {
      showToast("Deposit failed", "error");
    }
  };
  return (
    <div className="modal-overlay">
      <div className="modal-card">
        <h3>Deposit Money</h3>
        <input
          type="number"
          placeholder="Enter amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />

        <button onClick={handleDeposit}>
          {loading ? "Processing..." : "Confirm Deposit"}
        </button>

        <button className="cancel-btn" onClick={close}>
          Cancel
        </button>
      </div>
    </div>
  );
};

export default DepositModal;