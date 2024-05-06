const env = import.meta.env;

export const Auth = {
  auth_service: {
    CLIENT: env.AUTH_CLIENT ?? 'pitanga',
    AUTHORIZE: env.AUTHORIZE_URL ?? 'https://localhost:8444/realms/master/protocol/openid-connect/auth',
    TOKEN: env.TOKEN_URL ?? 'https://localhost:8444/realms/master/protocol/openid-connect/token',
    SCOPES: (env.AUTH_SCOPES as string ?? 'profile').split(',').map(i => i.trim()),
  },
  client: {
    BASE_PATH: env.BASE_URL ?? '/pitanga-tcc',
    CALLBACK_ENDPOINT: '/login/valid',
    DOMAIN() { return new URL(this.BASE_PATH, env.CLIENT_HOST ?? 'http://localhost:3000'); },
    get CALLBACK_URL() {
      const url = this.DOMAIN();
      url.pathname += this.CALLBACK_ENDPOINT;
      return url.toString();
    }
  }
};
