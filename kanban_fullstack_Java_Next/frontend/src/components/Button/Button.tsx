import React from "react";
export default function Button({
  children,
  onClick,
  className = "",
  variant = "primary",
}: {
  children: React.ReactNode;
  onClick: () => void;
  className?: string;
  variant?: "primary" | "danger";
}) {
  const baseClasses =
    "px-4 py-2 rounded hover:bg-gray-400 transition-colors mb-4 uppercase max-h-[40px]";

  const variantClasses = {
    primary: "bg-gray-600 text-white",
    danger: "bg-red-600 text-white hover:bg-red-500",
  };

  return (
    <button
      onClick={onClick}
      className={`${baseClasses} ${variantClasses[variant]} ${className}`}
    >
      {children}
    </button>
  );
}
