import { HttpRequest, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { tenantInterceptor } from './tenant-interceptor';

describe('tenantInterceptor', () => {
  it('nao deriva nem envia autoridade de tenant a partir do navegador', done => {
    localStorage.setItem('tenantId', '99');
    localStorage.setItem('empresaId', '88');

    const request = new HttpRequest('GET', '/api/v1/orcamentos');

    tenantInterceptor(request, forwardedRequest => {
      expect(forwardedRequest).toBe(request);
      expect(forwardedRequest.headers.has('X-Tenant-Id')).toBeFalse();
      return of(new HttpResponse({ status: 200 }));
    }).subscribe(() => {
      localStorage.removeItem('tenantId');
      localStorage.removeItem('empresaId');
      done();
    });
  });
});
