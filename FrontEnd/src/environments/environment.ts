// This file can be replaced during build by using the fileReplacements configuration in angular.json.
export const environment = {
  production: false,
  baseUrl: '/api',
  supportUrl: 'https://neritechauto.atlassian.net/servicedesk/customer/portal/1',
  useHash: false,
  // Prévia visual somente quando solicitada explicitamente no ambiente local.
  uxPreview: typeof window !== 'undefined' && new URLSearchParams(window.location.search).get('uxPreview') === '1',
};
