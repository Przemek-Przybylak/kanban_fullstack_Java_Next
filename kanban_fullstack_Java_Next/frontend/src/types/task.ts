export interface Task {
  id: string;
  title: string;
  description: string;
  status: string;
  approvedBy: string | undefined;
  dueDate?: string;
  createdAt: string;
  username?: string;
  project: {
    id: string;
    title: string;
  };
}
