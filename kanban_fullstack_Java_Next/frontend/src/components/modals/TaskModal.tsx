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
  const {
    taskId,
    title,
    project,
    dueDate,
    status,
    description,
    assignees,
    approvedBy,
  } = task;

  return (
    <ModalWrapper isOpen={!!data} onClose={closeModal}>
      <h2 className="text-2xl font-bold mb-4 text-gray-800">{title}</h2>
      <div className="flex flex-wrap items-center gap-3 text-sm text-gray-500 mb-6">
        <span>
          Projekt: <strong className="text-gray-700">{project}</strong>
        </span>
        <span>
          Termin: <strong className="text-gray-700">{dueDate}</strong>
        </span>
        <span>
          Status: <StatusBadge status={status} />
        </span>
      </div>
      <div className="prose prose-sm text-gray-700 mb-6">
        <p>{description}</p>
      </div>
      <div className="mb-6">
        <h3 className="font-semibold text-gray-800 mb-2">Osoby przypisane:</h3>
        <ul className="flex flex-wrap gap-2">
          {assignees &&
            assignees.map((name) => (
              <li
                key={name}
                className="bg-gray-100 px-3 py-1 rounded-full text-sm text-gray-700"
              >
                {name}
              </li>
            ))}
        </ul>
      </div>
      {approvedBy ? (
        <p className="text-sm text-green-600">
          ✅ Zatwierdzone przez: <strong>{approvedBy}</strong>
        </p>
      ) : (
        <p className="text-sm text-yellow-600">⏳ Oczekuje na zatwierdzenie</p>
      )}
      <Button
        onClick={() => {
          deleteTask(taskId);
          closeModal();
        }}
        className="ml-2"
      >
        Delete Task
      </Button>
      <Button onClick={() => openModal("editTask", task)} className="ml-2">
        Edit Task
      </Button>
    </ModalWrapper>
  );
}
