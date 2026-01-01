import { render, screen, within } from "@testing-library/react";
import CanbanBoard from "./CanbanBoard";
import { Task } from "../../../types/task";

const tasks: Task[] = [
  {
    taskId: "1",
    title: "Task 1",
    status: "todo",
    description: "Description for Task 1",
    projectId: "1",
  },
  {
    taskId: "2",
    title: "Task 2",
    status: "in_progress",
    description: "Description for Task 2",
    projectId: "1",
  },
  {
    taskId: "3",
    title: "Task 3",
    status: "in_progress",
    description: "Description for Task 3",
    projectId: "1",
  },
  {
    taskId: "4",
    title: "Task 4",
    status: "done",
    description: "Description for Task 4",
    projectId: "1",
  },
];

describe("CanbanBoard", () => {
  it("renders all columns", () => {
    render(<CanbanBoard tasks={tasks} />);
    expect(screen.getByRole("heading", { name: /todo/i })).toBeInTheDocument();
    expect(
      screen.getByRole("heading", { name: /in progress/i })
    ).toBeInTheDocument();
    expect(
      screen.getByRole("heading", { name: /in progress/i })
    ).toBeInTheDocument();
    expect(screen.getByRole("heading", { name: /done/i })).toBeInTheDocument();
  });

  it("renders tasks in rigght column", () => {
    render(<CanbanBoard tasks={tasks} />);

    const todoHeading = screen.getByRole("heading", { name: /todo/i });
    const todoColumn = todoHeading.closest("div");
    if (!todoColumn) {
      throw new Error("Todo column not found");
    }
    const todoTasks = within(todoColumn).getAllByRole("heading", {
      level: 3,
    });
    expect(todoTasks).toHaveLength(2);

    const inProgressHeading = screen.getByRole("heading", {
      name: /in progress/i,
    });
    const inProgressColumn = inProgressHeading.closest("div");
    if (!inProgressColumn) {
      throw new Error("In Progress column not found");
    }
    const inProgressTasks = within(inProgressColumn).getAllByRole("heading", {
      level: 3,
    });
    expect(inProgressTasks).toHaveLength(3);
  });
});
