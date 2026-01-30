"use client";

import React from "react";
import { useState } from "react";
import { ModalWrapper } from "./ModalsWrapper";
import { useModalStore } from "../../stores/useModalStore";
import { useProjectsStore } from "../../stores/useProjectsStore";
import Input from "../Input/Input";
import CreateProjectRequestDTO, { Project } from "../../types/projects";
import { useAuthStore } from "../../stores/useAuthStore";
import { validateRequired } from "../../utils/validators";

export const AddProjectModal = () => {
  const isLoggedIn = useAuthStore((s) => s.isLoggedIn);
  const [newProject, setNewProject] = useState<CreateProjectRequestDTO>({
    title: "",
    description: "",
  });
  const { type, closeModal } = useModalStore();
  const { sendProject } = useProjectsStore();
  const [errors, setErrors] = useState<Record<string, string>>({});

  if (type !== "addProject") return null;

  const validate = () => {
      const newErrors: Record<string, string> = {};

      const titleError = validateRequired(newProject.title);
      if (titleError) newErrors.title = titleError;

      const descError = validateRequired(newProject.description, 10);
      if (descError) newErrors.description = descError;

      setErrors(newErrors);
      return Object.keys(newErrors).length === 0;
    };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!isLoggedIn) {
      alert("Must be logged in to add a project");
      return;
    }

    if (validate()) {
      sendProject(newProject);
      closeModal();
    }
  };

  return (
    <ModalWrapper isOpen={true} onClose={closeModal}>
      <h2 className="py-4 uppercase bold">Add project</h2>
      <form onSubmit={handleSubmit}>
        <Input
          label="Project title"
          type="text"
          value={newProject.title}
          error={errors.title}
          onChange={(e) =>
            setNewProject({ ...newProject, title: e.target.value })
          }
          placeholder="Enter project title"
        />

        <Input
          label="Description"
          multiline={true}
          rows={3}
          value={newProject.description}
          error={errors.description}
          onChange={(e) =>
            setNewProject({ ...newProject, description: e.target.value })
          }
          placeholder="Enter project description"
        />

        <button
          className="mt-4 border-[1px] border-gray-800 rounded p-2 hover:opacity-70"
          type="submit"
        >
          Add
        </button>
      </form>
    </ModalWrapper>
  );
};
