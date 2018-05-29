package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Time;

// for Uncertainty

/*
difference in precision between(low DateTime, high DateTime) Integer
difference in precision between(low Time, high Time) Integer

The difference-between operator returns the number of boundaries crossed for the specified precision between the
first and second arguments.
If the first argument is after the second argument, the result is negative.
The result of this operation is always an integer; any fractional boundaries are dropped.
For DateTime values, precision must be one of: years, months, days, hours, minutes, seconds, or milliseconds.
For Time values, precision must be one of: hours, minutes, seconds, or milliseconds.
If either argument is null, the result is null.

Additional Complexity: precison elements above the specified precision must also be accounted for (handled by Joda Time).
For example:
days between DateTime(2012, 5, 5) and DateTime(2011, 5, 0) = 365 + 5 = 370 days

NOTE: This is the same operation as DurationBetween, but the precision after the specified precision is truncated
to get the number of boundaries crossed instead of whole calendar periods.
For Example:
difference in days between DateTime(2014, 5, 12, 12, 10) and DateTime(2014, 5, 25, 15, 55)
will truncate the DateTimes to:
DateTime(2014, 5, 12) and DateTime(2014, 5, 25) respectively
*/

public class DifferenceBetweenEvaluator extends org.cqframework.cql.elm.execution.DifferenceBetween {

    public static Object difference(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        if (precision == null) {
            throw new IllegalArgumentException("Precision must be specified.");
        }

        // difference in precision between(low DateTime, high DateTime)
        if (left instanceof DateTime && right instanceof DateTime) {
            return ((DateTime) left).differenceBetween((DateTime) right, precision);
        }

        // difference in precision between(low Time, high Time)
        if (left instanceof Time && right instanceof Time) {
            return ((Time) left).differenceBetween((Time) right, precision);
        }

        throw new IllegalArgumentException(String.format("Cannot DifferenceBetween arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision().value();

        return context.logTrace(this.getClass(), difference(left, right, precision), left, right);
    }
}
