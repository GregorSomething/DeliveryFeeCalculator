package me.gregors.ratecalc.rules.types;

import me.gregors.ratecalc.rules.CalculationRuleException;

import java.util.function.UnaryOperator;

public class CalculationTransform<T, V extends Comparable<V>> implements CalculationRule<T, V> {
    private final CalculationRule<T, V> child;
    private final UnaryOperator<V> transformer;

    protected CalculationTransform(UnaryOperator<V> transformer, CalculationRule<T, V> child) {
        this.child = child;
        this.transformer = transformer;
    }

    @Override
    public V apply(T input) throws CalculationRuleException {
        return this.transformer.apply(
                child.apply(input));
    }
}
