import React from "react";

interface InputProps {
  label?: string;
  type?: string;
  value: string | undefined;
  onChange: (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => void;
  placeholder?: string;
  required?: boolean;
  error?: string | null;
  multiline?: boolean;
}

export default function Input({
  label = "",
  type = "text",
  value,
  onChange,
  error = null,
  multiline = false,
  ...props
}: InputProps) {
  const baseClasses = `border-[1px] rounded p-2 w-full transition-all outline-none ${
    error
      ? "border-red-500 ring-1 ring-red-500"
      : "border-gray-800 focus:border-blue-500"
  }`;

  return (
    <div className="flex flex-col mb-4">
      {label && (
        <label className="block text-sm font-medium mb-1">{label}</label>
      )}

      {multiline ? (
        <textarea
          value={value}
          onChange={onChange as any}
          className={baseClasses}
        />
      ) : (
        <input
          type={type}
          value={value}
          onChange={onChange}
          className={baseClasses}
        />
      )}

      {error && (
        <span className="text-red-500 text-xs mt-1 font-medium">{error}</span>
      )}
    </div>
  );
}
