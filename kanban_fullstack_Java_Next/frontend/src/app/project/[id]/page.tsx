"use client";

import { useEffect } from "react";
import { useParams } from "next/navigation";
import React from "react";
import StatusWrapper from "../../../components/StatusWrapper/StatusWrapper";
import Dashboard from "../../../components/Dashboard/Dashboard";
import { AddProjectModal } from "../../../components/modals/AddProjectModal";
import { useModalStore } from "../../../stores/useModalStore";
import { useTasksStore } from "../../../stores/useTasksStore";
import TaskFormModal from "../../../components/modals/TaskFormModal";

export default function ProjectPage() {
  const { type } = useModalStore();
  const { fetchTasksByProjectId, tasks, loading, error } = useTasksStore();
  const { id } = useParams();

  useEffect(() => {
    fetchTasksByProjectId(id as string);
  }, [id]);

  return (
    <>
      <StatusWrapper loading={loading} error={error}>
        {type === "addProject" && <AddProjectModal />}
        {type === "addTask" && <TaskFormModal />}
        {type === "editTask" && <TaskFormModal />}
        {tasks && <Dashboard tasks={tasks} />}
      </StatusWrapper>
    </>
  );
}
