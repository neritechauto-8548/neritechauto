// https://angular.io/guide/build#proxying-to-a-backend-server

const PROXY_CONFIG = {
  // Exemplo original
  '/users/**': {
    target: 'https://api.github.com',
    changeOrigin: true,
    secure: false,
    logLevel: 'debug',
  },

  // Backend local da aplicação
  '/api/**': {
    target: 'http://localhost:8080',
    changeOrigin: true,
    secure: false,
    logLevel: 'debug',
    // Mantém o prefixo /api no destino: /api/foo -> http://localhost:8080/api/foo
    // Se o backend não tiver o prefixo /api, descomente a linha abaixo:
    // pathRewrite: { '^/api': '' },
  },
};

module.exports = PROXY_CONFIG;
