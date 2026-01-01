import { render, screen } from "@testing-library/react";
import Dashboard from "./Dashboard";

jest.mock("../../stores/useModalStore", () => ({
  useModalStore: jest.fn(),
}));
jest.mock("../../stores/useTasksStore", () => ({
  useTasksStore: jest.fn(),
}));

import { useModalStore } from "../../stores/useModalStore";

const mockUseModalStore = useModalStore as unknown as jest.Mock;

describe("Dashboard", () => {
  it("renders TaskModal when isOpen is true and type is 'task'", () => {
    mockUseModalStore.mockImplementation(() => ({
      isOpen: true,
      type: "task",
      data: {
        taskId: "1",
        title: "Test task",
        project: "Test project",
        dueDate: "2025-01-01",
        description: "This is a test task",
        status: "todo",
        assignees: ["user1"],
        approvedBy: "user2",
        projectId: "1",
      },
      closeModal: jest.fn(),
    }));

    render(<Dashboard tasks={[]} />);
    expect(screen.getByRole("dialog")).toBeInTheDocument();
  });
});
