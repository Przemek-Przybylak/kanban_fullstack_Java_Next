"use client";

import React from "react";
import { useState } from "react";
import { ModalWrapper } from "./ModalsWrapper";
import { useModalStore } from "../../stores/useModalStore";
import { useProjectsStore } from "../../stores/useProjectsStore";
import Input from "../Input/Input";
import { Project } from "../../types/projects";

export const AddProjectModal = () => {
  const [newProject, setNewProject] = useState<Project>({} as Project);
  const { type, closeModal } = useModalStore();
  const { sendProject } = useProjectsStore();

  if (type !== "addProject") return null;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewProject({
      ...newProject,
      title: e.target.value,
    });
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
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
          onChange={handleChange}
          required={true}
          placeholder="Enter project title"
        />
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
