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
      className="bg-white p-5 rounded-lg shadow-md hover:shadow-lg transition-shadow duration-300 cursor-pointer active:cursor-grabbing touch-none"
      onClick={() => openModal("task", task)}
    >
      <h3 className="text-xl font-semibold mb-2">{title}</h3>
      <p className="text-gray-700 mb-3 line-clamp-2">{description}</p>
      <div className="flex flex-wrap gap-3 text-sm text-gray-600">
        <div>
          <strong>Due:</strong> {dueDate}
        </div>
        <div>
          <strong>Project:</strong> {project.title}
        </div>
        <div>
          <strong>Status:</strong> <StatusBadge status={status} />
        </div>
      </div>
    </div>
  );
}