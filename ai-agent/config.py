import os
from dotenv import load_dotenv

load_dotenv()

# ================= CORE =================
JWT_SECRET = os.getenv("JWT_SECRET")
BASE_URL = os.getenv("BASE_URL")

# ================= AI =================
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
GROQ_API_KEY = os.getenv("GROQ_API_KEY")

# ================= EMAIL =================
EMAIL_USER = os.getenv("EMAIL_USER")
EMAIL_PASS = os.getenv("EMAIL_PASS")
SUPPORT_EMAIL = os.getenv("SUPPORT_EMAIL")

# ================= VALIDATION =================
# Hard requirements — app cannot function without these
if not JWT_SECRET:
    raise Exception("JWT_SECRET not set in .env")

if not BASE_URL:
    raise Exception("BASE_URL not set in .env")

# Soft requirements — warn only, don't crash startup
if not OPENAI_API_KEY and not GROQ_API_KEY:
    print("⚠️ WARNING: No LLM API key set (OpenAI or Groq) — chatbot replies will fail")

if not EMAIL_USER or not EMAIL_PASS:
    print("⚠️ WARNING: Email credentials missing — escalation emails disabled")