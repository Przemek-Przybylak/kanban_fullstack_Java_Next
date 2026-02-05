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
      bg-white p-4 rounded-lg shadow-sm hover:shadow-md
      transition-all duration-300 cursor-pointer active:cursor-grabbing
      touch-none border border-gray-100 select-none
    "
    onClick={() => openModal("task", task)}
  >
    <h3 className="text-sm md:text-base font-semibold mb-2 text-gray-800 leading-tight line-clamp-2">
      {title}
    </h3>

    <p className="text-xs text-gray-500 mb-3 line-clamp-2">
      {description}
    </p>

    <div className="flex flex-col gap-2 text-[10px] md:text-xs text-gray-500 border-t pt-2">
      <div className="flex justify-between items-center">
        <span>📅 {dueDate}</span>
        <StatusBadge status={status} className="scale-90 origin-right" />
      </div>
      <div className="truncate font-medium text-gray-600">
        📁 {project.title}
      </div>
    </div>
  </div>
);
}