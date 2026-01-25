"use client";

import React from "react";
import { useModalStore } from "../../stores/useModalStore";
import { AddProjectModal } from "./AddProjectModal";

export default function ModalProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const { type } = useModalStore();

  return (
    <>
      {children}

      {type === "addProject" && <AddProjectModal />}
    </>
  );
}
