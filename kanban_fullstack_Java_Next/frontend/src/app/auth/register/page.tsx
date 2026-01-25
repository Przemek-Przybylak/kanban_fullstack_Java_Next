"use client";

import React from "react";

import { useState } from "react";
import { loginUser, registerUser } from "../../../lib/api";
import { useAuthStore } from "../../../stores/useAuthStore";
import { useRouter } from "next/navigation";

export default function RegisterPage() {
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      await registerUser(username, password);
      setUsername("");
      setPassword("");
      const user = await loginUser(username, password);
      useAuthStore.getState().setUser(user);
      router.push("/");
    } catch (err: any) {
      alert(`Registration error: ${err.message}`);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <form
        onSubmit={handleRegister}
        className="w-full max-w-sm bg-white p-6 rounded-xl shadow-md flex flex-col"
      >
        <h1 className="text-2xl font-semibold mb-6 text-center">Register</h1>

        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          className="w-full mb-4 px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="w-full mb-6 px-4 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
        />

        <button
          type="submit"
          className="w-full bg-black text-white py-2 rounded hover:bg-gray-800 transition-colors"
        >
          Register & Login
        </button>
      </form>
    </div>
  );
}
function setAuthStoreUser(user: any) {
  throw new Error("Function not implemented.");
}
