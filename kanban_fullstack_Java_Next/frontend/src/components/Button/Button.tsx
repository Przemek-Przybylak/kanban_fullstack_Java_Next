import React from "react";
import { useAuthStore } from "../../stores/useAuthStore";

export default function Button({
  children,
  onClick,
  className = "",
  variant = "primary",
  requireAuth = false,
}: {
  children: React.ReactNode;
  onClick: () => void;
  className?: string;
  variant?: "primary" | "danger";
  requireAuth?: boolean;
}) {
  const isLoggedIn = useAuthStore((s) => s.isLoggedIn);

  const baseClasses =
    "px-4 py-2 rounded transition-colors mb-4 uppercase max-h-[40px]";

  const variantClasses = {
    primary: "bg-gray-600 text-white hover:bg-gray-500",
    danger: "bg-red-600 text-white hover:bg-red-500",
  };

  const disabledClasses =
    "bg-gray-300 text-gray-500 cursor-not-allowed hover:bg-gray-300";

  const isDisabled = requireAuth && !isLoggedIn;

  const handleClick = () => {
    if (isDisabled) {
      alert("Musisz być zalogowany, aby wykonać tę akcję");
      return;
    }
    onClick();
  };

  return (
    <button
      onClick={handleClick}
      disabled={isDisabled}
      className={`
        ${baseClasses}
        ${isDisabled ? disabledClasses : variantClasses[variant]}
        ${className}
      `}
    >
      {children}
    </button>
  );
}
