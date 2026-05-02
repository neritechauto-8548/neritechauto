/**
 * Validador de documentos brasileiros (CPF e CNPJ) para o Frontend.
 * Suporta o novo formato de CNPJ Alfanumérico (Nota Técnica 2024.001).
 */

/**
 * Valida um CPF (11 dígitos numéricos).
 */
export function isValidCpf(cpf: string): boolean {
  if (!cpf) return false;

  const cleanCpf = cpf.replace(/\D/g, '');

  if (cleanCpf.length !== 11) return false;

  // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
  if (/^(\d)\1{10}$/.test(cleanCpf)) return false;

  const calculateDigit = (base: string, weights: number[]) => {
    let sum = 0;
    for (let i = 0; i < base.length; i++) {
      sum += parseInt(base.charAt(i), 10) * weights[i];
    }
    const rest = sum % 11;
    return rest < 2 ? 0 : 11 - rest;
  };

  const d1 = calculateDigit(cleanCpf.substring(0, 9), [10, 9, 8, 7, 6, 5, 4, 3, 2]);
  const d2 = calculateDigit(cleanCpf.substring(0, 9) + d1, [11, 10, 9, 8, 7, 6, 5, 4, 3, 2]);

  return cleanCpf === cleanCpf.substring(0, 9) + d1 + d2;
}

/**
 * Valida um CNPJ (14 caracteres).
 * Suporta o formato numérico tradicional e o novo formato alfanumérico.
 */
export function isValidCnpj(cnpj: string): boolean {
  if (!cnpj) return false;

  // Remove formatação (mantém letras e números)
  const cleanCnpj = cnpj.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();

  if (cleanCnpj.length !== 14) return false;

  // Os dois últimos caracteres devem ser sempre numéricos (Dígitos Verificadores)
  if (!/^\d{2}$/.test(cleanCnpj.substring(12))) return false;

  const calculateDigit = (base: string, weights: number[]) => {
    let sum = 0;
    for (let i = 0; i < base.length; i++) {
      const char = base.charAt(i);
      let value: number;
      
      if (/\d/.test(char)) {
        value = parseInt(char, 10);
      } else {
        // Regra Alfanumérico: Valor ASCII - 48
        // Ex: 'A' (65) - 48 = 17
        value = char.charCodeAt(0) - 48;
      }
      sum += value * weights[i];
    }
    const rest = sum % 11;
    const digit = 11 - rest;
    return digit >= 10 ? 0 : digit;
  };

  const d1 = calculateDigit(cleanCnpj.substring(0, 12), [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);
  const d2 = calculateDigit(cleanCnpj.substring(0, 12) + d1, [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);

  const expectedDv = `${d1}${d2}`;
  return cleanCnpj.endsWith(expectedDv);
}
