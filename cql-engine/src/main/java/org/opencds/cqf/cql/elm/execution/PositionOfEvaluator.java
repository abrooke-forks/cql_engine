package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
PositionOf(pattern String, argument String) Integer

The PositionOf operator returns the 0-based index of the given pattern in the given string.
If the pattern is not found, the result is -1.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class PositionOfEvaluator extends org.cqframework.cql.elm.execution.PositionOf {

    public static Object positionOf(Object pattern, Object string) {
        if (pattern == null || string == null) {
            return null;
        }

        if (pattern instanceof String) {
            return ((String)string).indexOf((String)pattern);
        }

        throw new IllegalArgumentException(String.format("Cannot perform PositionOf operation with arguments of type '%s' and '%s'.", pattern.getClass().getName(), string.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object pattern = getPattern().evaluate(context);
        Object string = getString().evaluate(context);

        return context.logTrace(this.getClass(), positionOf(pattern, string), pattern, string);
    }
}
