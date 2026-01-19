"use client";

import React from "react";
import { useEffect } from "react";
import TaskModal from "../../components/modals/TaskModal";
import { useModalStore } from "../../stores/useModalStore";
import { useProjectsStore } from "../../stores/useProjectsStore";
import { Task } from "../../types/task";
import CanbanBoard from "../kanban/CanbanBoard/CanbanBoard";
import { useParams } from "next/navigation";

export default function Dashboard() {
  const id = useParams().id as string;

  const { type, isOpen } = useModalStore();
  const { project, fetchProject } = useProjectsStore();

  useEffect(() => {
    fetchProject(id);
  }, [fetchProject]);

  return (
    <div className="p-4 md:p-6 lg:p-8 flex-1 overflow-x-auto">
      <div className="mb-8">
        <h1
          className="
          text-3xl md:text-4xl
          font-bold
          tracking-tight
          text-slate-900
          mb-2 uppercase
        "
        >
          {project?.title}
        </h1>

        {project?.description && (
          <p
            className="
            text-slate-500
            text-base md:text-lg
            max-w-2xl
            leading-relaxed capitalize
          "
          >
            {project.description}
          </p>
        )}
      </div>
      {isOpen && type === "task" && <TaskModal />}
      <CanbanBoard />
    </div>
  );
}
