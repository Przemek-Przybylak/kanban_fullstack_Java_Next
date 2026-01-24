import { PostTask } from "../types/postTask";
import CreateProjectRequestDTO, { Project } from "../types/projects";
import { Task } from "../types/task";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

const JSON_HEADERS = {
  "Content-Type": "application/json",
};

// -------- PROJECTS --------

export async function fetchProjectsFromApi() {
  const res = await fetch(`${BASE_URL}/projects`, {
    credentials: "include",
  });
  if (!res.ok) throw new Error("Failed to fetch projects");
  return res.json();
}

export async function fetchProjectFromApi(id: string) {
  const res = await fetch(`${BASE_URL}/projects/${id}`, {
    credentials: "include",
  });
  if (!res.ok) throw new Error("Failed to fetch project");
  return res.json();
}

export async function postProject(project: CreateProjectRequestDTO) {
  const res = await fetch(`${BASE_URL}/projects`, {
    method: "POST",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(project),
  });
  if (!res.ok) throw new Error("Failed to add project");
  return res.json();
}

export async function putProject(id: string, project: Project) {
  const res = await fetch(`${BASE_URL}/projects/${id}`, {
    method: "PUT",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(project),
  });
  if (!res.ok) throw new Error(`Failed to edit project ${id}`);
  return res.json();
}

export async function deleteProjectFromApi(id: string) {
  const res = await fetch(`${BASE_URL}/projects/${id}`, {
    method: "DELETE",
    credentials: "include",
  });
  if (!res.ok) throw new Error(`Failed to delete project ${id}`);
  return true;
}

// -------- TASKS --------

export async function fetchTasksByProjectId(projectId: string) {
  const res = await fetch(`${BASE_URL}/projects/${projectId}/tasks`, {
    credentials: "include",
  });
  if (!res.ok) throw new Error("Failed to fetch tasks");
  return res.json();
}

export async function fetchTask(taskId: string) {
  const res = await fetch(`${BASE_URL}/tasks/${taskId}`, {
    credentials: "include",
  });
  if (!res.ok) throw new Error("Failed to fetch task");
  return res.json();
}

export async function postTask(task: PostTask) {
  const res = await fetch(`${BASE_URL}/projects/${task.projectId}/tasks`, {
    method: "POST",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(task),
  });
  if (!res.ok) throw new Error("Failed to add task");
  return res.json();
}

export async function putTask(taskId: string, newData: Task) {
  const res = await fetch(`${BASE_URL}/tasks/${taskId}`, {
    method: "PUT",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(newData),
  });
  if (!res.ok) throw new Error(`Failed to edit task ${taskId}`);
  return res.json();
}

export async function deleteTaskFromApi(id: string) {
  const res = await fetch(`${BASE_URL}/tasks/${id}`, {
    method: "DELETE",
    credentials: "include",
  });
  if (!res.ok) throw new Error(`Failed to delete task ${id}`);
  return true;
}

// -------- AUTH --------

export async function loginUser(username: string, password: string) {
  const res = await fetch(`${BASE_URL}/auth/login`, {
    method: "POST",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify({ username, password }),
  });

  if (!res.ok) throw new Error("Login failed");

  return res.json();
}

export async function fetchMe() {
  const res = await fetch(`${BASE_URL}/auth/me`, {
    credentials: "include",
  });
  if (!res.ok) return null;
  return res.json();
}

export async function logoutUser() {
  await fetch(`${BASE_URL}/auth/logout`, {
    method: "POST",
    credentials: "include",
  });
}
