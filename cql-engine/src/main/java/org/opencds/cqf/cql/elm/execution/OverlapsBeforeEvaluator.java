package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

/*
overlaps before precision (left Interval<T>, right Interval<T>) Boolean

The operator overlaps before returns true if the first interval overlaps the second and starts before it
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed
    at the specified precision.
If either argument is null, the result is null.
*/

public class OverlapsBeforeEvaluator extends org.cqframework.cql.elm.execution.OverlapsBefore {

    public static Boolean overlapsBefore(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        // overlaps before precision (left Interval<T>, right Interval<T>)
        if (left instanceof Interval && right instanceof Interval) {
            Object leftStart = ((Interval) left).getStart();
            Object rightStart = ((Interval) right).getStart();

            if (leftStart == null || rightStart == null) {
                return null;
            }

            if (leftStart instanceof BaseTemporal && rightStart instanceof BaseTemporal) {
                return AndEvaluator.and(
                        BeforeEvaluator.before(leftStart, rightStart, precision),
                        OverlapsEvaluator.overlaps(left, right, precision)
                );
            }

            return AndEvaluator.and(
                    LessEvaluator.less(leftStart, rightStart),
                    OverlapsEvaluator.overlaps(left, right, precision)
            );
        }

        throw new IllegalArgumentException(String.format("Cannot OverlapsBefore arguments of type '%s'.", left.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), overlapsBefore(left, right, precision), left, right, precision);
    }
}
