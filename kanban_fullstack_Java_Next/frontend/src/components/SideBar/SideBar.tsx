"use client";

import { useEffect, useState } from "react";
import React from "react";
import { useProjectsStore } from "../../stores/useProjectsStore";
import Link from "next/link";
import StatusWrapper from "../StatusWrapper/StatusWrapper";
import Button from "../Button/Button";
import { useModalStore } from "../../stores/useModalStore";
import { FiMenu, FiX } from "react-icons/fi";
import { useAuthStore } from "../../stores/useAuthStore";

export default function SideBar() {
  const { fetchProjects, projects, deleteProject, loading, error } =
    useProjectsStore();
  const { openModal } = useModalStore();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);
  const { user } = useAuthStore();

  useEffect(() => {
    fetchProjects();
  }, [fetchProjects]);

  return (
      <>
        <div className="lg:hidden p-4">
          <button
            className="bg-white p-3 rounded-xl shadow-xl border border-gray-200 text-gray-800 flex items-center justify-center w-12 h-12"
            onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
          >
            {isMobileMenuOpen ? <FiX size={24} /> : <FiMenu size={24} />}
          </button>
        </div>

        {isMobileMenuOpen && (
          <div
            className="fixed inset-0 bg-black/40 backdrop-blur-sm z-30 lg:hidden"
            onClick={() => setIsMobileMenuOpen(false)}
          />
        )}

        <aside
          className={`
            bg-gray-200 overflow-y-auto transition-all duration-300

            fixed inset-y-0 left-0 z-40 w-72 shadow-2xl
            transform ${isMobileMenuOpen ? "translate-x-0" : "-translate-x-full"}

            lg:static lg:translate-x-0 lg:block lg:w-1/4 lg:max-w-[280px] lg:m-4 lg:rounded-2xl

            p-6
          `}
        >
          <div className="mt-12 lg:mt-0">
            {user?.role === "ADMIN" && (
              <div className="mb-4">
                <Link href="/users" className="text-red-500 font-bold hover:underline text-sm">
                  Admin panel ⚙️
                </Link>
              </div>
            )}

            <h2 className="text-xl font-bold mb-6 text-gray-800">Projects</h2>

            <Button
              onClick={() => openModal("addProject")}
              className="w-full mb-6 h-[40px] flex items-center justify-center bg-blue-600 text-white rounded-lg"
            >
              Add Project
            </Button>

            <StatusWrapper loading={loading && projects.length === 0} error={error}>
              <ul className="space-y-4">
                {projects?.map((project) => (
                  <li key={project.id} className="flex flex-col gap-2">
                    <Link
                      className="px-3 py-3 bg-white rounded-lg shadow-sm hover:shadow-md transition-all
                      font-medium text-gray-800 hover:text-blue-600 border-l-4 border-blue-500"
                      href={`/project/${project.id}`}
                      onClick={() => setIsMobileMenuOpen(false)}
                    >
                      {project.title}
                    </Link>
                    <Button
                      requireAuth
                      variant="danger"
                      size="sm"
                      onClick={() => deleteProject(project.id)}
                    >
                      Remove
                    </Button>
                  </li>
                ))}
              </ul>
            </StatusWrapper>
          </div>
        </aside>
      </>
    );
  }