package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

/*
overlaps after precision (left Interval<T>, right Interval<T>) Boolean

The overlaps after operator returns true if the first interval overlaps the second and ends after it.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed
    at the specified precision.
If either argument is null, the result is null.
*/

public class OverlapsAfterEvaluator extends org.cqframework.cql.elm.execution.OverlapsAfter {

    public static Boolean overlapsAfter(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        // overlaps after precision (left Interval<T>, right Interval<T>)
        if (left instanceof Interval && right instanceof Interval) {
            Object leftEnd = ((Interval) left).getEnd();
            Object rightEnd = ((Interval) right).getEnd();

            if (leftEnd == null || rightEnd == null) {
                return null;
            }

            if (leftEnd instanceof BaseTemporal && rightEnd instanceof BaseTemporal) {
                return AndEvaluator.and(
                        AfterEvaluator.after(leftEnd, rightEnd, precision),
                        OverlapsEvaluator.overlaps(left, right, precision)
                );
            }

            return AndEvaluator.and(
                    GreaterEvaluator.greater(leftEnd, rightEnd),
                    OverlapsEvaluator.overlaps(left, right, precision)
            );
        }

        throw new IllegalArgumentException(String.format("Cannot OverlapsAfter arguments of type '%s'.", left.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), overlapsAfter(left, right, precision), left, right, precision);
    }
}
