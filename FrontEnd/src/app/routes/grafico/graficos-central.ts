import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { DataViewState, NeriTechIcon, PageHeader } from '@shared';
@Component({ standalone: true, selector: 'app-graficos-central', changeDetection: ChangeDetectionStrategy.OnPush, imports: [FormsModule, RouterLink, PageHeader, NeriTechIcon, DataViewState], templateUrl: './graficos-central.html', styleUrl: './graficos-central.scss' })
export class GraficosCentral { periodo = '30'; unidade = ''; dominio = 'OPERACIONAL'; comparacao = false; }
