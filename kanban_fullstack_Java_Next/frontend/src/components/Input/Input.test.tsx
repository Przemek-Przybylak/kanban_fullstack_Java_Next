import { render, screen, fireEvent } from "@testing-library/react";
import Input from "./Input";

describe("Input", () => {
  test("renders input with label and placeholder", () => {
    render(
      <Input
        label="Test Input"
        value=""
        onChange={() => {}}
        placeholder="Enter text"
      />
    );

    expect(screen.getByText("Test Input")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Enter text")).toBeInTheDocument();
  });

  test("calls onChange when input value changes", () => {
    const handleChange = jest.fn();
    render(
      <Input
        label="Test Input"
        value=""
        onChange={handleChange}
        placeholder="Enter text"
      />
    );

    const input = screen.getByPlaceholderText("Enter text");
    fireEvent.change(input, { target: { value: "New Value" } });
    expect(handleChange).toHaveBeenCalledTimes(1);
  });
  test("renders input with required attribute", () => {
    render(
      <Input
        label="Required Input"
        value=""
        onChange={() => {}}
        placeholder="Required"
        required={true}
      />
    );

    const input = screen.getByPlaceholderText("Required");
    expect(input).toBeRequired();
  });

  test("set input type to password", () => {
    render(
      <Input
        label="Password"
        type="password"
        value=""
        onChange={() => {}}
        placeholder="Enter password"
      />
    );

    const input = screen.getByPlaceholderText("Enter password");
    expect(input).toHaveAttribute("type", "password");
  });
});
