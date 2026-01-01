import { render, screen } from "@testing-library/react";
import CanbanColumn from "./CanbanColumn";

describe("CanbanColumn", () => {
  test("renders column with tasks", () => {
    const tasks = [
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
        status: "todo",
        description: "Description for Task 2",
        projectId: "1",
      },
    ];
    render(<CanbanColumn tasks={tasks} />);

    expect(screen.getByRole("heading", { name: /TODO/i })).toBeInTheDocument();
    expect(screen.getByText("Add Task")).toBeInTheDocument();
    expect(screen.getAllByRole("heading", { level: 3 })).toHaveLength(3);
  });

  test("renders empty column message when no tasks", () => {
    render(<CanbanColumn tasks={[]} />);

    expect(screen.getByText("No tasks for this column")).toBeInTheDocument();
  });
});
