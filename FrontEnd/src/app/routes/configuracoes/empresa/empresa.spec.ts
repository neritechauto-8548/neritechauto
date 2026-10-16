import { HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MessageService } from 'primeng/api';
import { of, throwError } from 'rxjs';
import { EmpresaConfig } from './empresa';
import { EmpresaService } from './services/empresa.service';
import { SettingsService } from '@core/bootstrap/settings.service';
import { LocalStorageService } from '@shared/services/storage.service';
import { UtilService } from '@shared/services/util.service';
import { ConfirmationService } from '@shared/services/confirmation.service';

describe('ADM-001 — Empresa: carregamento seguro', () => {
  let api: jasmine.SpyObj<EmpresaService>;
  let page: EmpresaConfig;

  beforeEach(() => {
    api = jasmine.createSpyObj<EmpresaService>('EmpresaService', [
      'getEmpresa', 'getEnderecos', 'getConfigEmpresa', 'getConfigFiscal',
      'getConfigOficina', 'getConfigEmail', 'getConfigWhatsapp', 'getConfigSms',
      'updateEmpresa', 'saveConfigEmpresa', 'saveConfigFiscal', 'saveConfigOficina',
      'saveEndereco', 'saveConfigEmail', 'getLogo'
    ]);
    api.getEmpresa.and.returnValue(of({
      id: 12, nomeFantasia: 'Empresa A', razaoSocial: 'Empresa A Ltda',
      cnpj: '11222333000181'
    }));
    api.getEnderecos.and.returnValue(of([]));
    api.getConfigEmpresa.and.returnValue(of({ empresaId: 12 } as any));
    api.getConfigFiscal.and.returnValue(of({ empresaId: 12 } as any));
    api.getConfigOficina.and.returnValue(of({ empresaId: 12, corTema: 'light' } as any));
    api.getConfigEmail.and.returnValue(of({ empresaId: 12, ativo: true } as any));
    api.getConfigWhatsapp.and.returnValue(of(null));
    api.getConfigSms.and.returnValue(of(null));

    TestBed.configureTestingModule({
      providers: [
        { provide: EmpresaService, useValue: api },
        { provide: SettingsService, useValue: { setTheme: jasmine.createSpy('setTheme') } },
        { provide: MessageService, useValue: { add: jasmine.createSpy('add') } },
        { provide: LocalStorageService, useValue: {
          has: (key: string) => key === 'tenantId',
          get: () => 12
        } },
        { provide: UtilService, useValue: {} },
        { provide: ConfirmationService, useValue: {} }
      ]
    });
    page = TestBed.runInInjectionContext(() => new EmpresaConfig());
  });

  it('blocks editing when fiscal configuration returns 403', () => {
    api.getConfigFiscal.and.returnValue(throwError(() => new HttpErrorResponse({ status: 403 })));
    page.ngOnInit();
    expect(page.empresaCarregada).toBeFalse();
    expect(page.erroCarregamento).toContain('Sem permissão');
    page.salvar();
    expect(api.updateEmpresa).not.toHaveBeenCalled();
    expect(api.saveConfigFiscal).not.toHaveBeenCalled();
  });

  it('blocks editing offline instead of accepting partial blank settings', () => {
    api.getConfigOficina.and.returnValue(throwError(() => new HttpErrorResponse({ status: 0 })));
    page.ngOnInit();
    expect(page.empresaCarregada).toBeFalse();
    expect(page.erroCarregamento).toContain('Sem conexão');
    expect(page.loading).toBeFalse();
  });

  it('permits missing optional configuration while protecting unavailable email settings', () => {
    api.getConfigFiscal.and.returnValue(throwError(() => new HttpErrorResponse({ status: 404 })));
    api.getConfigEmail.and.returnValue(throwError(() => new HttpErrorResponse({ status: 403 })));
    page.ngOnInit();
    expect(page.empresaCarregada).toBeTrue();
    expect(page.emailConfigWritable).toBeFalse();
  });

  it('allows first-time email configuration after a confirmed 404', () => {
    api.getConfigEmail.and.returnValue(throwError(() => new HttpErrorResponse({ status: 404 })));
    page.ngOnInit();
    expect(page.empresaCarregada).toBeTrue();
    expect(page.emailConfigWritable).toBeTrue();
  });

  it('rejects a mismatched tenant response', () => {
    api.getEmpresa.and.returnValue(of({
      id: 33, nomeFantasia: 'Empresa B', razaoSocial: 'Empresa B',
      cnpj: '11222333000181'
    }));
    page.ngOnInit();
    expect(page.empresaCarregada).toBeFalse();
    expect(page.erroCarregamento).toContain('empresa autenticada');
  });
});
