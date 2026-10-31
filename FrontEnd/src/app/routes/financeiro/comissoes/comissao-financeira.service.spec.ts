import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { LocalStorageService } from '@shared/services/storage.service';
import { environment } from '../../../../environments/environment';
import { ComissaoFinanceiraService } from './comissao-financeira.service';

describe('ComissaoFinanceiraService - empresa e pagamento', () => {
  let service: ComissaoFinanceiraService;
  let http: HttpTestingController;
  beforeEach(() => {
    const storage = jasmine.createSpyObj<LocalStorageService>('LocalStorageService', ['get', 'has']);
    storage.has.and.callFake((key: string) => key === 'empresaId');
    storage.get.and.callFake((key: string) => key === 'empresaId' ? '12' : null);
    TestBed.configureTestingModule({ providers: [
      provideHttpClient(), provideHttpClientTesting(),
      { provide: LocalStorageService, useValue: storage }
    ] });
    service = TestBed.inject(ComissaoFinanceiraService);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());
  it('aplica tenant na consulta e em todas as transições, sem gerar contas a pagar', () => {
    service.listar({ nome: 'Maria', paga: false }, 0, 20).subscribe();
    const search = http.expectOne(r => r.url === `${environment.baseUrl}/v1/financeiro/comissoes`);
    expect(search.request.params.get('empresaId')).toBe('12');
    expect(search.request.params.get('paga')).toBe('false');
    expect(search.request.params.get('nome')).toBe('Maria');
    expect(search.request.headers.get('X-Tenant-Id')).toBe('12');
    search.flush({ content: [], totalElements: 0, totalPages: 0 });

    service.aprovar(8).subscribe();
    const approve = http.expectOne(r => r.url.endsWith('/comissoes/8/aprovar'));
    expect(approve.request.params.get('empresaId')).toBe('12');
    approve.flush({ id: 8, aprovado: true });

    service.quitar(8, '2026-10-31').subscribe();
    const pay = http.expectOne(r => r.url.endsWith('/comissoes/8/quitar'));
    expect(pay.request.body.gerarContaPagar).toBe(false);
    expect(pay.request.params.get('empresaId')).toBe('12');
    pay.flush({ id: 8, pago: true });

    service.estornar(8).subscribe();
    const reverse = http.expectOne(r => r.url.endsWith('/comissoes/8/estornar'));
    expect(reverse.request.params.get('empresaId')).toBe('12');
    reverse.flush({ id: 8, pago: false });
  });
  it('envia todos os filtros da tela real e usa o número visível da OS', () => {
    service.listar({ nome:'JOÃO', numeroOs:'00000015/00', cliente:'RICARDO',
      placa:'KOB5E58', paga:false, inicio:'2026-10-01', fim:'2026-10-31' }).subscribe();
    const req=http.expectOne(r=>r.url.endsWith('/financeiro/comissoes'));
    expect(req.request.params.get('numeroOs')).toBe('00000015/00');
    expect(req.request.params.get('cliente')).toBe('RICARDO');
    expect(req.request.params.get('placa')).toBe('KOB5E58');
    expect(req.request.params.get('inicio')).toBe('2026-10-01');
    expect(req.request.params.get('fim')).toBe('2026-10-31');
    expect(req.request.headers.get('X-Tenant-Id')).toBe('12');
    req.flush({content:[],totalElements:0,totalPages:0});
  });

  it('quita diretamente e em lote com os parâmetros financeiros reais', () => {
    const opcoes={ gerarContaPagar:true, planoContaId:6, tipoDocumento:'OUTROS' };
    service.processarQuitacao(21,'2026-10-31','Teste',opcoes).subscribe();
    const individual=http.expectOne(r=>r.url.endsWith('/comissoes/21/processar-quitacao'));
    expect(individual.request.body).toEqual(jasmine.objectContaining({
      dataPagamento:'2026-10-31',gerarContaPagar:true,planoContaId:6,tipoDocumento:'OUTROS'
    }));
    expect(individual.request.params.get('empresaId')).toBe('12');
    individual.flush({id:21,pago:true});
    service.quitarLote([21,22],'2026-10-31',undefined,opcoes).subscribe();
    const lote=http.expectOne(r=>r.url.endsWith('/comissoes/quitar-lote'));
    expect(lote.request.body.ids).toEqual([21,22]);
    expect(lote.request.body.dados.planoContaId).toBe(6);
    expect(lote.request.headers.get('X-Tenant-Id')).toBe('12');
    lote.flush([]);
  });

  it('carrega apenas planos de contas válidos para quitação', () => {
    service.listarPlanos().subscribe(p => expect(p.map(x=>x.id)).toEqual([10]));
    const req=http.expectOne(r=>r.url.endsWith('/financeiro/plano-contas'));
    expect(req.request.params.get('empresaId')).toBe('12');
    req.flush({content:[
      {id:10,nome:'Comissões',codigo:'4.1',ativo:true,aceitaLancamento:true},
      {id:11,nome:'Inativo',codigo:'4.2',ativo:false,aceitaLancamento:true},
      {id:12,nome:'Sintético',codigo:'4.3',ativo:true,aceitaLancamento:false}
    ]});
  });

});
