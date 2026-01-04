export interface Project {
  id: string;
  title: string;
  description: string | null;
  createdAt: string;
  updatedAt: string;
  tasks: ShortTask[];
  userId: string[];

}

export default interface CreateProjectRequestDTO {
  title: string;
  description: string;
}

export interface ShortTask {
  id: string;
  title: string;
}
