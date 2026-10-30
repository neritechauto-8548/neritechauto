import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { HistoricoVeiculos } from './historico-veiculos';
import { HistoricoVeiculosService } from './historico-veiculos.service';
import { LocalStorageService } from '@shared/services/storage.service';

describe('Histórico de veículos — interface de referência', () => {
  let component:HistoricoVeiculos;
  let fixture:ComponentFixture<HistoricoVeiculos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports:[HistoricoVeiculos],
      providers:[
        provideRouter([]),
        {provide:LocalStorageService,useValue:{get:()=>null}},
        {provide:HistoricoVeiculosService,useValue:{
          listar:()=>of({content:[],totalElements:0,totalPages:0,number:0,size:10})
        }}
      ]
    }).compileComponents();
    fixture=TestBed.createComponent(HistoricoVeiculos);
    component=fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(()=>fixture.destroy());

  it('exibe ações de relatórios e colunas e estado vazio sem registros inventados',()=>{
    const text=fixture.nativeElement.textContent as string;
    expect(text).toContain('Histórico de veículos');
    expect(text).toContain('Relatórios');
    expect(text).toContain('Colunas');
    expect(text).toContain('Nenhum veículo encontrado');
  });

  it('bloqueia a remoção da coluna Cliente, mas permite desativar Placa',()=>{
    component.openColumns();
    fixture.detectChanges();
    const modal=fixture.nativeElement.querySelector('.hv-columns-dialog') as HTMLElement;
    expect(modal.getAttribute('aria-modal')).toBe('true');
    component.toggleColumn('cliente');
    expect(component.draftColumns).toEqual(['cliente','placa']);
    component.toggleColumn('placa');
    expect(component.draftColumns).toEqual(['cliente']);
    component.restoreDefault();
    expect(component.draftColumns).toEqual(['cliente','placa']);
  });

  it('abre modal com três filtros, expande e fecha usando ESC',()=>{
    component.openReport();
    fixture.detectChanges();
    const text=fixture.nativeElement.textContent as string;
    expect(text).toContain('Somente O.S. autorizadas');
    expect(text).toContain('Exibir produtivo comissionado');
    expect(text).toContain('Exibir valores');
    const fullscreen=fixture.nativeElement.querySelector('.hv-report-expand') as HTMLButtonElement;
    expect(fullscreen).not.toBeNull();
    fullscreen.click();
    fixture.detectChanges();
    expect(component.reportFullscreen).toBeTrue();
    expect(fixture.nativeElement.querySelector('.hv-report-fullscreen')).not.toBeNull();
    const event=new KeyboardEvent('keydown',{key:'Escape',cancelable:true});
    component.onEscape(event);
    fixture.detectChanges();
    expect(component.reportOpen).toBeFalse();
  });
});
