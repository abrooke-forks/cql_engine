package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Value;

/*
meets before precision (left Interval<T>, right Interval<T>) Boolean

The meets before operator returns true if the first interval ends immediately before the second interval starts.
If precision is specified and the point type is a date/time type, comparisons used in the operation are
    performed at the specified precision.
If either argument is null, the result is null.
*/

public class MeetsBeforeEvaluator extends org.cqframework.cql.elm.execution.MeetsBefore {

    public static Boolean meetsBefore(Object left, Object right, String precision) {
        if (left == null || right == null) {
            return null;
        }

        // meets before precision (left Interval<T>, right Interval<T>)
        if (left instanceof Interval && right instanceof Interval) {
            Object leftEnd = ((Interval) left).getEnd();
            Object rightStart = ((Interval) right).getStart();

            if (leftEnd == null || rightStart == null) {
                return null;
            }

            if (leftEnd instanceof BaseTemporal && rightStart instanceof BaseTemporal) {
                return SameAsEvaluator.sameAs(rightStart, Value.successor(leftEnd), precision);
            }

            return EqualEvaluator.equal(rightStart, Value.successor(leftEnd));
        }

        throw new IllegalArgumentException(String.format("Cannot MeetsBefore arguments of type '%s'.", left.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), meetsBefore(left, right, precision), left, right, precision);
    }
}
