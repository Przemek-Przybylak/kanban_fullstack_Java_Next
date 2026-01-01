const mockState = {
  isOpen: false,
  type: null,
  data: null,
  openModal: jest.fn(),
  closeModal: jest.fn(),
};

export const useModalStore = jest.fn(() => mockState);
export const setMockModalState = (state: Partial<typeof mockState>) => {
  Object.assign(mockState, state);
};
