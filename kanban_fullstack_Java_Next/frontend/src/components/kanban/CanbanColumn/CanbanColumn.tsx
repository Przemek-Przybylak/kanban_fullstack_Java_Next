import React from "react";
import { useDroppable } from "@dnd-kit/core";
import { useModalStore } from "../../../stores/useModalStore";
import { Task } from "../../../types/task";
import Button from "../../Button/Button";
import TaskCard from "../TaskCard/TaskCard";

export default function CanbanColumn({
  tasks,
  id,
}: {
  tasks: Task[];
  id: string;
}) {
  const { openModal } = useModalStore();

  const { setNodeRef, isOver } = useDroppable({
    id: id,
  });

  const formattedStatus = id.replace(/_/g, " ").toUpperCase();

return (
  <div
    ref={setNodeRef}
    className={`
      p-3 md:p-4 rounded-xl transition-all duration-300 flex flex-col min-w-0
      ${isOver ? "bg-blue-50 ring-2 ring-blue-300 scale-[1.01]" : "bg-gray-100/80"}
      border border-gray-200 shadow-sm h-full
    `}
  >
    <div className="flex items-center justify-between mb-4 border-b border-gray-300 pb-2">
      <h3 className="text-sm md:text-base font-bold text-gray-700 uppercase tracking-wider truncate">
        {formattedStatus} ({tasks.length})
      </h3>
      <Button
        onClick={() => openModal("addTask")}
        variant="ghost"
        className="h-8 px-2 text-xs font-bold"
      >
        + Add
      </Button>
    </div>

    <div className="flex flex-col gap-3 overflow-y-auto max-h-[70vh] pr-1">
      {tasks.length > 0 ? (
        tasks.map((task) => <TaskCard key={task.id} task={task} />)
      ) : (
        <p className="text-gray-400 italic py-8 text-center text-xs">
          No tasks here
        </p>
      )}
    </div>
  </div>
);
}
