const mockState = {
  tasks: [],
  addTask: jest.fn(),
  updateTask: jest.fn(),
  deleteTask: jest.fn(),
  setTasks: jest.fn(),
};

export const useTasksStore = jest.fn(() => mockState);
export const setMockTasksState = (state: Partial<typeof mockState>) => {
  Object.assign(mockState, state);
};
