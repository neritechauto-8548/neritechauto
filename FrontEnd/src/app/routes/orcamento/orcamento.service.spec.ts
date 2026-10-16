import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { HotToastService } from '@ngxpert/hot-toast';
import { TokenService } from '@core/authentication';
import { LocalStorageService, MemoryStorageService } from '@shared/services/storage.service';
import { BASE_URL, baseUrlInterceptor } from '@core/interceptors/base-url-interceptor';
import { apiInterceptor } from '@core/interceptors/api-interceptor';
import { tokenInterceptor } from '@core/interceptors/token-interceptor';
import { environment } from '../../../environments/environment';
import { MetodoEnvio, OrcamentoRequest, OrcamentoResponse, StatusOrcamento, TipoOrcamento } from './models/orcamento.models';
import { OrcamentoService } from './orcamento.service';

describe('OrcamentoService - contrato HTTP BASE-02', () => {
  let service: OrcamentoService;
  let httpMock: HttpTestingController;
  const url = `${environment.baseUrl}/v1/orcamentos`;
  const dto: OrcamentoRequest = {
    ordemServicoId: 71,
    numeroOrcamento: 'ORC-71',
    tipoOrcamento: TipoOrcamento.SIMPLES,
    status: StatusOrcamento.RASCUNHO,
    metodoEnvio: MetodoEnvio.PRESENCIAL,
    valorTotal: 149.90,
  };
  const response = {
    id: 11,
    ...dto,
    tipoOrcamento: TipoOrcamento.SIMPLES,
    status: StatusOrcamento.RASCUNHO,
  } as OrcamentoResponse;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([baseUrlInterceptor, tokenInterceptor, apiInterceptor])),
        provideHttpClientTesting(),
        { provide: BASE_URL, useValue: environment.baseUrl },
        { provide: LocalStorageService, useClass: MemoryStorageService },
        { provide: HotToastService, useValue: { error: jasmine.createSpy('error') } },
      ],
    });
    service = TestBed.inject(OrcamentoService);
    httpMock = TestBed.inject(HttpTestingController);
    TestBed.inject(TokenService).set({ access_token: 'jwt-teste', token_type: 'Bearer' });
  });

  afterEach(() => httpMock.verify());

  it('lista por OS na rota real, com JWT e envelope ApiResponse desempacotado', () => {
    let data: OrcamentoResponse[] | undefined;
    service.getByOrdemServicoId(71).subscribe(value => data = value);
    const request = httpMock.expectOne(`${url}/ordem-servico/71`);
    expect(request.request.method).toBe('GET');
    expect(request.request.headers.get('Authorization')).toBe('Bearer jwt-teste');
    request.flush({ data: [response], code: null });
    expect(data?.[0].tipoOrcamento).toBe(TipoOrcamento.SIMPLES);
    expect(data?.[0].status).toBe(StatusOrcamento.RASCUNHO);
    expect(data?.[0].metodoEnvio).toBe(MetodoEnvio.PRESENCIAL);
  });

  it('cria na rota real, serializando enum e valores do DTO', () => {
    let saved: OrcamentoResponse | undefined;
    service.create(dto).subscribe(value => saved = value);
    const request = httpMock.expectOne(url);
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual(dto);
    request.flush({ data: response, code: null });
    expect(saved?.id).toBe(11);
  });

  it('edita e consulta por ID sem acrescentar outro /api', () => {
    service.update(11, dto).subscribe(value => expect(value.id).toBe(11));
    const update = httpMock.expectOne(`${url}/11`);
    expect(update.request.method).toBe('PUT');
    expect(update.request.body).toEqual(dto);
    update.flush({ data: response, code: null });

    service.getById(11).subscribe(value => expect(value.id).toBe(11));
    const get = httpMock.expectOne(`${url}/11`);
    expect(get.request.method).toBe('GET');
    get.flush({ data: response, code: null });
  });

  it('mantém o DELETE legado e seu retorno vazio', () => {
    let completed = false;
    service.delete(11).subscribe({ complete: () => completed = true });
    const request = httpMock.expectOne(`${url}/11`);
    expect(request.request.method).toBe('DELETE');
    request.flush(null, { status: 204, statusText: 'No Content' });
    expect(completed).toBeTrue();
  });

  for (const status of [401, 403, 422, 409]) {
    it(`preserva HTTP ${status} para o tratamento global da aplicação`, () => {
      let receivedStatus: number | undefined;
      service.update(11, dto).subscribe({
        error: error => receivedStatus = error.status,
      });
      httpMock.expectOne(`${url}/11`).flush(
        { message: 'Erro de contrato' },
        { status, statusText: 'Error' },
      );
      expect(receivedStatus).toBe(status);
    });
  }
});
