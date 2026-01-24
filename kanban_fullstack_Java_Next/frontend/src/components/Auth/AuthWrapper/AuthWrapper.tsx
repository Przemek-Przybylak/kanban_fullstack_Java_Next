import Link from "next/link";
import React from "react";

export default function AuthWrapper() {
  return (
    <div className="fixed top-4 right-4 flex gap-4 z-50">
      <Link href="/auth/login" className="hover:underline">
        Login
      </Link>
      <Link href="/auth/register" className="hover:underline">
        Register
      </Link>
    </div>
  );
}
