export interface Task {
  taskId: string;
  projectId: string;
  title: string;
  description: string;
  dueDate?: string;
  project?: string;
  status: string;
  assignees?: string[];
  approvedBy?: string | undefined;
}
