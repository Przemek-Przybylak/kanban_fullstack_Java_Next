import { create } from "zustand";
import {
  deleteTask,
  fetchTasksByProjectId,
  fetchTask,
  postTask,
  putTask,
} from "../lib/api";
import { Task } from "../types/task";

interface TasksStore {
  tasks: Task[];
  task: Task | null;
  loading: boolean;
  error: string | null;
  fetchTasksByProjectId: (projectId: string) => Promise<void>;
  fetchTask: (projectId: string, taskId: string) => Promise<void>;
  addTask: (addedTask: Task) => Promise<void>;
  deleteTask: (taskId: string) => Promise<void>;
  editTask: (taskId: string, newData: Partial<Task>) => Promise<void>;
}

export const useTasksStore = create<TasksStore>((set, get) => ({
  tasks: [],
  task: null,
  loading: false,
  error: null,

  fetchTasksByProjectId: async (projectId: string) => {
    set({ loading: true, error: null });
    try {
      const tasks = await fetchTasksByProjectId(projectId);
      set({ tasks });
    } catch (error) {
      set({ error: (error as Error).message });
    } finally {
      set({ loading: false });
    }
  },
  fetchTask: async (taskId: string) => {
    set({ loading: true, error: null });
    try {
      const task = await fetchTask(taskId);
      set({ task });
    } catch (error) {
      set({ error: (error as Error).message });
    } finally {
      set({ loading: false });
    }
  },
  addTask: async (addedTask: Task) => {
    set({ loading: true, error: null });
    try {
      const task = await postTask(addedTask);
      set((state) => ({ tasks: [...state.tasks, task] }));
      console.log("Odpowiedź z backendu:", task);
    } catch (error) {
      set({ error: (error as Error).message });
    } finally {
      set({ loading: false });
    }
  },
  deleteTask: async (taskId: string) => {
    set({ loading: true, error: null });
    try {
      await deleteTask(taskId);
      set((state) => ({
        tasks: state.tasks.filter((task) => task.taskId !== taskId),
      }));
    } catch (error) {
      set({ error: (error as Error).message });
    } finally {
      set({ loading: false });
    }
  },
  editTask: async (taskId: string, newData: Partial<Task>) => {
    set({ loading: true, error: null });
    try {
      // Find the existing task to merge with newData
      const existingTask = get().tasks.find((task) => task.taskId === taskId);
      if (!existingTask) throw new Error("Task not found");
      const updatedTask = await putTask(taskId, {
        ...existingTask,
        ...newData,
      });
      set((state) => ({
        tasks: state.tasks.map((task) =>
          task.taskId === taskId ? updatedTask : task
        ),
      }));
    } catch (error) {
      set({ error: (error as Error).message });
    } finally {
      set({ loading: false });
    }
  },
}));
