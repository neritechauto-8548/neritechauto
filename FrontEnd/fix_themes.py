import os

def update_file(path, replacements):
    with open(path, 'r', encoding='utf8') as f:
        content = f.read()
    for old, new in replacements:
        content = content.replace(old, new)
    with open(path, 'w', encoding='utf8') as f:
        f.write(content)

settings_ts = 'src/app/core/bootstrap/settings.service.ts'
if os.path.exists(settings_ts):
    update_file(settings_ts, [
        ("import { BehaviorSubject } from 'rxjs';", "import { PrimeNG } from 'primeng/config';\nimport Aura from '@primeuix/themes/aura';\nimport Lara from '@primeuix/themes/lara';\nimport { BehaviorSubject } from 'rxjs';"),
        ("private readonly dir = inject(AppDirectionality);", "private readonly dir = inject(AppDirectionality);\n  private readonly primengConfig = inject(PrimeNG);"),
        ("applyThemeColors() {", "applyThemeColors() {\n    if (this.options.presetTheme === 'Lara') {\n      this.primengConfig.theme.set({ preset: Lara });\n    } else {\n      this.primengConfig.theme.set({ preset: Aura });\n    }")
    ])
    print("Updated settings.service.ts")

colab_ts = 'src/app/routes/configuracoes/colaboradores/cadastro-colaborador/cadastro-colaborador.ts'
if os.path.exists(colab_ts):
    update_file(colab_ts, [
        ("selectSurfaceColor(color: string) {", """presetOptions = [
    { name: 'Aura', value: 'Aura' },
    { name: 'Lara', value: 'Lara' }
  ];

  selectPreset(preset: string) {
    this.usuarioForm.preferencias.presetTheme = preset;
    this.settings.setOptions({ ...this.settings.options, ...this.usuarioForm.preferencias });
  }

  selectMenuTheme(themeClass: string) {
    this.usuarioForm.preferencias.menuThemeClass = themeClass;
    this.settings.setOptions({ ...this.settings.options, ...this.usuarioForm.preferencias });
  }

  selectTopbarTheme(themeClass: string) {
    this.usuarioForm.preferencias.topbarThemeClass = themeClass;
    this.settings.setOptions({ ...this.settings.options, ...this.usuarioForm.preferencias });
  }

  selectSurfaceColor(color: string) {""")
    ])
    print("Updated cadastro-colaborador.ts")

colab_html = 'src/app/routes/configuracoes/colaboradores/cadastro-colaborador/cadastro-colaborador.html'
if os.path.exists(colab_html):
    update_file(colab_html, [
        ("</ng-container>\n    </ng-container>", """
        <!-- Tipo de Estilo (Preset) -->
        <p-panel styleClass="nt-panel mt-4">
          <ng-template pTemplate="header">
            <div class="nt-panel-hd">
              <span class="nt-panel-title">Estilo de Componentes (Aura/Lara)</span>
              <span class="nt-panel-sub">Escolha o visual base para tabelas, inputs e painéis</span>
            </div>
          </ng-template>
          
          <div class="flex flex-wrap gap-4 p-2">
            <button *ngFor="let opt of presetOptions" type="button" 
              (click)="selectPreset(opt.value)"
              [ngClass]="usuarioForm.preferencias.presetTheme === opt.value ? 'border-primary ring-2 ring-indigo-100 bg-indigo-50/50' : 'border-slate-200 bg-white hover:bg-slate-50'"
              class="flex items-center gap-3 px-5 py-3 border rounded-xl cursor-pointer transition-all duration-200 text-left outline-none min-w-[150px]">
              <div>
                <p class="text-xs font-bold text-slate-800">{{ opt.name }}</p>
                <p class="text-[10px] text-slate-500 mt-0.5">Estilo {{ opt.name }}</p>
              </div>
            </button>
          </div>
        </p-panel>

        <!-- Tema do Menu -->
        <p-panel styleClass="nt-panel mt-4">
          <ng-template pTemplate="header">
            <div class="nt-panel-hd">
              <span class="nt-panel-title">Tema do Menu (Sidebar)</span>
              <span class="nt-panel-sub">Escolha uma cor para o menu lateral</span>
            </div>
          </ng-template>
          
          <div class="flex flex-wrap gap-3.5 p-2">
            <div *ngFor="let opt of menuThemeOptions" 
              (click)="selectMenuTheme(opt.value)"
              [pTooltip]="opt.name"
              class="cursor-pointer group relative">
              <div 
                [style.background-color]="opt.hex"
                [ngClass]="usuarioForm.preferencias.menuThemeClass === opt.value ? 'ring-2 ring-offset-2 ring-indigo-500 scale-105' : 'border border-black/10'"
                class="w-9 h-9 rounded-full flex items-center justify-center shadow-sm transition-all duration-200 group-hover:scale-110 relative">
                <i *ngIf="usuarioForm.preferencias.menuThemeClass === opt.value" 
                  [ngClass]="opt.hex === '#ffffff' ? 'text-indigo-600' : 'text-white'"
                  class="pi pi-check text-xs font-bold"></i>
              </div>
            </div>
          </div>
        </p-panel>

        <!-- Tema da Barra Superior -->
        <p-panel styleClass="nt-panel mt-4">
          <ng-template pTemplate="header">
            <div class="nt-panel-hd">
              <span class="nt-panel-title">Tema da Barra Superior (Topbar)</span>
              <span class="nt-panel-sub">Escolha uma cor para a barra do topo</span>
            </div>
          </ng-template>
          
          <div class="flex flex-wrap gap-3.5 p-2">
            <div *ngFor="let opt of topbarThemeOptions" 
              (click)="selectTopbarTheme(opt.value)"
              [pTooltip]="opt.name"
              class="cursor-pointer group relative">
              <div 
                [style.background-color]="opt.hex"
                [ngClass]="usuarioForm.preferencias.topbarThemeClass === opt.value ? 'ring-2 ring-offset-2 ring-indigo-500 scale-105' : 'border border-black/10'"
                class="w-9 h-9 rounded-full flex items-center justify-center shadow-sm transition-all duration-200 group-hover:scale-110 relative">
                <i *ngIf="usuarioForm.preferencias.topbarThemeClass === opt.value" 
                  [ngClass]="opt.hex === '#ffffff' ? 'text-indigo-600' : 'text-white'"
                  class="pi pi-check text-xs font-bold"></i>
              </div>
            </div>
          </div>
        </p-panel>

      </ng-container>
    </ng-container>""")
    ])
    print("Updated cadastro-colaborador.html")
