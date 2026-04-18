package com.neritech.saas.common.util;

import java.util.InputMismatchException;

/**
 * Classe utilitÃ¡ria para validaÃ§Ã£o de CNPJ
 */
public class CNPJ {

    private CNPJ() {
        // Construtor privado para evitar instanciaÃ§Ã£o
    }

    /**
     * Valida um CNPJ
     *
     * @param cnpj CNPJ a ser validado
     * @return true se o CNPJ for vÃ¡lido, false caso contrÃ¡rio
     */
    public static boolean isValid(String cnpj) {
        if (cnpj == null || cnpj.isEmpty()) {
            return false;
        }

        // Remove caracteres nÃ£o numÃ©ricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifica se tem 14 dÃ­gitos
        if (cnpj.length() != 14) {
            return false;
        }

        // Verifica se todos os dÃ­gitos sÃ£o iguais
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            // CÃ¡lculo do primeiro dÃ­gito verificador
            int soma = 0;
            int peso = 2;
            for (int i = 11; i >= 0; i--) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = peso == 9 ? 2 : peso + 1;
            }

            int resto = soma % 11;
            int dv1 = resto < 2 ? 0 : 11 - resto;

            // Verifica o primeiro dÃ­gito verificador
            if (Character.getNumericValue(cnpj.charAt(12)) != dv1) {
                return false;
            }

            // CÃ¡lculo do segundo dÃ­gito verificador
            soma = 0;
            peso = 2;
            for (int i = 12; i >= 0; i--) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = peso == 9 ? 2 : peso + 1;
            }

            resto = soma % 11;
            int dv2 = resto < 2 ? 0 : 11 - resto;

            // Verifica o segundo dÃ­gito verificador
            return Character.getNumericValue(cnpj.charAt(13)) == dv2;
        } catch (InputMismatchException e) {
            return false;
        }
    }

    /**
     * Formata um CNPJ
     *
     * @param cnpj CNPJ a ser formatado
     * @return CNPJ formatado (XX.XXX.XXX/XXXX-XX)
     */
    public static String format(String cnpj) {
        if (cnpj == null || cnpj.isEmpty()) {
            return cnpj;
        }

        // Remove caracteres nÃ£o numÃ©ricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifica se tem 14 dÃ­gitos
        if (cnpj.length() != 14) {
            return cnpj;
        }

        return cnpj.substring(0, 2) + "." +
               cnpj.substring(2, 5) + "." +
               cnpj.substring(5, 8) + "/" +
               cnpj.substring(8, 12) + "-" +
               cnpj.substring(12, 14);
    }

    /**
     * Remove a formataÃ§Ã£o de um CNPJ
     *
     * @param cnpj CNPJ formatado
     * @return CNPJ sem formataÃ§Ã£o
     */
    public static String unformat(String cnpj) {
        if (cnpj == null || cnpj.isEmpty()) {
            return cnpj;
        }

        return cnpj.replaceAll("[^0-9]", "");
    }
}
