import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminDashboardService, AdminDashboardMetricsResponse } from './admin-dashboard.service';
import { PageHeader } from '@shared';
import { CardModule } from 'primeng/card';
import { SkeletonModule } from 'primeng/skeleton';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, PageHeader, CardModule, SkeletonModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  metrics: AdminDashboardMetricsResponse | null = null;
  loading = true;

  constructor(private adminDashboardService: AdminDashboardService) {}

  ngOnInit(): void {
    this.loadMetrics();
  }

  loadMetrics(): void {
    this.loading = true;
    this.adminDashboardService.getMetrics().subscribe({
      next: (res) => {
        this.metrics = res;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading admin metrics', err);
        this.loading = false;
      }
    });
  }
}
