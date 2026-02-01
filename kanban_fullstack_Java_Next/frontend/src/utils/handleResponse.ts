export async function handleResponse(res) {
  console.log("DEBUG: API Response Status ->", res.status);
  if (res.ok) {
    const contentType = res.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
      return res.json();
    }
    return null;
  }

  const errorMessages = {
    400: "Invalid request. Please check your input.",
    401: "Session expired. Please log in again.",
    403: "Access denied. You don't have permission to perform this action.",
    404: "The requested resource was not found.",
    409: "Conflict detected. The data might have been modified by someone else.",
    500: "Internal server error. Please try again later.",
    503: "Service unavailable. The server is temporarily down.",
  };

  const status = res.status;
  let finalMessage =
    errorMessages[status] || `An unexpected error occurred (Status: ${status})`;

  try {
    const data = await res.json();
    if (data && data.message) {
      finalMessage = data.message;
    }
  } catch (e) {}

  throw new Error(finalMessage);
}
