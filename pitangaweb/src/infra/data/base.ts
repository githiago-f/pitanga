import axios from 'axios';
import { restConfig } from '../../app/config/rest.config';

export const apiBase = axios.create({
    baseURL: restConfig.baseURL,
    timeout: 30000,
    validateStatus: (status) => [200, 404].includes(status)
});
