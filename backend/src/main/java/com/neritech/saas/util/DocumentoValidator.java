package com.neritech.saas.util;

/**
 * Validador de documentos brasileiros (CPF e CNPJ).
 * Suporta o novo formato de CNPJ Alfanumérico (Nota Técnica 2024.001).
 */
public class DocumentoValidator {

    /**
     * Valida um CPF (11 dígitos numéricos).
     */
    public static boolean isValidCpf(String cpf) {
        if (cpf == null) return false;
        
        // Remove formatação
        String cleanCpf = cpf.replaceAll("\\D", "");
        
        if (cleanCpf.length() != 11) return false;
        
        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
        if (cleanCpf.matches("(\\d)\\1{10}")) return false;

        try {
            int d1 = calculateCpfDigit(cleanCpf.substring(0, 9), new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2});
            int d2 = calculateCpfDigit(cleanCpf.substring(0, 9) + d1, new int[]{11, 10, 9, 8, 7, 6, 5, 4, 3, 2});
            
            return cleanCpf.equals(cleanCpf.substring(0, 9) + d1 + d2);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida um CNPJ (14 caracteres).
     * Suporta o formato numérico tradicional e o novo formato alfanumérico.
     */
    public static boolean isValidCnpj(String cnpj) {
        if (cnpj == null) return false;

        // Remove formatação (mantém letras e números)
        String cleanCnpj = cnpj.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        if (cleanCnpj.length() != 14) return false;

        try {
            // Os dois últimos caracteres devem ser sempre numéricos (Dígitos Verificadores)
            if (!Character.isDigit(cleanCnpj.charAt(12)) || !Character.isDigit(cleanCnpj.charAt(13))) {
                return false;
            }

            int d1 = calculateCnpjDigit(cleanCnpj.substring(0, 12), new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
            int d2 = calculateCnpjDigit(cleanCnpj.substring(0, 12) + d1, new int[]{6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});

            String expectedDv = "" + d1 + d2;
            return cleanCnpj.endsWith(expectedDv);
        } catch (Exception e) {
            return false;
        }
    }

    private static int calculateCpfDigit(String base, int[] weights) {
        int sum = 0;
        for (int i = 0; i < base.length(); i++) {
            sum += Character.getNumericValue(base.charAt(i)) * weights[i];
        }
        int rest = sum % 11;
        return (rest < 2) ? 0 : 11 - rest;
    }

    private static int calculateCnpjDigit(String base, int[] weights) {
        int sum = 0;
        for (int i = 0; i < base.length(); i++) {
            char c = base.charAt(i);
            int value;
            if (Character.isDigit(c)) {
                value = Character.getNumericValue(c);
            } else {
                // Regra Alfanumérico: Valor ASCII - 48
                // Ex: 'A' (65) - 48 = 17
                value = (int) c - 48;
            }
            sum += value * weights[i];
        }
        int rest = sum % 11;
        int digit = 11 - rest;
        return (digit >= 10) ? 0 : digit;
    }
}
