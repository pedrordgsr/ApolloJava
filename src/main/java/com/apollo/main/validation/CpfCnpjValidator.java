package com.apollo.main.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfCnpjValidator implements ConstraintValidator<ValidCpfCnpj, String> {

    private boolean required;

    @Override
    public void initialize(ValidCpfCnpj constraintAnnotation) {
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Se não é obrigatório e está vazio, retorna válido
        if (!required && (value == null || value.trim().isEmpty())) {
            return true;
        }

        // Se é obrigatório e está vazio, retorna inválido
        if (required && (value == null || value.trim().isEmpty())) {
            return false;
        }

        // Remove caracteres não numéricos
        String cleanValue = value.replaceAll("[^0-9]", "");

        // Verifica se é CPF (11 dígitos) ou CNPJ (14 dígitos)
        if (cleanValue.length() == 11) {
            return isValidCpf(cleanValue);
        } else if (cleanValue.length() == 14) {
            return isValidCnpj(cleanValue);
        }

        return false;
    }

    /**
     * Valida CPF usando o algoritmo oficial
     */
    private boolean isValidCpf(String cpf) {
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // Calcula o primeiro dígito verificador
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            if (firstDigit >= 10) {
                firstDigit = 0;
            }

            // Valida o primeiro dígito
            if (firstDigit != Character.getNumericValue(cpf.charAt(9))) {
                return false;
            }

            // Calcula o segundo dígito verificador
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            if (secondDigit >= 10) {
                secondDigit = 0;
            }

            // Valida o segundo dígito
            return secondDigit == Character.getNumericValue(cpf.charAt(10));

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida CNPJ usando o algoritmo oficial
     */
    private boolean isValidCnpj(String cnpj) {
        // Verifica se todos os dígitos são iguais
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            // Calcula o primeiro dígito verificador
            int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int sum = 0;
            for (int i = 0; i < 12; i++) {
                sum += Character.getNumericValue(cnpj.charAt(i)) * weight1[i];
            }
            int firstDigit = sum % 11;
            firstDigit = firstDigit < 2 ? 0 : 11 - firstDigit;

            // Valida o primeiro dígito
            if (firstDigit != Character.getNumericValue(cnpj.charAt(12))) {
                return false;
            }

            // Calcula o segundo dígito verificador
            int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            sum = 0;
            for (int i = 0; i < 13; i++) {
                sum += Character.getNumericValue(cnpj.charAt(i)) * weight2[i];
            }
            int secondDigit = sum % 11;
            secondDigit = secondDigit < 2 ? 0 : 11 - secondDigit;

            // Valida o segundo dígito
            return secondDigit == Character.getNumericValue(cnpj.charAt(13));

        } catch (Exception e) {
            return false;
        }
    }
}

