package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
*** NOTES FOR INTERVAL ***
includes _precision_ (left Interval<T>, right Interval<T>) Boolean
includes _precision_ (left Interval<T>, right T) Boolean

The includes operator for intervals returns true if the first interval completely includes the second.
    More precisely, if the starting point of the first interval is less than or equal to the starting
    point of the second interval, and the ending point of the first interval is greater than or equal
    to the ending point of the second interval.
For the point overload, the operator returns true if the interval includes (i.e. contains) the point.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If precision is specified and the point type is a date/time type, comparisons used in the operation are
    performed at the specified precision.
If either argument is null, the result is null.

*** NOTES FOR LIST ***
includes(left List<T>, right List<T>) Boolean
includes(left List<T>, right T) Boolean

The includes operator for lists returns true if the first list contains every element of the second list.
For the singleton overload, this operator returns true if the list includes (i.e. contains) the singleton.
This operator uses the notion of equivalence to determine whether or not two elements are the same.
If the left argument is null, the result is false, else if the right argument is null, the result is true.
Note that the order of elements does not matter for the purposes of determining inclusion.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class IncludesEvaluator extends org.cqframework.cql.elm.execution.Includes {

    public static Object includes(Object left, Object right, String precision) {
        try {
            return IncludedInEvaluator.includedIn(right, left, precision);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("Cannot Includes arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
        }
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision() != null ? getPrecision().value() : null;;

        return context.logTrace(this.getClass(), includes(left, right, precision), left, right, precision);
    }
}
