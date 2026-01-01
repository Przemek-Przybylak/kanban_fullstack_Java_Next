import { render, screen, fireEvent } from "@testing-library/react";
import TaskCard from "./TaskCard";
import { Task } from "../../../types/task";
import * as modalStore from "../../../stores/useModalStore";

describe("TaskCard", () => {
  const task: Task = {
    taskId: "1",
    title: "Test Task",
    description: "This is a test task description.",
    dueDate: "2023-10-01",
    projectId: "1",
    status: "todo",
  };

  test("renders task card with title, description, due date, project, and status", () => {
    render(<TaskCard task={task} />);

    expect(screen.getByText("Test Task")).toBeInTheDocument();
    expect(
      screen.getByText("This is a test task description.")
    ).toBeInTheDocument();
    expect(screen.getByText("Status:")).toBeInTheDocument();
  });

  test("calls openModal with task when card is clicked", () => {
    const openModalMock = jest.fn();

    jest.spyOn(modalStore, "useModalStore").mockReturnValue({
      openModal: openModalMock,
      closeModal: jest.fn(),
      isOpen: false,
      type: null,
      data: null,
    });

    render(<TaskCard task={task} />);

    fireEvent.click(screen.getByText("Test Task"));

    expect(openModalMock).toHaveBeenCalledWith("task", task);
  });
});
