import React from "react";

export default function Input({
  label = "",
  type = "text",
  value,
  onChange,
  placeholder = "",
  required = false,
}: {
  label?: string;
  type?: string;
  value: string | undefined;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
  required?: boolean;
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
        className="border-[1px] border-gray-800 rounded p-2 w-full"
      />
    </div>
  );
}
