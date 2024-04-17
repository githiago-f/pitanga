const apiBase = import.meta.env.PITANGA_API_BASE_URL;

export const restConfig = {
  baseURL: apiBase ?? 'https://localhost:8443'
};
