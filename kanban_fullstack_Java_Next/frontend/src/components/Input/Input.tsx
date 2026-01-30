import React from "react";

export default function Input({
  label = "",
  type = "text",
  value,
  onChange,
  placeholder = "",
  required = false,
  error = null,
}: {
  label?: string;
  type?: string;
  value: string | undefined;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
  required?: boolean;
  error?: string | null;
}) {
  return (
    <div className="flex flex-row mb-4">
      {label && (
        <label className="block text-sm font-medium mb-1">{label}</label>
      )}
      <input
        type={type}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
        required={required}
        className={`border-[1px] border-gray-800 rounded p-2 w-full ${error ? "border-red-500 ring-1 ring-red-500" : "border-gray-300"}`}
      />
      {error && <span className="text-red-500 text-xs mt-1 font-medium">{error}</span>}
    </div>
  );
}
