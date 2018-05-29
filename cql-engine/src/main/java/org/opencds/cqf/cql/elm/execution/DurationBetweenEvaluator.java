package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;
import org.opencds.cqf.cql.runtime.DateTime;

// for Uncertainty

/*
duration between(low DateTime, high DateTime) Integer
duration between(low Time, high Time) Integer

The duration-between operator returns the number of whole calendar periods for the specified precision between
  the first and second arguments.
If the first argument is after the second argument, the result is negative.
The result of this operation is always an integer; any fractional periods are dropped.
For DateTime values, duration must be one of: years, months, days, hours, minutes, seconds, or milliseconds.
For Time values, duration must be one of: hours, minutes, seconds, or milliseconds.
If either argument is null, the result is null.

Additional Complexity: precision elements above the specified precision must also be accounted.
For example:
days between DateTime(2012, 5, 5) and DateTime(2011, 5, 0) = 365 + 5 = 370 days
*/

public class DurationBetweenEvaluator extends org.cqframework.cql.elm.execution.DurationBetween {

    public static Object durationBetween(Object left, Object right, String precision) {

        if (precision == null) {
            throw new IllegalArgumentException("Precision must be specified.");
        }

        if (left == null || right == null) {
            return null;
        }

        // duration between(low DateTime, high DateTime)
        if (left instanceof DateTime && right instanceof DateTime) {
            return ((DateTime) left).durationBetween((DateTime) right, precision);
        }

        // duration between(low Time, high Time)
        if (left instanceof Time && right instanceof Time) {
            return ((Time) left).durationBetween((Time) right, precision);
        }

        throw new IllegalArgumentException(String.format("Cannot DurationBetween arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision().value();

        return context.logTrace(this.getClass(), durationBetween(left, right, precision), left, right);
    }
}
