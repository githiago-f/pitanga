const apiBase = import.meta.env.API_BASE_URL;

export const restConfig = {
    baseURL: apiBase ?? 'https://localhost:8443'
};
