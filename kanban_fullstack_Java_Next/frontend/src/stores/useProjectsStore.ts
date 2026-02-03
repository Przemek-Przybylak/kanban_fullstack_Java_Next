import { create } from "zustand";
import {
  deleteProjectFromApi,
  fetchProjectFromApi,
  fetchProjectsFromApi,
  postProject,
  putProject,
} from "../lib/api";
import CreateProjectRequestDTO from "../types/projects";

interface ProjectsStore {
  project: Project | null;
  projects: Project[];
  error: string | null;
  loading: boolean;
  fetchProjects: () => Promise<void>;
  fetchProject: (id: string) => Promise<void>;
  sendProject: (project: CreateProjectRequestDTO) => Promise<void>;
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
      const projects: Project[] = await fetchProjectsFromApi();
      set({ projects, loading: false });
    } catch (error) {
      set({ error: (error as Error).message, loading: false });
    }
  },
  fetchProject: async (id: string) => {
    set({ loading: true, error: null });
    try {
      const response = await fetchProjectFromApi(id);
      set({ project: response, loading: false });
    } catch (error) {
      set({ error: (error as Error).message, loading: false });
    }
  },
  sendProject: async (dto: CreateProjectRequestDTO) => {
    set({ loading: true, error: null });
    try {
      const createdProject = await postProject(dto);
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
        projects: state.projects.map((p) => (p.id === id ? updatedProject : p)),
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
      await deleteProjectFromApi(id);
      set((state) => ({
        projects: state.projects.filter((p) => p.id !== id),
        loading: false,
      }));
    } catch (error: unknown) {
      if (error.message === "FORBIDDEN") {
        set({
          error: "You dont have permission to delete this project",
          loading: false,
        });
        return;
      }

      set({
        error: "An error occurred while deleting the project",
        loading: false,
      });
    }
  },
}));
