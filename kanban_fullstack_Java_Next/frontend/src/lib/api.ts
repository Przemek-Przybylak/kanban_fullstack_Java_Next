import { Project } from "../types/projects";
import { Task } from "../types/task";

export async function fetchProjects() {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/projects`);
  if (!response.ok) throw new Error("Failed to fetch projects");

  return response.json();
}

export const fetchProject = async (id: string) => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/projects/${id}`
  );
  if (!response.ok) throw new Error("Failed to fetch project");
  return response.json();
};

export const postProject = async (project: Project) => {
  const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/projects`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(project),
  });

  if (!response.ok) throw new Error("Failed to add project");
  return response.json();
};

export const putProject = async (id: string, project: Project) => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/projects/${id}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(project),
    }
  );

  if (!response.ok)
    throw new Error(`Failed to edit project with id ${id}, ${project}`);
  return response.json();
};

export const deleteProject = async (id: string) => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/projects/${id}`,
    {
      method: "DELETE",
    }
  );

  await fetchProjects();

  if (!response.ok) throw new Error(`Failed to delete project with id ${id}`);
  return response.status === 204;
};

export async function fetchTasksByProjectId(projectId: string) {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/tasks/${projectId}`
  );
  if (!response.ok) throw new Error("Failed to fetch tasks");
  return response.json();
}

export async function fetchTask(taskId: string) {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/tasks/${taskId}`
  );
  if (!response.ok) throw new Error("Failed to fetch task");
  return response.json();
}

export async function postTask(addedTask: Task) {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/tasks/${addedTask.projectId}`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ addedTask }),
    }
  );

  if (!response.ok) throw new Error("Failed to add task");
  return response.json();
}

export async function deleteTask(taskId: string) {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/tasks/${taskId}`,
    {
      method: "DELETE",
    }
  );

  if (!response.ok) throw new Error(`Failed to delete task with id ${taskId}`);
  return response.status === 204;
}

export async function putTask(taskId: string, newData: Task) {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/tasks/${taskId}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newData),
    }
  );

  if (!response.ok)
    throw new Error(`Failed to edit task with id ${taskId}, ${newData}`);
  return response.json();
}
