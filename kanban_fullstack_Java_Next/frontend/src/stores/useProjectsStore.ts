import { create } from "zustand";
import {
  deleteProject,
  fetchProject,
  fetchProjects,
  postProject,
  putProject,
} from "../lib/api";
import { Project } from "../types/projects";

interface ProjectsStore {
  project: Project | null;
  projects: Project[];
  error: string | null;
  loading: boolean;
  fetchProjects: () => Promise<void>;
  fetchProject: (id: string) => Promise<void>;
  sendProject: (project: Project) => Promise<void>;
  updateProject: (id: string, project: Project) => Promise<void>;
  deleteProject: (id: string) => Promise<void>;
}

export const useProjectsStore = create<ProjectsStore>((set) => ({
  project: null,
  projects: [],
  error: null,
  loading: false,

  fetchProjects: async () => {
    set({ loading: true, error: null });
    try {
      const projects: Project[] = await fetchProjects();
      set({ projects, loading: false });
    } catch (error) {
      set({ error: (error as Error).message, loading: false });
    }
  },
  fetchProject: async (id: string) => {
    set({ loading: true, error: null });
    try {
      const response = await fetchProject(id);
      set({ project: response, loading: false });
    } catch (error) {
      set({ error: (error as Error).message, loading: false });
    }
  },
  sendProject: async (project: Project) => {
    set({ loading: true, error: null });
    try {
      const createdProject = await postProject(project);
      set((state) => ({
        projects: [...state.projects, createdProject],
        loading: false,
      }));
    } catch (error) {
      set({ error: (error as Error).message, loading: false });
    }
  },
  updateProject: async (id: string, project: Project) => {
    set({ loading: true, error: null });
    try {
      const updatedProject = await putProject(id, project);
      set((state) => ({
        projects: state.projects.map((p) =>
          p.projectId === id ? updatedProject : p
        ),
        project: updatedProject,
        loading: false,
      }));
    } catch (error) {
      set({ error: (error as Error).message, loading: false });
    }
  },
  deleteProject: async (id: string) => {
    set({ loading: true, error: null });
    try {
      await deleteProject(id);
      set((state) => ({
        projects: state.projects.filter((p) => p.projectId !== id),
        loading: false,
      }));
    } catch (error) {
      set({ error: (error as Error).message, loading: false });
    }
  },
}));
