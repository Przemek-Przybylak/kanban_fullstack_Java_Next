"use client";

import React from "react";
import {
  DndContext,
  DragEndEvent,
  PointerSensor,
  useSensor,
  useSensors,
} from "@dnd-kit/core";
import { Column } from "../../../types/columns";
import CanbanColumn from "../CanbanColumn/CanbanColumn";
import { useTasksStore } from "../../../stores/useTasksStore";

const statusColumn: Column[] = [
  { colId: 1, colTitle: "To Do", value: "todo" },
  { colId: 2, colTitle: "In Progress", value: "in_progress" },
  { colId: 3, colTitle: "To Check", value: "review" },
  { colId: 4, colTitle: "Done", value: "done" },
];

export default function CanbanBoard() {
  const { tasks, moveTask } = useTasksStore();

  const sensors = useSensors(
    useSensor(PointerSensor, {
      activationConstraint: {
        distance: 5,
      },
    }),
  );

  const handleDragEnd = (event: DragEndEvent) => {
    const { active, over } = event;

    if (!over) return;

    const taskId = active.id as string;
    const newStatus = over.id as string;

    if (taskId && newStatus) {
      moveTask(taskId, newStatus);
    }
  };

  return (
    <DndContext sensors={sensors} onDragEnd={handleDragEnd}>
      <div className="flex flex-row gap-4 w-full h-full min-h-[500px] items-start">
        {statusColumn.map((column) => {
          const filteredTasks = tasks.filter(
            (task) => task.status === column.value,
          );

          return (
            <CanbanColumn
              key={column.colId}
              id={column.value}
              tasks={filteredTasks}
            />
          );
        })}
      </div>
    </DndContext>
  );
}
