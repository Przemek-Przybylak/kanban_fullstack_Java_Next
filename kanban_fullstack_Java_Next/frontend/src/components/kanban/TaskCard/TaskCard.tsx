import React from "react";
import { useDraggable } from "@dnd-kit/core";
import { CSS } from "@dnd-kit/utilities";
import { useModalStore } from "../../../stores/useModalStore";
import { Task } from "../../../types/task";
import { StatusBadge } from "../../modals/statusBadge";

export default function TaskCard({ task }: { task: Task }) {
  const { id, title, description, dueDate, project, status } = task;
  const { openModal } = useModalStore();

  const { attributes, listeners, setNodeRef, transform, isDragging } = useDraggable({
    id: id,
  });

  const style = {
    transform: CSS.Translate.toString(transform),
    opacity: isDragging ? 0.5 : 1,
    zIndex: isDragging ? 50 : 1,
  };

return (
  <div
    ref={setNodeRef}
    style={style}
    {...attributes}
    {...listeners}
    className="
      bg-white p-4 md:p-5 rounded-lg shadow-sm hover:shadow-md
      transition-all duration-300 cursor-pointer active:cursor-grabbing
      touch-none border border-gray-100 select-none
    "
    onClick={() => openModal("task", task)}
  >
    <h3 className="text-lg md:text-xl font-semibold mb-2 text-gray-800 leading-tight">
      {title}
    </h3>

    <p className="text-sm md:text-base text-gray-600 mb-4 line-clamp-2">
      {description}
    </p>

    <div className="flex flex-col gap-2 sm:flex-row sm:flex-wrap sm:gap-4 text-xs md:text-sm text-gray-500">
      <div className="flex items-center gap-1">
        <span className="font-bold text-gray-700">Due:</span>
        <span>{dueDate}</span>
      </div>

      <div className="flex items-center gap-1">
        <span className="font-bold text-gray-700">Project:</span>
        <span className="truncate max-w-[120px]">{project.title}</span>
      </div>

      <div className="flex items-center gap-1 mt-1 sm:mt-0">
        <StatusBadge status={status} />
      </div>
    </div>
  </div>
);
}