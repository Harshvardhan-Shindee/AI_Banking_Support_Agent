import os
from dotenv import load_dotenv

load_dotenv()

# ================= CORE =================
JWT_SECRET = os.getenv("JWT_SECRET")
BASE_URL = os.getenv("BASE_URL")

# ================= AI =================
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")

# ================= EMAIL =================
EMAIL_USER = os.getenv("EMAIL_USER")
EMAIL_PASS = os.getenv("EMAIL_PASS")
SUPPORT_EMAIL = os.getenv("SUPPORT_EMAIL")


# ================= VALIDATION =================
if not JWT_SECRET:
    raise Exception("JWT_SECRET not set in .env")

if not OPENAI_API_KEY:
    raise Exception("OPENAI_API_KEY not set in .env")

if not EMAIL_USER or not EMAIL_PASS:
    raise Exception("Email credentials missing in .env")