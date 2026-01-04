export interface Project {
  projectId: string;
  title: string;
  tasks: string[];
}

export default interface CreateProjectRequestDTO {
  title: string;
  description: string;
}
