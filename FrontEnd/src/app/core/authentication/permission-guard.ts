import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, Router } from '@angular/router';
import { NgxPermissionsService } from 'ngx-permissions';

export const permissionGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const router = inject(Router);
  const permissionsService = inject(NgxPermissionsService);
  const required = normalizePermissions(route.data?.['permissions']);

  if (required.length === 0) {
    return true;
  }

  const granted = permissionsService.getPermissions();
  const authorized = required.some(permission => Boolean(granted[permission]));

  return authorized ? true : router.parseUrl('/403');
};

function normalizePermissions(value: unknown): string[] {
  if (typeof value === 'string' && value.trim()) {
    return [value.trim()];
  }

  if (Array.isArray(value)) {
    return value
      .filter((permission): permission is string => typeof permission === 'string')
      .map(permission => permission.trim())
      .filter(Boolean);
  }

  return [];
}
