package me.gregors.ratecalc.rules.types;

import me.gregors.ratecalc.rules.CalculationRuleException;

import java.util.function.BinaryOperator;

public class CalculationReduce<T, V extends Comparable<V>> implements CalculationRule<T, V> {
    private final BinaryOperator<V> reducer;
    private final CalculationRule<T, V>[] rules;

    protected CalculationReduce(BinaryOperator<V> reducer, CalculationRule<T, V>... rules) {
        this.reducer = reducer;
        this.rules = rules;
        if (this.rules.length == 0)
            throw new RuntimeException("Must contain at least 1 CalculationRule.");
    }

    @Override
    public V apply(T input) throws CalculationRuleException {
        V acc = this.rules[0].apply(input);
        if (this.rules.length == 1)
            return acc;
        for (int i = 1; i < this.rules.length; i++) {
            acc = this.reducer.apply(acc, this.rules[i].apply(input));
        }
        return acc;
    }
}
