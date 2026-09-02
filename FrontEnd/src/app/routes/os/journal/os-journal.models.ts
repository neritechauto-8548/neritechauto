export type OsJournalState = 'idle' | 'loading' | 'ready' | 'forbidden' | 'error';

export interface OsComment {
  id: number;
  ordemServicoId: number;
  authorUserId: number;
  authorName: string;
  content: string;
  visibility: 'INTERNAL';
  createdAt: string;
}

export interface OsCommentCreateRequest {
  content: string;
}
