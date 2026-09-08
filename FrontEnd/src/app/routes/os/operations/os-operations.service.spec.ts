import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { OsOperationsService } from './os-operations.service';

describe('OsOperationsService contract', () => {
  let service: OsOperationsService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OsOperationsService, provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(OsOperationsService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('lista escopo sem enviar empresa ou tenant pelo navegador', () => {
    service.listProducts(12).subscribe();
    const products = http.expectOne(req => req.url.endsWith('/v1/itens-os-produtos/ordem-servico/12'));
    expect(products.request.method).toBe('GET');
    expect(products.request.params.keys()).toEqual([]);
    products.flush([]);

    service.listServices(12).subscribe();
    const services = http.expectOne(req => req.url.endsWith('/v1/itens-os-servicos/ordem-servico/12'));
    expect(services.request.method).toBe('GET');
    expect(services.request.params.keys()).toEqual([]);
    services.flush([]);
  });

  it('lista diagnóstico, checklist e evidências sem tenant controlado pelo cliente', () => {
    service.listDiagnostics(12).subscribe();
    const diagnostics = http.expectOne(req => req.url.endsWith('/v1/diagnosticos/ordem-servico/12'));
    expect(diagnostics.request.params.keys()).toEqual([]);
    diagnostics.flush([]);

    service.listChecklist(12).subscribe();
    const checklist = http.expectOne(req => req.url.endsWith('/v1/ordens-servico/os-checklist/ordem-servico/12'));
    expect(checklist.request.params.keys()).toEqual([]);
    checklist.flush([]);

    service.listEvidence(12).subscribe();
    const evidence = http.expectOne(req => req.url.endsWith('/v1/ordens-servico/12/fotos'));
    expect(evidence.request.params.keys()).toEqual([]);
    evidence.flush([]);
  });

  it('lista catálogo de checklist sem empresaId e aplica modelo somente à OS', () => {
    service.listChecklistModels().subscribe();
    const catalog = http.expectOne(req => req.url.endsWith('/v1/ordens-servico/checklists'));
    expect(catalog.request.method).toBe('GET');
    expect(catalog.request.params.get('empresaId')).toBeNull();
    expect(catalog.request.params.get('size')).toBe('100');
    catalog.flush({ content: [], totalElements: 0, totalPages: 0, number: 0, size: 100 });

    service.applyChecklist(12, 5).subscribe();
    const apply = http.expectOne(req => req.url.endsWith('/v1/ordens-servico/os-checklist'));
    expect(apply.request.method).toBe('POST');
    expect(apply.request.body).toEqual({ ordemServicoId: 12, checklistId: 5 });
    expect(apply.request.body.empresaId).toBeUndefined();
    apply.flush([]);
  });

  it('cria diagnóstico com ordem da rota, sem empresaId no payload', () => {
    service.createDiagnostic({
      ordemServicoId: 12,
      problemaIdentificado: 'Ruído no eixo dianteiro',
      urgencia: 'MEDIA',
      impactoSeguranca: false,
      impactoDirigibilidade: true,
    }).subscribe();

    const request = http.expectOne(req => req.url.endsWith('/v1/diagnosticos'));
    expect(request.request.method).toBe('POST');
    expect(request.request.body.ordemServicoId).toBe(12);
    expect(request.request.body.empresaId).toBeUndefined();
    expect(request.request.body.tenantId).toBeUndefined();
    request.flush({ id: 1, ...request.request.body });
  });

  it('atualiza checklist sem incluir contexto de empresa no corpo', () => {
    service.updateChecklistItem({
      id: 7,
      ordemServicoId: 12,
      checklistModeloId: 2,
      descricao: 'Confirmar torque',
      feito: false,
      ordem: 1,
    }, true).subscribe();

    const request = http.expectOne(req => req.url.endsWith('/v1/ordens-servico/os-checklist/7'));
    expect(request.request.method).toBe('PUT');
    expect(request.request.body).toEqual({ descricao: 'Confirmar torque', feito: true, ordem: 1 });
    expect(request.request.body.empresaId).toBeUndefined();
    request.flush({ id: 7, ordemServicoId: 12, descricao: 'Confirmar torque', feito: true, ordem: 1 });
  });
});
