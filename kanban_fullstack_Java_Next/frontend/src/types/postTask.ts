export interface PostTask {
  title: string;
  description: string;
  status: string;
  dueDate?: string;
  approvedBy?: string;
  username?: string;
  projectId: string;
}
