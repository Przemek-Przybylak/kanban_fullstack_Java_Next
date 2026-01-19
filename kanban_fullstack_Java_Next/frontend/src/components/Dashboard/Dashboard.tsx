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
      <div>{project?.title}</div>
      {isOpen && type === "task" && <TaskModal />}
      <CanbanBoard />
    </div>
  );
}
