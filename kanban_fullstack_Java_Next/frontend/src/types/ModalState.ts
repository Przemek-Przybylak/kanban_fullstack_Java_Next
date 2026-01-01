import { Project } from "./projects";
import { Task } from "./task";

export interface ModalState {
  isOpen: boolean;
  type: string | null;
  data: Project | Task | null;
  openModal: (type: string, data?: Project | Task) => void;
  closeModal: () => void;
}
