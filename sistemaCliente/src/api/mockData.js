// Exemplo de dados mockados para desenvolvimento e testes
// Este arquivo pode ser usado para simular respostas da API durante o desenvolvimento

export const mockOrdemServico = {
  id: 1,
  numeroOs: '12345',
  status: 'EM_EXECUCAO',
  veiculo: 'Fiat Uno 1.0 2020',
  placa: 'ABC-1234',
  km: 45000,
  mecanico: 'João Silva',
  dataEntrada: '2025-11-25T08:00:00',
  previsaoEntrega: '2025-12-02T18:00:00',
  valorMaoObra: 350.00,
  valorPecas: 450.00,
  subtotal: 800.00,
  valorTotal: 800.00,
  observacoes: 'Cliente relatou barulho estranho no motor ao acelerar. Solicitou também revisão completa dos freios.',
  
  servicos: [
    {
      descricao: 'Troca de óleo e filtro',
      tempo: 1.5,
      valor: 150.00
    },
    {
      descricao: 'Alinhamento e balanceamento',
      tempo: 2,
      valor: 120.00
    },
    {
      descricao: 'Revisão do sistema de freios',
      tempo: 1,
      valor: 80.00
    }
  ],
  
  pecas: [
    {
      nome: 'Óleo sintético 5W30',
      quantidade: 4,
      valorUnitario: 45.00,
      valorTotal: 180.00
    },
    {
      nome: 'Filtro de óleo',
      quantidade: 1,
      valorUnitario: 35.00,
      valorTotal: 35.00
    },
    {
      nome: 'Pastilha de freio dianteira',
      quantidade: 1,
      valorUnitario: 120.00,
      valorTotal: 120.00
    },
    {
      nome: 'Disco de freio',
      quantidade: 2,
      valorUnitario: 85.00,
      valorTotal: 170.00
    }
  ],
  
  fotos: [
    'https://via.placeholder.com/800x600/005CFF/FFFFFF?text=Foto+1',
    'https://via.placeholder.com/800x600/3380FF/FFFFFF?text=Foto+2',
    'https://via.placeholder.com/800x600/0047CC/FFFFFF?text=Foto+3',
    'https://via.placeholder.com/800x600/005CFF/FFFFFF?text=Foto+4'
  ],
  
  historicoStatus: [
    {
      status: 'AGUARDANDO_APROVACAO',
      data: '2025-11-25T08:00:00'
    },
    {
      status: 'EM_EXECUCAO',
      data: '2025-11-26T09:30:00'
    }
  ],
  
  historicoAlteracoes: [
    {
      descricao: 'Ordem de serviço criada',
      data: '2025-11-25T08:00:00'
    },
    {
      descricao: 'Cliente aprovou orçamento',
      data: '2025-11-26T09:00:00'
    },
    {
      descricao: 'Serviço iniciado',
      data: '2025-11-26T09:30:00'
    },
    {
      descricao: 'Peças solicitadas ao fornecedor',
      data: '2025-11-26T10:15:00'
    }
  ]
};

export const mockHistorico = [
  {
    id: 1,
    numeroOs: '12345',
    status: 'EM_EXECUCAO',
    veiculo: 'Fiat Uno 1.0 2020',
    dataEntrada: '2025-11-25T08:00:00',
    previsaoEntrega: '2025-12-02T18:00:00',
    valorTotal: 800.00
  },
  {
    id: 2,
    numeroOs: '12344',
    status: 'ENTREGUE',
    veiculo: 'Fiat Uno 1.0 2020',
    dataEntrada: '2025-10-15T08:00:00',
    previsaoEntrega: '2025-10-20T18:00:00',
    valorTotal: 450.00
  },
  {
    id: 3,
    numeroOs: '12343',
    status: 'ENTREGUE',
    veiculo: 'Fiat Uno 1.0 2020',
    dataEntrada: '2025-09-10T08:00:00',
    previsaoEntrega: '2025-09-15T18:00:00',
    valorTotal: 320.00
  }
];

export const mockLoginResponse = {
  token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvYW8gU2lsdmEiLCJpYXQiOjE1MTYyMzkwMjJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c',
  user: {
    id: 1,
    nome: 'João Silva',
    email: 'joao@email.com',
    cpf: '123.456.789-00'
  }
};
