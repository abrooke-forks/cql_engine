package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

/*
ends precision (left Interval<T>, right Interval<T>) Boolean

The ends operator returns true if the first interval ends the second.
    More precisely, if the starting point of the first interval is greater than or equal
    to the starting point of the second, and the ending point of the first interval is
    equal to the ending point of the second.
This operator uses the semantics described in the start and end operators to determine
    interval boundaries.
If precision is specified and the point type is a date/time type, comparisons used in the
    operation are performed at the specified precision.
If either argument is null, the result is null.

*/

public class EndsEvaluator extends org.cqframework.cql.elm.execution.Ends {

    public static Boolean ends(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        // ends precision (left Interval<T>, right Interval<T>)
        if (left instanceof Interval && right instanceof Interval) {
            Interval leftInterval = (Interval) left;
            Interval rightInterval = (Interval) right;

            Object leftStart = leftInterval.getStart();
            Object leftEnd = leftInterval.getEnd();
            Object rightStart = rightInterval.getStart();
            Object rightEnd = rightInterval.getEnd();

            if (leftStart == null || leftEnd == null
                    || rightStart == null || rightEnd == null) {
                return null;
            }

            if (leftStart instanceof BaseTemporal && rightStart instanceof BaseTemporal) {
                return AndEvaluator.and(
                        SameOrAfterEvaluator.sameOrAfter(leftStart, rightStart, precision),
                        ((BaseTemporal) leftEnd).equal(rightEnd)
                );
            }

            return AndEvaluator.and(
                    GreaterOrEqualEvaluator.greaterOrEqual(leftStart, rightStart),
                    EqualEvaluator.equal(leftEnd, rightEnd));
        }

        throw new IllegalArgumentException(String.format("Cannot Ends arguments of type '%s'.", left.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), ends(left, right, precision), left, right, precision);
    }
}
