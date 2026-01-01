import { render, screen, fireEvent } from "@testing-library/react";
import Button from "./Button";

describe("Button", () => {
  test("reacts to click events, render with text", () => {
    const handleClick = jest.fn();
    render(<Button onClick={handleClick}>Click Me</Button>);
    const button = screen.getByRole("button", { name: /click me/i });
    expect(button).toBeInTheDocument();
    fireEvent.click(button);
    expect(handleClick).toHaveBeenCalledTimes(1);
  });
});

test("renders variant primary", () => {
  render(
    <Button onClick={() => {}} variant="primary">
      Primary Button
    </Button>
  );
  const button = screen.getByRole("button", { name: /primary button/i });
  expect(button).toHaveClass("bg-gray-600");
});

test("added danger class for variant danger", () => {
  render(
    <Button onClick={() => {}} variant="danger">
      Danger Button
    </Button>
  );
  const button = screen.getByRole("button", { name: /danger button/i });
  expect(button).toHaveClass("bg-red-600");
});
