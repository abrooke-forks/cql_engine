package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Interval;

/*
contains(argument List<T>, element T) Boolean

The contains operator for lists returns true if the given element is in the list.
This operator uses the notion of equivalence to determine whether or not the element being searched for is equivalent
    to any element in the list. In particular this means that if the list contains a null, and the element being
    searched for is null, the result will be true.
If the list argument is null, the result is false.

contains precision (argument Interval<T>, point T) Boolean

The contains operator for intervals returns true if the given point is greater than or equal to the starting point
  of the interval, and less than or equal to the ending point of the interval.
For open interval boundaries, exclusive comparison operators are used.
For closed interval boundaries, if the interval boundary is null, the result of the boundary comparison is considered true.
If either argument is null, the result is null.
*/

public class ContainsEvaluator extends org.cqframework.cql.elm.execution.Contains {

    public static Boolean contains(Object left, Object right, String precision) {
        if (left == null) {
            return null;
        }

        // contains precision (argument Interval<T>, point T)
        if (left instanceof Interval) {
            Interval leftInterval = (Interval)left;

            if (right == null) {
                return null;
            }

            Object leftStart = leftInterval.getStart();
            Object leftEnd = leftInterval.getEnd();

            return AndEvaluator.and(
                    GreaterOrEqualEvaluator.greaterOrEqual(right, leftStart),
                    LessOrEqualEvaluator.lessOrEqual(right, leftEnd)
            );
        }

        // contains(argument List<T>, element T)
        else if (left instanceof Iterable) {
            Iterable list = (Iterable)left;

            return InEvaluator.in(right, list, precision);
        }

        throw new IllegalArgumentException(String.format("Cannot Contains arguments of type '%s'.", left.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), contains(left, right, precision));
    }
}
