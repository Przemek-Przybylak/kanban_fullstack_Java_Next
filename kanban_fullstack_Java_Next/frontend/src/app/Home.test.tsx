import { render, screen } from "@testing-library/react";
import Home from "./page";
import { useModalStore } from "../stores/useModalStore";

jest.mock("../stores/useModalStore", () => ({
  useModalStore: jest.fn(),
}));

const mockUseModalStore = useModalStore as unknown as jest.Mock;

describe("Home", () => {
  it("renders without crashing", () => {
    mockUseModalStore.mockReturnValue({
      type: null,
      openModal: jest.fn(),
    });

    render(<Home />);
    expect(
      screen.getByRole("button", { name: /add project/i })
    ).toBeInTheDocument();
  });
  it("opens AddProjectModal when type is 'addProject'", () => {
    mockUseModalStore.mockReturnValue({
      type: "addProject",
      openModal: jest.fn(),
    });

    render(<Home />);
    expect(screen.getByRole("dialog")).toBeInTheDocument();
  });

  it("does not render AddProjectModal when type is not 'addProject'", () => {
    mockUseModalStore.mockReturnValue({
      type: null,
      openModal: jest.fn(),
    });

    render(<Home />);
    expect(screen.queryByRole("dialog")).not.toBeInTheDocument();
  });
});

it("renders the Add Project button", () => {
  mockUseModalStore.mockReturnValue({
    type: null,
    openModal: jest.fn(),
  });

  render(<Home />);
  const button = screen.getByRole("button", { name: /add project/i });
  expect(button).toBeInTheDocument();
  expect(button).toHaveTextContent("Add Project");
});
