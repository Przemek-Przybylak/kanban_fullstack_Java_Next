import { create } from "zustand";

type User = {
  id: string;
  role: string;
  username: string;
};

type AuthState = {
    user: User | null;
    isLoggedIn: boolean;

    setUser: (user: User) => void;
    logout: () => void;
    }

export const useAuthStore = create<AuthStore>((set) => ({
  user: null,
  isLoggedIn: false,

  setUser: (user) =>
    set({
      user,
      isLoggedIn: true,
    }),

  logout: () =>
    set({
      user: null,
      isLoggedIn: false,
    }),
}));
