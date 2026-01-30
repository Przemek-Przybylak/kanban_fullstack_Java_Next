"use client";

import React from "react";
import { useState, useEffect } from "react";
import { useModalStore } from "../../stores/useModalStore";
import { ModalWrapper } from "./ModalsWrapper";
import { useTasksStore } from "../../stores/useTasksStore";
import Input from "../Input/Input";
import { Task } from "../../types/task";
import { useParams } from "next/navigation";
import { PostTask } from "../../types/postTask";
import { useAuthStore } from "../../stores/useAuthStore";
import { validateRequired } from "../../utils/validators";

const getInitialTaskData = (projectId: string): Task => ({
  id: "",
  title: "",
  description: "",
  dueDate: "",
  status: "todo",
  approvedBy: "",
  createdAt: "",
  username: "",
  project: { id: projectId, title: "" },
});

export default function TaskFormModal() {
  const isLoggedIn = useAuthStore((s) => s.isLoggedIn);
  const projectId = useParams().id as string;
  const { type, isOpen, closeModal, data } = useModalStore();
  const { addTask, editTask } = useTasksStore();
  const [errors, setErrors] = useState<Record<string, string>>({});

  const [taskData, setTaskData] = useState<Task>(getInitialTaskData(projectId));

  const validate = () => {
    const newErrors: Record<string, string> = {};

    const titleError = validateRequired(taskData.title);
    if (titleError) newErrors.title = titleError;

    const descriptionError = validateRequired(taskData.description, 10);
    if (descriptionError) newErrors.description = descriptionError;

    const usernameError = validateRequired(taskData.username);
    if (usernameError) newErrors.username = usernameError;

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  useEffect(() => {
    if (!isOpen) return;

    if (type === "editTask" && data) {
      setTaskData(data as Task);
    } else if (type === "addTask") {
      setTaskData(getInitialTaskData(projectId));
    }
  }, [isOpen, type, data, projectId]);

  if (!isOpen || (type !== "addTask" && type !== "editTask")) {
    return null;
  }

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!isLoggedIn) {
      alert("Must be logged in to perform this action");
      return;
    }

    const taskToPost: PostTask = {
      title: taskData.title,
      description: taskData.description,
      status: taskData.status,
      dueDate: taskData.dueDate,
      approvedBy: taskData.approvedBy,
      username: taskData.username,
      projectId: projectId,
    };

    if (validate()) {
      try {
        if (type === "addTask") {
          await addTask(taskToPost);
        } else if (type === "editTask" && data) {
          await editTask(data.id, taskData);
        }

        closeModal();
      } catch (error) {
        console.error("Error saving task:", error);
      }
    } else {
      console.log("Validation errors:", errors);
    }
  };

  return (
    <ModalWrapper isOpen={isOpen} onClose={closeModal}>
      <div className="w-full max-w-md p-6 bg-white rounded-lg shadow-xl">
        <h2 className="text-2xl font-bold text-gray-800 mb-6">
          {type === "addTask" ? "Add New Task" : "Edit Task"}
        </h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <Input
            label="Task title"
            type="text"
            value={taskData.title}
            error={errors.title}
            multiline={false}
            onChange={(e) =>
              setTaskData({ ...taskData, title: e.target.value })
            }
          />

          <Input
            label="Description"
            multiline={true}
            value={taskData.description}
            error={errors.description}
            onChange={(e) => {
              setTaskData({ ...taskData, description: e.target.value });
              if (errors.description) setErrors({ ...errors, description: "" });
            }}
          />

          <Input
            label="Due Date"
            type="date"
            value={taskData.dueDate}
            multiline={false}
            onChange={(e) =>
              setTaskData({ ...taskData, dueDate: e.target.value })
            }
          />

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Status
            </label>
            <select
              className="w-full border border-gray-300 rounded-md p-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              value={taskData.status}
              onChange={(e) =>
                setTaskData({
                  ...taskData,
                  status: e.target.value as Task["status"],
                })
              }
            >
              <option value="todo">To Do</option>
              <option value="in_progress">In Progress</option>
              <option value="review">Review</option>
              <option value="done">Done</option>
            </select>
          </div>

          <Input
            label="Assignee"
            type="text"
            value={taskData.username}
            error={errors.username}
            multiline={false}
            onChange={(e) =>
              setTaskData({ ...taskData, username: e.target.value })
            }
          />

          {taskData.username && (
            <div className="bg-gray-50 p-3 rounded-md">
              <h3 className="text-sm font-medium text-gray-700 mb-2">
                Assigned to:
              </h3>
              <ul className="space-y-1">
                <li className="flex items-center justify-between bg-white px-3 py-1 rounded">
                  <span>{taskData.username}</span>
                </li>
              </ul>
            </div>
          )}

          <Input
            label="Approved By"
            type="text"
            value={taskData.approvedBy}
            multiline={false}
            onChange={(e) =>
              setTaskData({ ...taskData, approvedBy: e.target.value })
            }
          />

          <div className="flex justify-end gap-3 pt-4">
            <button
              type="button"
              onClick={closeModal}
              className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors"
            >
              Save Task
            </button>
          </div>
        </form>
      </div>
    </ModalWrapper>
  );
}
