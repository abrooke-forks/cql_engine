package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.ArrayList;
import java.util.List;

/*
*** NOTES FOR INTERVAL ***
union(left Interval<T>, right Interval<T>) Interval<T>

The union operator for intervals returns the union of the intervals.
  More precisely, the operator returns the interval that starts at the earliest starting point in either argument,
    and ends at the latest starting point in either argument.
If the arguments do not overlap or meet, this operator returns null.
If either argument is null, the result is null.

*** NOTES FOR LIST ***
union(left List<T>, right List<T>) List<T>

The union operator for lists returns a list with all elements from both arguments.
Note that duplicates are not eliminated during this process, if an element appears once in both sources,
  that element will be present twice in the resulting list.
If either argument is null, the result is null.
Note that the union operator can also be invoked with the symbolic operator (|).
*/

public class UnionEvaluator extends org.cqframework.cql.elm.execution.Union {

    public static Object union(Object left, Object right) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Interval) {
            Object leftStart = ((Interval) left).getStart();
            Object leftEnd = ((Interval) left).getEnd();
            Object rightStart = ((Interval) right).getStart();
            Object rightEnd = ((Interval) right).getEnd();

            if (leftStart == null || leftEnd == null
                    || rightStart == null || rightEnd == null)
            {
                return null;
            }

            if (!OverlapsEvaluator.overlaps(left, right, null)
                    && !MeetsEvaluator.meets(left, right, null))
            {
                return null;
            }

            Object min = LessEvaluator.less(leftStart, rightStart) ? leftStart : rightStart;
            Object max = GreaterEvaluator.greater(leftEnd, rightEnd) ? leftEnd : rightEnd;

            return new Interval(min, true, max, true);
        }

        else if (left instanceof Iterable) {
            // List Logic
            List<Object> result = new ArrayList<>();
            for (Object leftElement : (Iterable)left) {
                result.add(leftElement);
            }

            for (Object rightElement : (Iterable)right) {
                result.add(rightElement);
            }
            return result;
        }

        throw new IllegalArgumentException(String.format("Cannot Union arguments of type: %s and %s", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), union(left, right), left, right);
    }
}
