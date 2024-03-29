package me.gregors.ratecalc.rules;

/**
 * Exception that is used when error rule calls out exception
 */
public class CalculationRuleException extends Exception {
    public CalculationRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
