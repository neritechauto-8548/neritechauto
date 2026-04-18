import axios from 'axios';
import { mockOrdemServico, mockHistorico, mockLoginResponse } from './mockData';

// Configuração da URL base da API
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
const USE_MOCK = false; // Conectado ao backend real v1.0

// Criar instância do axios
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para adicionar token de autenticação
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para tratamento de erros
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Serviços da API
const osService = {
  /**
   * Consulta pública de OS
   */
  async consultarOS(cpf, numeroOs) {
    if (USE_MOCK) {
      console.log('Consultando OS (MOCK):', { cpf, numeroOs });
      // Simula delay de rede
      await new Promise(resolve => setTimeout(resolve, 800));
      
      // Verifica se os dados batem com o mock (CPF 123.456.789-00 e OS 12345)
      const cpfLimpo = cpf.replace(/\D/g, '');
      if (numeroOs === mockOrdemServico.numeroOs) {
        return { data: mockOrdemServico };
      }
      throw { response: { status: 404 } };
    }
    return api.get('/public/ordem-servico/consulta', {
      params: { cpf, numeroOs },
    });
  },

  /**
   * Login do cliente
   */
  async login(email, senha) {
    if (USE_MOCK) {
      await new Promise(resolve => setTimeout(resolve, 1000));
      // Aceita qualquer login para facilitar testes
      auth.setToken(mockLoginResponse.token, mockLoginResponse.user);
      return true;
    }
    try {
      const response = await api.post('/auth/login', { email, senha });
      auth.setToken(response.data.token, response.data.user);
      return true;
    } catch (error) {
      console.error('Erro no login:', error);
      return false;
    }
  },

  /**
   * Lista histórico
   */
  async listarHistorico() {
    if (USE_MOCK) {
      await new Promise(resolve => setTimeout(resolve, 600));
      return { data: mockHistorico };
    }
    const user = auth.getUser();
    if (!user || !user.id) throw new Error('Usuário não autenticado');
    // Ajustado para o endpoint real do backend: /v1/ordens-servico/cliente/{id}
    return api.get(`/v1/ordens-servico/cliente/${user.id}`);
  },

  /**
   * Busca detalhes completos
   */
  async obterOS(id) {
    if (USE_MOCK) {
      await new Promise(resolve => setTimeout(resolve, 700));
      return { data: mockOrdemServico };
    }
    // Ajustado para o endpoint público: /public/ordem-servico/{id}
    return api.get(`/public/ordem-servico/${id}`);
  },

  /**
   * Aprovar OS
   */
  async aprovarOS(id) {
    if (USE_MOCK) {
      await new Promise(resolve => setTimeout(resolve, 1000));
      mockOrdemServico.status = 'EM_EXECUCAO';
      return { data: { success: true } };
    }
    // Adaptar conforme endpoint real (ex: usando o orcamento controller ou similar)
    return api.post(`/v1/ordens-servico/${id}/aprovar`);
  },

  /**
   * Negar OS
   */
  async negarOS(id, motivo) {
    if (USE_MOCK) {
      await new Promise(resolve => setTimeout(resolve, 1000));
      mockOrdemServico.status = 'CANCELADO';
      return { data: { success: true } };
    }
    return api.post(`/v1/ordens-servico/${id}/negar`, { motivo });
  }
};

// Utilitários de autenticação
export const auth = {
  setToken(token, user) {
    localStorage.setItem('token', token);
    if (user) {
      localStorage.setItem('user', JSON.stringify(user));
    }
  },
  getToken() {
    return localStorage.getItem('token');
  },
  getUser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },
  isAuthenticated() {
    return !!this.getToken();
  },
  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },
};

export default osService;
