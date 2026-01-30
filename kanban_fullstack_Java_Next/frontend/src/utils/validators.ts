export const validateRequired = (value: string, min = 3) => {
  if (!value || value.trim().length < min) {
    return `Must be at least ${min} characters long`;
  }
  return null;
};

export const validateDate = (value: string) => {
  if (!value) return "Date is required";
  return null;
};