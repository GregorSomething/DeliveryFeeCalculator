package me.gregors.ratecalc.rules.types;

import me.gregors.ratecalc.rules.CalculationRuleException;

public class CalculationMaxOf<T, V extends Comparable<V>> implements CalculationRule<T, V> {

    private final CalculationRule<T, V>[] rules;

    protected CalculationMaxOf(CalculationRule<T, V>... rules) {
        this.rules = rules;
        if (this.rules.length == 0)
            throw new RuntimeException("Must contain at least 1 CalculationRule.");
    }

    @Override
    public V apply(T input) throws CalculationRuleException {
        V min = this.rules[0].apply(input);
        for (int i = 1; i < this.rules.length; i++) {
            V val = this.rules[i].apply(input);
            if (min.compareTo(val) < 0) {
                min = val;
            }
        }
        return min;
    }
}
