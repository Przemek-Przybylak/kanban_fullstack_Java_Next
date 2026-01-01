import React from "react";
import { Task } from "../../types/task";

const statusClasses: Record<Task["status"], string> = {
  todo: "bg-gray-200 text-gray-800",
  in_progress: "bg-blue-200 text-blue-800",
  review: "bg-yellow-200 text-yellow-800",
  done: "bg-green-200 text-green-800",
};

export function StatusBadge({ status }: { status: Task["status"] }) {
  const label = status.replace(/_/g, " ").toUpperCase();
  const classes = statusClasses[status] ?? statusClasses.todo;

  return (
    <span
      className={`inline-block px-3 py-1 rounded-full text-xs font-semibold ${classes}`}
    >
      {label}
    </span>
  );
}
