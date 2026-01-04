export interface Task {
  id: string;
  title: string;
  description: string | null;
  status: string;
  approvedBy: string | null;
  dueDate?: string;
  createdAt: string;
  project: {
               id: string;
               title: string;
               };

}