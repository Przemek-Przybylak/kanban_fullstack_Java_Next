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
      className={`p-4 rounded-lg shadow-md transition-all duration-300 flex flex-col flex-1 min-w-0 ${
        isOver ? "bg-blue-50 ring-2 ring-blue-300 scale-[1.01]" : "bg-gray-100"
      }`}
    >
      <h3 className="text-xl font-semibold mb-4 text-gray-700 border-b border-gray-300 pb-2">
        {formattedStatus}
      </h3>

      <Button onClick={() => openModal("addTask")}>Add Task</Button>

      <div className="flex flex-col gap-4 mt-4 overflow-y-auto max-h-[70vh]">
        {tasks.length > 0 ? (
          tasks.map((task) => <TaskCard key={task.id} task={task} />)
        ) : (
          <p className="text-gray-500 italic py-4 text-center">No tasks here</p>
        )}
      </div>
    </div>
  );
}
