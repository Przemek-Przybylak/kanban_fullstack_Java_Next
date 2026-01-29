import { PostTask } from "../types/postTask";
import CreateProjectRequestDTO, { Project } from "../types/projects";
import { Task } from "../types/task";

const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

const JSON_HEADERS = {
  "Content-Type": "application/json",
};

function checkPermission(res: Response) {
  if (res.status === 403) {
    throw new Error("FORBIDDEN");
  }
  if (!res.ok) throw new Error(`DELETE_FAILED`);
}

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
  checkPermission(res);
  return res.json();
}

export async function putProject(id: string, project: Project) {
  const res = await fetch(`${BASE_URL}/projects/${id}`, {
    method: "PUT",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(project),
  });
  checkPermission(res);
  return res.json();
}

export async function deleteProjectFromApi(id: string) {
  const res = await fetch(`${BASE_URL}/projects/${id}`, {
    method: "DELETE",
    credentials: "include",
  });

  checkPermission(res);

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
  if (!res.ok) {
    let errorMessage = "Task not added";
    try {
      const data = await res.json();
      if (data?.message) errorMessage = data.message;
    } catch {}
    throw new Error(errorMessage);
  }
  checkPermission(res);
  return res.json();
}

export async function putTask(taskId: string, newData: Task) {
  const res = await fetch(`${BASE_URL}/tasks/${taskId}`, {
    method: "PUT",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(newData),
  });
  checkPermission(res);
  return res.json();
}

export async function deleteTaskFromApi(id: string) {
  const res = await fetch(`${BASE_URL}/tasks/${id}`, {
    method: "DELETE",
    credentials: "include",
  });
  checkPermission(res);
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

export async function registerUser(username: string, password: string) {
  const res = await fetch(`${BASE_URL}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({ username, password }),
  });

  if (!res.ok) {
    const err = await res.json().catch(() => ({ message: "Unknown error" }));
    throw new Error(err.message || "Registration failed");
  }

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

export async function getUsers() {
  const res = await fetch(`http://localhost:8080/auth/users`, {
    method: "GET",
    credentials: "include",
  });

  if (!res.ok) {
    console.error("Cannot get users");
    return null;
  }
  return res.json();
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

  if (!res.ok) {
    const errorData = await res.json().catch(() => ({}));
    throw new Error(errorData.message || "Role cannot be change");
  }

  return true;
}

export async function patchTask(taskId: string, updatedFields: Partial<Task>) {
  const res = await fetch(`${BASE_URL}/projects/${taskId}/tasks`, {
    method: "PATCH",
    headers: JSON_HEADERS,
    credentials: "include",
    body: JSON.stringify(updatedFields),
  });
  checkPermission(res);
}
