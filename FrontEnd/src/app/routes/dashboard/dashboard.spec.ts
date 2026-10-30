import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { AuthService } from '@core/authentication';
import { of } from 'rxjs';
import { Dashboard } from './dashboard';
import { DashboardService, DashboardDTO } from './dashboard.service';

describe('MASTER — Visão geral de referência', () => {
  let fixture: ComponentFixture<Dashboard>;
  let service: jasmine.SpyObj<DashboardService>;

  const payload = {
    totalClientes: 0, osAbertas: 0, osEmAndamento: 0, osConcluidas: 0, osCanceladas: 0,
    faturamentoMes: 0, despesasMes: 0, lucroMes: 0, ticketMedio: 0,
    contasReceber: 0, contasPagar: 0, valoresVencidos: 0, veiculosEmAtraso: 0,
    historicoFaturamento: [], historicoDespesas: [], historicoMeses: [],
    abertosMes: 0, abertosTotal: 0, autorizadosMes: 0, autorizadosTotal: 0,
    canceladosMes: 0, canceladosTotal: 0, fechadosMes: 0, fechadosTotal: 0,
    entradasVeiculosMes: 0, saidasVeiculosMes: 0,
  } satisfies DashboardDTO;

  beforeEach(() => {
    service = jasmine.createSpyObj<DashboardService>('DashboardService', ['getDashboardData']);
    service.getDashboardData.and.returnValue(of(payload));
    TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [
        provideRouter([]),
        { provide: DashboardService, useValue: service },
        { provide: AuthService, useValue: { user: () => of({ id: 1, empresaId: 2, name: 'Comercial Teste' }) } },
      ],
    });
    fixture = TestBed.createComponent(Dashboard);
    fixture.detectChanges();
  });

  it('exibe os dois cartões e valores zerados sem inventar resultados', () => {
    const element: HTMLElement = fixture.nativeElement;
    expect(element.querySelectorAll('.master-widget').length).toBe(2);
    expect(element.textContent).toContain('Financeiro');
    expect(element.textContent).toContain('Ordens de serviço');
    expect(element.textContent).toContain('R$');
    expect(element.textContent).toContain('0,00');
    expect(element.textContent).toContain('Ticket médio do mês');
    expect(element.textContent).not.toContain('3.037,96');
    expect(element.textContent).toContain('Comercial');
  });

  it('atualiza os indicadores apenas por meio do serviço real', () => {
    const button: HTMLButtonElement = fixture.nativeElement.querySelector(
      '[aria-label="Atualizar Financeiro"]'
    );
    expect(service.getDashboardData).toHaveBeenCalledTimes(1);
    button.click();
    expect(service.getDashboardData).toHaveBeenCalledTimes(2);
  });

  it('permite mostrar e ocultar widgets sem alterar dados da API', () => {
    const component = fixture.componentInstance;
    component.widgetFinanceiro = false;
    component.widgetOrdens = false;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('.master-widget').length).toBe(0);
    expect(fixture.nativeElement.textContent).toContain('Nenhum componente selecionado');
    component.showAddComponent = true;
    component.widgetClientes = true;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelectorAll('.master-widget').length).toBe(1);
    expect(fixture.nativeElement.textContent).toContain('Clientes ativos');
    expect(service.getDashboardData).toHaveBeenCalledTimes(1);
  });

  it('preserva a navegação entre as quatro visões de gestão', () => {
    const component = fixture.componentInstance;
    expect(component.viewOptions.map(view => view.value)).toEqual([
      'padrao', 'gerencial', 'financeiro', 'orcamento'
    ]);
  });
});
