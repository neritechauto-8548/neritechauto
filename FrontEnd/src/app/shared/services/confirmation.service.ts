import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

export interface ConfirmationConfig {
  title: string;
  message: string;
  confirmText?: string;
  cancelText?: string;
  type?: 'danger' | 'warning' | 'info';
  icon?: string;
}

export interface ConfirmationResult {
  confirmed: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class ConfirmationService {
  private confirmationSubject = new Subject<ConfirmationConfig>();
  private resultSubject = new Subject<ConfirmationResult>();

  confirmation$ = this.confirmationSubject.asObservable();
  result$ = this.resultSubject.asObservable();

  confirm(config: ConfirmationConfig): Observable<boolean> {
    this.confirmationSubject.next(config);

    return new Observable<boolean>(observer => {
      const subscription = this.result$.subscribe(result => {
        observer.next(result.confirmed);
        observer.complete();
        subscription.unsubscribe();
      });
    });
  }

  sendResult(confirmed: boolean) {
    this.resultSubject.next({ confirmed });
  }
}
