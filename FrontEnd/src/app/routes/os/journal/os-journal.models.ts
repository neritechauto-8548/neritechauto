export type EstadoDiarioOS = 'ocioso' | 'carregando' | 'pronto' | 'proibido' | 'erro';

export interface ComentarioOrdemServico {
  id: number;
  ordemServicoId: number;
  usuarioAutorId: number;
  nomeAutor: string;
  conteudo: string;
  visibilidade: 'INTERNO';
  dataCadastro: string;
}

export interface ComentarioOrdemServicoCriacao {
  conteudo: string;
}
