import { PostTask } from "../types/postTask";
import CreateProjectRequestDTO, { Project } from "../types/projects";
import { Task } from "../types/task";
import { handleResponse } from "../utils/handleResponse";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

const JSON_HEADERS = {
  "Content-Type": "application/json",
};

// -------- PROJECTS --------

export async function fetchProjectsFromApi() {
  const res = await fetch(`${BASE_URL}/projects`, {
    credentials: "include",
  });

  return handleResponse(res);
}

export async function fetchProjectFromApi(id: string) {
  const res = await fetch(`${BASE_URL}/projects/${id}`, {
    credentials: "include",
  });

  return handleResponse(res);
}

export async function postProject(project: CreateProjectRequestDTO) {
  const res = await fetch(`${BASE_URL}/projects`, {
    method: "POST",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(project),
  });

  return handleResponse(res);
}

export async function putProject(id: string, project: Project) {
  const res = await fetch(`${BASE_URL}/projects/${id}`, {
    method: "PUT",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(project),
  });

  return handleResponse(res);
}

export async function deleteProjectFromApi(id: string) {
  const res = await fetch(`${BASE_URL}/projects/${id}`, {
    method: "DELETE",
    credentials: "include",
  });

  return handleResponse(res);
}

// -------- TASKS --------

export async function fetchTasksByProjectId(projectId: string) {
  const res = await fetch(`${BASE_URL}/projects/${projectId}/tasks`, {
    credentials: "include",
  });

  return handleResponse(res);
}

export async function fetchTask(taskId: string) {
  const res = await fetch(`${BASE_URL}/tasks/${taskId}`, {
    credentials: "include",
  });

  return handleResponse(res);
}

export async function postTask(task: PostTask) {
  const res = await fetch(`${BASE_URL}/projects/${task.projectId}/tasks`, {
    method: "POST",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(task),
  });
  if (!res.ok) {
    let errorMessage = "Task not added";
    try {
      const data = await res.json();
      if (data?.message) errorMessage = data.message;
    } catch {}
    throw new Error(errorMessage);
  }

  return handleResponse(res);
}

export async function putTask(taskId: string, newData: Task) {
  const res = await fetch(`${BASE_URL}/tasks/${taskId}`, {
    method: "PUT",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(newData),
  });

  return handleResponse(res);
}

export async function deleteTaskFromApi(id: string) {
  const res = await fetch(`${BASE_URL}/tasks/${id}`, {
    method: "DELETE",
    credentials: "include",
  });

  return handleResponse(res);
}

// -------- AUTH --------

export async function loginUser(username: string, password: string) {
  const res = await fetch(`${BASE_URL}/auth/login`, {
    method: "POST",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify({ username, password }),
  });

  return handleResponse(res);
}

export async function registerUser(username: string, password: string) {
  const res = await fetch(`${BASE_URL}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({ username, password }),
  });

  return handleResponse(res);
}

export async function fetchMe() {
  const res = await fetch(`${BASE_URL}/auth/me`, {
    credentials: "include",
  });

  return handleResponse(res);
}

export async function logoutUser() {
  await fetch(`${BASE_URL}/auth/logout`, {
    method: "POST",
    credentials: "include",
  });
}

export async function getUsers() {
  const res = await fetch(`${BASE_URL}/auth/users`, {
    method: "GET",
    credentials: "include",
  });

  return handleResponse(res);
}

export async function changeRole(userId: string, newRole: string) {
  const res = await fetch(`${BASE_URL}/auth/${userId}/role`, {
    method: "PATCH",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ role: newRole }),
  });

  return handleResponse(res);
}

export async function patchTask(taskId: string, updatedFields: Partial<Task>) {
  const res = await fetch(`${BASE_URL}/projects/${taskId}/tasks`, {
    method: "PATCH",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(updatedFields),
  });
  return handleResponse(res);
}
