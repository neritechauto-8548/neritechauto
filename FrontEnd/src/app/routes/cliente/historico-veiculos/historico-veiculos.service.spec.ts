import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '@env/environment';
import { HistoricoVeiculosService } from './historico-veiculos.service';

describe('Histórico de veículos — contrato HTTP isolado por empresa', () => {
  const root = environment.baseUrl + '/v1/historicos-veiculos';
  let api: HistoricoVeiculosService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()]
    });
    api = TestBed.inject(HistoricoVeiculosService);
    http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());

  it('lista com busca, ordenação e paginação sem permitir empresaId do navegador', () => {
    let total = -1;
    api.listar('Volkswagen', 'ZA', 2, 10).subscribe(x => total = x.totalElements);
    const req = http.expectOne(x => x.url === root);
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('busca')).toBe('Volkswagen');
    expect(req.request.params.get('ordem')).toBe('ZA');
    expect(req.request.params.get('page')).toBe('2');
    expect(req.request.params.get('size')).toBe('10');
    expect(req.request.params.has('empresaId')).toBeFalse();
    req.flush({content:[],totalElements:17,totalPages:2,number:2,size:10});
    expect(total).toBe(17);
  });

  it('consulta veículo pela rota de detalhe sem retornar histórico inventado', () => {
    let nome = '';
    api.veiculo(123).subscribe(v => nome = v.cliente);
    const req = http.expectOne(root+'/123');
    expect(req.request.method).toBe('GET');
    expect(req.request.params.keys()).toEqual([]);
    req.flush({id:123,clienteId:8,cliente:'Cliente real',placa:'ABC1D23'});
    expect(nome).toBe('Cliente real');
  });

  it('distingue OS fechadas/autorizadas e envia pesquisa somente ao tenant da sessão', () => {
    let count=0;
    api.ordens(20,'troca de óleo','ENTRADA',true,1,10)
      .subscribe(page => count=page.totalElements);
    const req=http.expectOne(x => x.url===root+'/20/ordens');
    expect(req.request.params.get('busca')).toBe('troca de óleo');
    expect(req.request.params.get('ordenar')).toBe('ENTRADA');
    expect(req.request.params.get('incluirAutorizados')).toBe('true');
    expect(req.request.params.get('page')).toBe('1');
    expect(req.request.params.has('empresaId')).toBeFalse();
    req.flush({content:[],totalElements:0,totalPages:0,number:1,size:10});
    expect(count).toBe(0);
  });

  it('emite PDF como blob, preservando exatamente os três filtros do modal', () => {
    let result:Blob|null=null;
    api.baixarRelatorio({veiculoId:16,soAutorizadas:true,mostrarComissionado:true,
      mostrarValores:false, busca:'Gol'}).subscribe(x=>result=x);
    const req=http.expectOne(root+'/relatorio?soAutorizadas=true&mostrarComissionado=true&mostrarValores=false&veiculoId=16&busca=Gol');
    expect(req.request.method).toBe('GET');
    expect(req.request.responseType).toBe('blob');
    expect(req.request.params.has('empresaId')).toBeFalse();
    req.flush(new Blob(['%PDF-1.4'],{type:'application/pdf'}));
    expect(result).toEqual(jasmine.any(Blob));
    expect((result as unknown as Blob).type).toBe('application/pdf');
  });

  it('propaga 404/403 do backend em vez de apresentar dados fictícios', () => {
    let responseStatus=0;
    api.veiculo(9999).subscribe({error:err=>responseStatus=err.status});
    http.expectOne(root+'/9999')
      .flush({message:'Sem vínculo com a empresa'},{status:404,statusText:'Not Found'});
    expect(responseStatus).toBe(404);
  });
});
