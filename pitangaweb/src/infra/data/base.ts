import axios, { AxiosError } from 'axios';
import { restConfig } from '../../app/config/rest.config';
import { loginAction } from '../../app/actions/login-action';

const apiBase = axios.create({
  baseURL: restConfig.baseURL,
  timeout: 30000,
  validateStatus: (status) => [200, 404].includes(status),
  withCredentials: true,
});

apiBase.interceptors.request.use((val) => {
  const token = window.sessionStorage.getItem('access_token');
  if (token === null) {
    return val;
  }
  val.headers.Authorization = `Bearer ${token}`;
  return val;
});

apiBase.interceptors.response.use(v => v, async (val) => {
  if(val instanceof AxiosError) {
    if (val.response?.status === 401) {
      window.sessionStorage.removeItem('access_token');
      await loginAction();
    }
  }
  return val;
});

export { apiBase };
