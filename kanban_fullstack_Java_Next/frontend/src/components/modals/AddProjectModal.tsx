"use client";

import React from "react";
import { useState } from "react";
import { ModalWrapper } from "./ModalsWrapper";
import { useModalStore } from "../../stores/useModalStore";
import { useProjectsStore } from "../../stores/useProjectsStore";
import Input from "../Input/Input";
import CreateProjectRequestDTO, { Project } from "../../types/projects";
import { useAuthStore } from "../../stores/useAuthStore";

export const AddProjectModal = () => {
  const isLoggedIn = useAuthStore((s) => s.isLoggedIn);
  const [newProject, setNewProject] = useState<CreateProjectRequestDTO>({
    title: "",
    description: "",
  });
  const { type, closeModal } = useModalStore();
  const { sendProject } = useProjectsStore();

  if (type !== "addProject") return null;

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!isLoggedIn) {
      alert("Must be logged in to add a project");
      return;
    }

    sendProject(newProject);
    closeModal();
  };

  return (
    <ModalWrapper isOpen={true} onClose={closeModal}>
      <h2 className="py-4 uppercase bold">Add project</h2>
      <form onSubmit={handleSubmit}>
        <Input
          label="Project title"
          type="text"
          value={newProject.title}
          onChange={(e) =>
            setNewProject({ ...newProject, title: e.target.value })
          }
          required={true}
          placeholder="Enter project title"
        />
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Description
          </label>
          <textarea
            className="w-full border border-gray-300 rounded-md p-2 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            rows={3}
            value={newProject.description}
            onChange={(e) =>
              setNewProject({ ...newProject, description: e.target.value })
            }
          />
        </div>
        <button
          className="border-[1px] border-gray-800 rounded p-2 hover:opacity-70"
          type="submit"
        >
          Add
        </button>
      </form>
    </ModalWrapper>
  );
};
