package me.gregors.ratecalc.rules.types;

import me.gregors.ratecalc.rules.CalculationRuleException;

import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Represents rule that changes T to V
 * @param <T> input parameter type
 * @param <V> output type
 */
public interface CalculationRule<T, V extends Comparable<V>> {

    /**
     * Performs calulation/s on calculation rule
     * @param input input that is given to rule
     * @return output rule returned
     * @throws CalculationRuleException if error node got invoked
     */
    V apply(T input) throws CalculationRuleException;

    /**
     * Creates rule, that takes max value from its child rule outputs.
     * @param rules child rules
     * @return rule that takes max value of child rule outputs
     * @param <T> input parameter type
     * @param <V> output type
     */
    static <T, V extends Comparable<V>> CalculationRule<T, V> max(CalculationRule<T, V>... rules) {
        return new CalculationMaxOf<>(rules);
    }

    /**
     * Creates rule that "merges" multiple chile rules output to one
     * Eg reduce(Integer::sum, rule1, rule2, rule3) will return sum of rule outputs.
     * @param reducer function that converts two outputs to one
     * @param rules rule outputs you need to reduce
     * @return rule that reduces number of outputs to one
     * @param <T> input parameter type
     * @param <V> output type
     */
    static <T, V extends Comparable<V>> CalculationRule<T, V> reduce(BinaryOperator<V> reducer, CalculationRule<T, V>... rules) {
        return new CalculationReduce<>(reducer, rules);
    }

    /**
     * Creates rule that will map type T object to V object
     * @param mapper function that will map type T object to V object
     * @return rule that will map input to type V object
     * @param <T> input parameter type
     * @param <V> output type
     */
    static <T, V extends Comparable<V>> CalculationRule<T, V> map(Function<T, V> mapper) {
        return new CalculationMap<>(mapper);
    }

    /**
     * Creates rule that changes its child rules output
     * @param transformer function that changes rules output
     * @param child rule what's output to change
     * @return rule that transforms its childs output.
     * @param <T> input parameter type
     * @param <V> output type
     */
    static <T, V extends Comparable<V>> CalculationRule<T, V> transform(UnaryOperator<V> transformer, CalculationRule<T, V> child) {
        return new CalculationTransform<>(transformer, child);
    }

    /**
     * Creates rule that will throw exception
     * @param toError function that transforms input to exception
     * @return rule that will throw exception
     * @param <T> input parameter type
     * @param <V> output type
     */
    static <T, V extends Comparable<V>> CalculationRule<T, V> error(Function<T, ? extends Exception> toError) {
        return new CalculationException<>(toError);
    }

    /**
     * Creates constant value as rule, input does not change its return value.
     * @param value value that rule will
     * @return rule that always returns constant value
     * @param <T> input parameter type
     * @param <V> output type
     */
    static <T, V extends Comparable<V>> CalculationRule<T, V> constant(V value) {
        return new CalculationConstant<>(value);
    }

    /**
     * Creates if statements as rule
     * @param statement predicate on T
     * @param then if statement true, thins will be executed
     * @param otherwise if statement false, this will be executed
     * @return rule that return value from the executed rule (then or otherwise), depending on the input.
     * @param <T> input parameter type
     * @param <V> output type
     */
    static <T, V extends Comparable<V>> CalculationRule<T, V> ifThen(Predicate<T> statement, CalculationRule<T, V> then, CalculationRule<T, V> otherwise) {
        return new CalculationIf<>(statement, then, otherwise);
    }
}
