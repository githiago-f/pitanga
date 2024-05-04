import { Auth } from '../config/auth.config';

const hasPersistedCookie = true;

export async function loginAction() {
  if(hasPersistedCookie) {
    return;
  }

  const thisAppUrl = new URL(Auth.CALLBACK_ENDPOINT, Auth.CLIENT_DOMAIN);
  const url = new URL(Auth.AUTH_ENDPOINT, Auth.AUTH_SERVICE);
  url.searchParams.set('response_type', 'code');
  url.searchParams.set('client_id', Auth.AUTH_CLIENT);
  url.searchParams.set('scope', Auth.AUTH_SCOPES);
  url.searchParams.set('redirect_uri', thisAppUrl.toString());
  console.log(url.toString());

  window.location.href = url.toString();
}
