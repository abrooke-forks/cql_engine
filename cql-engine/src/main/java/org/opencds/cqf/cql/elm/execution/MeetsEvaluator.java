package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Value;

/*
meets(left Interval<T>, right Interval<T>) Boolean

The meets operator returns true if the first interval ends immediately before the second interval starts,
  or if the first interval starts immediately after the second interval ends.
In other words, if the ending point of the first interval is equal to the predecessor of the starting point of the second,
  or if the starting point of the first interval is equal to the successor of the ending point of the second.
If precision is specified and the point type is a date/time type, comparisons used in the operation are performed at the
    specified precision.
If either argument is null, the result is null.
*/

public class MeetsEvaluator extends org.cqframework.cql.elm.execution.Meets {

    public static Boolean meets(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        // meets(left Interval<T>, right Interval<T>)
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
                return AfterEvaluator.after(rightStart, leftEnd, precision)
                        ? ((BaseTemporal) rightStart).equal(Value.successor(leftEnd))
                        : ((BaseTemporal) leftStart).equal(Value.successor(rightEnd));
            }

            return GreaterEvaluator.greater(rightStart, leftEnd)
                    ? EqualEvaluator.equal(rightStart, Value.successor(leftEnd))
                    : EqualEvaluator.equal(leftStart, Value.successor(rightEnd));
        }

        throw new IllegalArgumentException(String.format("Cannot Meets arguments of type '%s'.", left.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), meets(left, right, precision), left, right, precision);
    }
}
