import React from "react";
import { StatusBadge } from "./statusBadge";
import { ModalWrapper } from "./ModalsWrapper";
import { useModalStore } from "../../stores/useModalStore";
import Button from "../Button/Button";
import { useTasksStore } from "../../stores/useTasksStore";
import { Task } from "../../types/task";

export default function TaskModal() {
  const { data, type, closeModal } = useModalStore();
  const openModal = useModalStore((state) => state.openModal);
  const deleteTask = useTasksStore((state) => state.deleteTask);

  if (type !== "task") return null;
  const task = data as Task;
  const { id, title, description, status, dueDate, project } = task;

  return (
    <ModalWrapper isOpen={!!data} onClose={closeModal}>
      <h2 className="text-2xl font-bold mb-4 text-gray-800">{title}</h2>
      <div className="flex flex-wrap items-center gap-3 text-sm text-gray-500 mb-6">
        <span>
          Project: <strong className="text-gray-700">{project.title}</strong>
        </span>
        <span>
          Due date: <strong className="text-gray-700">{dueDate}</strong>
        </span>
        <span>
          Status: <StatusBadge status={status} />
        </span>
      </div>
      <div className="prose prose-sm text-gray-700 mb-6">
        <p>{description}</p>
      </div>
      <Button
        requireAuth
        onClick={() => {
          deleteTask(id);
          closeModal();
        }}
        className="ml-2"
      >
        Delete Task
      </Button>
      <Button
        requireAuth
        onClick={() => openModal("editTask", task)}
        className="ml-2"
      >
        Edit Task
      </Button>
    </ModalWrapper>
  );
}
