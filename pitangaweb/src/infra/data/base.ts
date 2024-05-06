import axios from 'axios';
import { restConfig } from '../../app/config/rest.config';

const apiBase = axios.create({
  baseURL: restConfig.baseURL,
  timeout: 30000,
  validateStatus: (status) => [200, 404].includes(status),
  withCredentials: true,
  headers: {
    Authorization: 'Bearer ' + window.sessionStorage.getItem('access_token')
  },
});

apiBase.interceptors.response.use(function (val) {
  if(val.status === 401) {
    window.sessionStorage.clear();
  }
  return val;
});

export { apiBase };
