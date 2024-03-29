package me.gregors.ratecalc.rules.types;

import me.gregors.ratecalc.rules.CalculationRuleException;

public class CalculationConstant<T, V extends Comparable<V>> implements CalculationRule<T, V> {
    public final V value;

    protected CalculationConstant(V value) {
        this.value = value;
    }

    @Override
    public V apply(T input) throws CalculationRuleException {
        return this.value;
    }
}
