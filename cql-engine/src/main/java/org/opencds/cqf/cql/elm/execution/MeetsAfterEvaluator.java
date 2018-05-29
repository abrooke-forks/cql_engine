package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Value;

/*
meets after precision (left Interval<T>, right Interval<T>) Boolean

The meets after operator returns true if the first interval starts immediately after the second interval ends.
If precision is specified and the point type is a date/time type, comparisons used in the operation are
    performed at the specified precision.
If either argument is null, the result is null.
*/

public class MeetsAfterEvaluator extends org.cqframework.cql.elm.execution.MeetsAfter {

    public static Boolean meetsAfter(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        // meets after precision (left Interval<T>, right Interval<T>)
        if (left instanceof Interval && right instanceof Interval) {
            Object leftStart = ((Interval) left).getStart();
            Object rightEnd = ((Interval) right).getEnd();

            if (leftStart == null || rightEnd == null) {
                return null;
            }

            if (leftStart instanceof BaseTemporal && rightEnd instanceof BaseTemporal) {
                return SameAsEvaluator.sameAs(leftStart, Value.successor(rightEnd), precision);
            }

            return EqualEvaluator.equal(leftStart, Value.successor(rightEnd));
        }

        throw new IllegalArgumentException(String.format("Cannot MeetsAfter arguments of type '%s'.", left.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), meetsAfter(left, right, precision), left, right, precision);
    }
}
