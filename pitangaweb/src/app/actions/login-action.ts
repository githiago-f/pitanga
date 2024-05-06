import axios from 'axios';
import { Auth } from '../config/auth.config';
import { redirect } from 'react-router-dom';

export async function loginAction() {
  const isCallback = window.location.href.includes(Auth.client.CALLBACK_URL);
  const authToken = window.sessionStorage.getItem('access_token');
  if(isCallback || authToken !== null) {
    return;
  }
  const authorize = new URL(Auth.auth_service.AUTHORIZE);
  authorize.searchParams.set('client_id', Auth.auth_service.CLIENT);
  authorize.searchParams.set('response_type', 'code');
  authorize.searchParams.set('redirect_uri', Auth.client.CALLBACK_URL);

  window.location.href = authorize.toString();
  return null;
}

export async function requestAuthToken() {
  const url = new URL(window.location.href);
  const code = url.searchParams.get('code');
  if(!code) {
    return redirect('/');
  }
  const res = await axios.post(Auth.auth_service.TOKEN, {
    grant_type: 'authorization_code',
    code,
    redirect_uri: Auth.client.CALLBACK_URL,
    client_id: Auth.auth_service.CLIENT,
    client_secret: ''
  }, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    withCredentials: true
  });

  console.log(res.data);

  window.sessionStorage.setItem('access_token', res.data.access_token);
  window.sessionStorage.setItem('refresh_token', res.data.refresh_token);
  window.sessionStorage.setItem('expires_in', res.data.expires_in);
  return redirect('/');
}
