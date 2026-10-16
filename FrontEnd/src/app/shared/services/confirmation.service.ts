import { Injectable } from '@angular/core';
import { Observable, ReplaySubject, Subject } from 'rxjs';

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
  private pendingResult?: ReplaySubject<boolean>;

  confirmation$ = this.confirmationSubject.asObservable();
  result$ = this.resultSubject.asObservable();

  confirm(config: ConfirmationConfig): Observable<boolean> {
    // Uma segunda solicitação nunca deve confirmar acidentalmente a primeira.
    // ReplaySubject preserva a resposta mesmo se a inscrição ocorrer logo após o clique.
    if (this.pendingResult) {
      this.pendingResult.next(false);
      this.pendingResult.complete();
    }

    const pending = new ReplaySubject<boolean>(1);
    this.pendingResult = pending;
    this.confirmationSubject.next(config);
    return pending.asObservable();
  }

  sendResult(confirmed: boolean) {
    const pending = this.pendingResult;
    this.pendingResult = undefined;
    if (!pending) return;
    pending.next(confirmed);
    pending.complete();
    this.resultSubject.next({ confirmed });
  }
}
