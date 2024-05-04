const env = import.meta.env;

export const Auth = {
  CALLBACK_ENDPOINT: '/login/oauth2/code',
  AUTH_CLIENT: env.AUTH_CLIENT ?? 'pitangaweb',
  AUTH_SERVICE: env.AUTH_URL ?? 'https://localhost:8444',
  AUTH_ENDPOINT: env.AUTH_ENDPOINT ?? '/oauth2/authorize',
  CLIENT_DOMAIN: env.APP_URL ?? 'http://localhost:3000/pitanga-tcc',
  AUTH_SCOPES: env.AUTH_SCOPES ?? 'profile'
};
