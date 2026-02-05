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
    <div className="
      /* Mobile: scroll w poziomie, przyciąganie (snap) */
      flex flex-row overflow-x-auto snap-x snap-mandatory px-4 pb-6
      /* Desktop: brak scrolla, kolumny obok siebie */
      md:overflow-x-visible md:snap-none md:px-0 md:pb-0 md:flex-row md:justify-between
      gap-4 w-full h-full min-h-[500px] items-start scrollbar-hide
    ">
      {statusColumn.map((column) => {
        const filteredTasks = tasks.filter(
          (task) => task.status === column.value
        );

        return (
          <div
            key={column.colId}
            className="
              /* Mobile: szeroka kolumna pod snap-scroll */
              min-w-[85vw] snap-center
              /* Desktop: elastyczna szerokość, mieści się w oknie */
              md:min-w-0 md:w-full md:flex-1 md:snap-align-none
            "
          >
            <CanbanColumn
              id={column.value}
              tasks={filteredTasks}
            />
          </div>
        );
      })}
    </div>
  </DndContext>
);
}
