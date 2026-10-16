import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { NgxPermissionsService } from 'ngx-permissions';
import { environment } from '@env/environment';

/**
 * Route-level UX guard. Server-side authorization is mandatory and authoritative.
 * Declare route.data.requiredPermissions as a string or array. By default
 * all declared permissions are required; mode "any" permits any one.
 */
export const permissionGuard: CanActivateFn = route => {
  if (environment.uxPreview) return true;
  const required = route.data['requiredPermissions'] as string | string[] | undefined;
  const permissions = typeof required === 'string' ? [required] : required;
  // No permission requirement: do not impose a new constraint accidentally.
  if (!permissions || !permissions.length) return true;

  const service = inject(NgxPermissionsService);
  const router = inject(Router);
  const hasPermission = (name: string) => !!service.getPermission(name);
  const accepted = route.data['permissionMode'] === 'any'
    ? permissions.some(hasPermission)
    : permissions.every(hasPermission);
  return accepted ? true : router.parseUrl('/403');
};
