package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.Interval;

/*
*** NOTES FOR INTERVAL ***
included in(left Interval<T>, right Interval<T>) Boolean

The included in operator for intervals returns true if the first interval is completely included in the second.
  More precisely, if the starting point of the first interval is greater than or equal to the starting point of the second interval,
    and the ending point of the first interval is less than or equal to the ending point of the second interval.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.
Note that during is a synonym for included in and may be used to invoke the same operation wherever included in may appear.

*** NOTES FOR LIST ***
included in(left List<T>, right list<T>) Boolean

The included in operator for lists returns true if every element of the first list is in the second list.
This operator uses the notion of equivalence to determine whether or not two elements are the same.
If either argument is null, the result is null.
Note that the order of elements does not matter for the purposes of determining inclusion.
*/

public class IncludedInEvaluator extends org.cqframework.cql.elm.execution.IncludedIn {

    public static Object includedIn(Object left, Object right, String precision) {

        if (left == null) {
            return true;
        }

        if (right == null) {
            return false;
        }

        if (left instanceof Interval) {
            Object leftStart = ((Interval)left).getStart();
            Object leftEnd = ((Interval)left).getEnd();
            Object rightStart = ((Interval)right).getStart();
            Object rightEnd = ((Interval)right).getEnd();

            if (leftStart == null || leftEnd == null
                    || rightStart == null || rightEnd == null)
            {
                return null;
            }

            if (leftStart instanceof BaseTemporal) {
                return AndEvaluator.and(
                        SameOrAfterEvaluator.sameOrAfter(leftStart, rightStart, precision),
                        SameOrBeforeEvaluator.sameOrBefore(leftEnd, rightEnd, precision)
                );
            }

            return AndEvaluator.and(
                    GreaterOrEqualEvaluator.greaterOrEqual(leftStart, rightStart),
                    LessOrEqualEvaluator.lessOrEqual(leftEnd, rightEnd)
            );
        }

        else if (left instanceof Iterable) {
            for (Object element : (Iterable)left) {
                Object in = InEvaluator.in(element, right, precision);

                if (in == null) continue;

                if (!(Boolean) in) {
                    return false;
                }
            }
            return true;
        }

        throw new IllegalArgumentException(String.format("Cannot IncludedIn arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() != null ? getPrecision().value() : null;

        return context.logTrace(this.getClass(), includedIn(left, right, precision), left, right, precision);
    }
}
