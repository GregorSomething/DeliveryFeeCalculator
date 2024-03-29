package me.gregors.ratecalc.rules.types;

import me.gregors.ratecalc.rules.CalculationRuleException;

import java.util.function.Function;

public class CalculationException<T, V extends Comparable<V>> implements CalculationRule<T, V> {
    private final Function<T, ? extends Exception> toError;

    protected CalculationException(Function<T, ? extends Exception> toError) {
        this.toError = toError;
    }

    @Override
    public V apply(T input) throws CalculationRuleException {
        Exception e = this.toError.apply(input);
        throw new CalculationRuleException(e.getMessage(), e);
    }



}
