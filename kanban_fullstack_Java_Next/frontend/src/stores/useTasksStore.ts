import { create } from "zustand";
import {
  fetchTasksByProjectId,
  fetchTask,
  postTask,
  putTask,
  deleteTaskFromApi,
  patchTask,
} from "../lib/api";
import { Task } from "../types/task";
import { PostTask } from "../types/postTask";

interface TasksStore {
  tasks: Task[];
  task: Task | null;
  loading: boolean;
  error: string | null;
  fetchTasksByProjectId: (projectId: string) => Promise<void>;
  fetchTask: (projectId: string, taskId: string) => Promise<void>;
  addTask: (addedTask: PostTask) => Promise<void>;
  deleteTask: (taskId: string) => Promise<void>;
  editTask: (taskId: string, newData: Partial<Task>) => Promise<void>;
  moveTask: (taskId: string, newStatus: string) => Promise<void>;
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

  deleteTask: async (taskId: string) => {
    set({ loading: true, error: null });
    try {
      await deleteTaskFromApi(taskId);
      set((state) => ({
        tasks: state.tasks.filter((task) => task.id !== taskId),
      }));
    } catch (error) {
      set({ error: (error as Error).message });
    } finally {
      set({ loading: false });
    }
  },

  addTask: async (addedTask: PostTask) => {
    set({ loading: true, error: null });
    try {
      const task = await postTask(addedTask);
      set((state) => ({ tasks: [...state.tasks, task] }));
    } catch (error) {
      set({ error: (error as Error).message });
    } finally {
      set({ loading: false });
    }
  },

  editTask: async (taskId: string, newData: Partial<Task>) => {
    set({ loading: true, error: null });
    try {
      const existingTask = get().tasks.find((task) => task.id === taskId);
      if (!existingTask) throw new Error("Task not found");
      const updatedTask = await putTask(taskId, {
        ...existingTask,
        ...newData,
      });
      set((state) => ({
        tasks: state.tasks.map((task) =>
          task.id === taskId ? updatedTask : task,
        ),
      }));
    } catch (error) {
      set({ error: (error as Error).message });
    } finally {
      set({ loading: false });
    }
  },

moveTask: async (taskId: string, newStatus: string) => {
    const previousTasks = get().tasks;

    set((state) => ({
      tasks: state.tasks.map((task) =>
        task.id === taskId ? { ...task, status: newStatus } : task
      ),
    }));

    try {
      await patchTask(taskId, { status: newStatus });

    } catch (error) {
      set({
        tasks: previousTasks,
        error: "Failed to move task. Reverting..."
      });
      console.error(error);
    }
  },
}));
