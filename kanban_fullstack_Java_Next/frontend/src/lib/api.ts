import { Project } from "../types/projects";
import { Task } from "../types/task";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;
const TOKEN = process.env.NEXT_PUBLIC_DEV_TOKEN;

// Pomocnicze nagłówki z tokenem
const HEADERS = {
  "Content-Type": "application/json",
  "Authorization": `Bearer ${TOKEN}`,
};

export async function fetchProjectsFromApi() {
  const response = await fetch(`${BASE_URL}/projects`, {
    headers: { "Authorization": `Bearer ${TOKEN}` }
  });
  if (!response.ok) throw new Error("Failed to fetch projects");
  return response.json();
}

export const fetchProjectFromApi = async (id: string) => {
  const response = await fetch(`${BASE_URL}/projects/${id}`, {
    headers: { "Authorization": `Bearer ${TOKEN}` }
  });
  if (!response.ok) throw new Error("Failed to fetch project");
  return response.json();
};

export const postProject = async (project: Project) => {
  const response = await fetch(`${BASE_URL}/projects`, {
    method: "POST",
    headers: HEADERS,
    body: JSON.stringify(project),
  });
  if (!response.ok) {
       const errorData = await response.json();
       console.error("Błąd z backendu:", errorData);
       throw new Error("Failed to add project");
    }
    return response.json();
};

export const putProject = async (id: string, project: Project) => {
  const response = await fetch(`${BASE_URL}/projects/${id}`, {
    method: "PUT",
    headers: HEADERS,
    body: JSON.stringify(project),
  });
  if (!response.ok) throw new Error(`Failed to edit project with id ${id}`);
  return response.json();
};

export const deleteProjectFromApi = async (id: string) => {
  const response = await fetch(`${BASE_URL}/projects/${id}`, {
    method: "DELETE",
    headers: { "Authorization": `Bearer ${TOKEN}` }
  });
  if (!response.ok) throw new Error(`Failed to delete project with id ${id}`);
  return response.status === 204;
};

export async function fetchTasksByProjectId(projectId: string) {
  const response = await fetch(`${BASE_URL}/projects/${projectId}/tasks`, {
    headers: { "Authorization": `Bearer ${TOKEN}` }
  });
  if (!response.ok) throw new Error("Failed to fetch tasks");
  return response.json();
}

export async function fetchTask(taskId: string) {
  const response = await fetch(`${BASE_URL}/tasks/${taskId}`, {
    headers: { "Authorization": `Bearer ${TOKEN}` }
  });
  if (!response.ok) throw new Error("Failed to fetch task");
  return response.json();
}

export async function postTask(addedTask: Task) {
  const response = await fetch(`${BASE_URL}/tasks/${addedTask.projectId}`, {
    method: "POST",
    headers: HEADERS,
    body: JSON.stringify(addedTask), // Poprawiłem: wysyłamy obiekt, nie {addedTask: addedTask}
  });
  if (!response.ok) throw new Error("Failed to add task");
  return response.json();
}

export async function deleteTask(taskId: string) {
  const response = await fetch(`${BASE_URL}/tasks/${taskId}`, {
    method: "DELETE",
    headers: { "Authorization": `Bearer ${TOKEN}` }
  });
  if (!response.ok) throw new Error(`Failed to delete task with id ${taskId}`);
  return response.status === 204;
}

export async function putTask(taskId: string, newData: Task) {
  const response = await fetch(`${BASE_URL}/tasks/${taskId}`, {
    method: "PUT",
    headers: HEADERS,
    body: JSON.stringify(newData),
  });
  if (!response.ok) throw new Error(`Failed to edit task with id ${taskId}`);
  return response.json();
}