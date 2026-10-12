import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { LocalStorageService, MemoryStorageService } from '@shared/services/storage.service';
import { BASE_URL } from './base-url-interceptor';
import { tenantInterceptor } from './tenant-interceptor';

describe('TenantInterceptor', () => {
  let http: HttpClient;
  let requests: HttpTestingController;
  let storage: LocalStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [
      { provide: BASE_URL, useValue: 'https://api.neritechauto.com.br/api' },
      { provide: LocalStorageService, useClass: MemoryStorageService },
      provideHttpClient(withInterceptors([tenantInterceptor])),
      provideHttpClientTesting(),
    ] });
    http = TestBed.inject(HttpClient);
    requests = TestBed.inject(HttpTestingController);
    storage = TestBed.inject(LocalStorageService);
    storage.set('tenantId', '7');
  });

  afterEach(() => requests.verify());

  it('does not send the tenant to unrelated external APIs', () => {
    const url = 'https://other.example/api/items';
    http.get(url).subscribe();
    const request = requests.expectOne(url);
    expect(request.request.headers.has('X-Tenant-Id')).toBeFalse();
    request.flush({});
  });

  it('does not reuse a previous tenant during refresh', () => {
    http.post('/api/auth/refresh', {}).subscribe();
    const request = requests.expectOne('/api/auth/refresh');
    expect(request.request.headers.has('X-Tenant-Id')).toBeFalse();
    request.flush({});
  });
});
