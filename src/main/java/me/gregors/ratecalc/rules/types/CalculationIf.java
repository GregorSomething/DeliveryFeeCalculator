package me.gregors.ratecalc.rules.types;

import me.gregors.ratecalc.rules.CalculationRuleException;

import java.util.function.Predicate;

public class CalculationIf<T, V extends Comparable<V>> implements CalculationRule<T, V> {
    private final Predicate<T> statement;
    private final CalculationRule<T, V> then;
    private final CalculationRule<T, V> otherwise;

    protected CalculationIf(Predicate<T> statement, CalculationRule<T, V> then, CalculationRule<T, V> otherwise) {
        this.statement = statement;
        this.then = then;
        this.otherwise = otherwise;
    }

    @Override
    public V apply(T input) throws CalculationRuleException {
        return statement.test(input) ? then.apply(input) : otherwise.apply(input);
    }
}
