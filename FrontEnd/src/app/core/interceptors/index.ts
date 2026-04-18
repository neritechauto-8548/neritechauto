export * from './noop-interceptor';
export * from './base-url-interceptor';
export * from './settings-interceptor';
export * from './token-interceptor';
export * from './api-interceptor';
export * from './error-interceptor';
export * from './logging-interceptor';
export * from './tenant-interceptor';

import { apiInterceptor } from './api-interceptor';
import { baseUrlInterceptor } from './base-url-interceptor';
import { errorInterceptor } from './error-interceptor';
import { loggingInterceptor } from './logging-interceptor';
import { noopInterceptor } from './noop-interceptor';
import { settingsInterceptor } from './settings-interceptor';
import { tokenInterceptor } from './token-interceptor';
import { tenantInterceptor } from './tenant-interceptor';

// Http interceptor providers in outside-in order
export const interceptors = [
  noopInterceptor,
  baseUrlInterceptor,
  settingsInterceptor,
  tenantInterceptor,
  tokenInterceptor,
  apiInterceptor,
  errorInterceptor,
  loggingInterceptor,
];
