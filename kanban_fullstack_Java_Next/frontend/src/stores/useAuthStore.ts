import { create } from "zustand";

type User = {
  id: string;
  role: string;
  username: string;
};

export const useAuthStore = create<{
    user: User | mull;
    setUser: (u: User | null) => void;
    }>(set => ({
        user: null,
        setUser: user => set({ user }),
        }));
