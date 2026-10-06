import { ChangeDetectionStrategy, Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DataViewState, NeriTechIcon, PageHeader } from '@shared';
@Component({standalone:true,selector:'app-pdv-central',changeDetection:ChangeDetectionStrategy.OnPush,imports:[RouterLink,PageHeader,NeriTechIcon,DataViewState],templateUrl:'./pdv-central.html',styleUrl:'./pdv-central.scss'}) export class PdvCentral {}
