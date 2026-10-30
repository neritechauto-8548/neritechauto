export const environment = {
  production: true,
  baseUrl: 'https://api.neritechauto.com.br/api',
  supportUrl: 'https://neritechauto.atlassian.net/servicedesk/customer/portal/1',
  useHash: false,
  // Temporário para a branch master: apenas prévia visual explícita.
  // O interceptor de prévia bloqueia TODOS os acessos a APIs, inclusive GETs e gravações.
  uxPreview: typeof window !== 'undefined' &&
    new URLSearchParams(window.location.search).get('uxPreview') === '1',
};
