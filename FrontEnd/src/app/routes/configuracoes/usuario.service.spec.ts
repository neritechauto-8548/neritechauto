import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';
import { UsuarioService } from './usuario.service';

describe('UsuarioService ADM-003', () => {
  let service: UsuarioService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(UsuarioService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('uses the existing users route to send an invitation', () => {
    const payload = { nomeCompleto: 'Ana', email: 'ANA@EXEMPLO.COM', ativo: true,
      bloqueado: false, funcoesIds: [] };
    service.invite(payload).subscribe(result => expect(result.id).toBe(9));
    const request = http.expectOne(`${environment.baseUrl}/usuarios/convites`);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(payload);
    request.flush({ id: 9 });
  });
});
