package me.gregors.ratecalc.rules.types;

import me.gregors.ratecalc.rules.CalculationRuleException;

import java.util.function.Function;

public class CalculationMap<T, V extends Comparable<V>> implements CalculationRule<T, V> {

    private final Function<T, V> mapper;

    protected CalculationMap(Function<T, V> mapper) {
        this.mapper = mapper;
    }

    @Override
    public V apply(T input) throws CalculationRuleException {
        return mapper.apply(input);
    }
}
