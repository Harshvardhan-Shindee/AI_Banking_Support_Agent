from sentence_transformers import SentenceTransformer
import faiss, pickle, numpy as np, os

model = SentenceTransformer('all-MiniLM-L6-v2')

def load_data():
    with open("rag/data.txt") as f:
        return [x.strip() for x in f.read().split("\n\n") if x.strip()]

def create_vector_store():
    data = load_data()
    emb = model.encode(data).astype("float32")
    faiss.normalize_L2(emb)

    index = faiss.IndexFlatIP(len(emb[0]))
    index.add(emb)

    pickle.dump((index, data), open("rag/vector_store.pkl", "wb"))

def load_store():
    if not os.path.exists("rag/vector_store.pkl"):
        create_vector_store()
    return pickle.load(open("rag/vector_store.pkl", "rb"))

def search(query):
    index, data = load_store()

    q = model.encode([query]).astype("float32")
    faiss.normalize_L2(q)

    D, I = index.search(q, k=3)
    return " ".join([data[i] for i in I[0]])