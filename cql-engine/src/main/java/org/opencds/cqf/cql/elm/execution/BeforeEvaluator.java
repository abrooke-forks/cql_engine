package org.opencds.cqf.cql.elm.execution;

import org.joda.time.Instant;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.util.List;

/*
*** NOTES FOR INTERVAL ***
before(left Interval<T>, right Interval<T>) Boolean
before(left T, right Interval<T>) Boolean
before(left interval<T>, right T) Boolean

The before operator for intervals returns true if the first interval ends before the second one starts.
  In other words, if the ending point of the first interval is less than the starting point of the second interval.
For the point-interval overload, the operator returns true if the given point is less than the start of the interval.
For the interval-point overload, the operator returns true if the given interval ends before the given point.
This operator uses the semantics described in the Start and End operators to determine interval boundaries.
If either argument is null, the result is null.


*** NOTES FOR DATETIME ***
before precision of(left DateTime, right DateTime) Boolean
before precision of(left Time, right Time) Boolean

The before-precision-of operator compares two date/time values to the specified precision to determine whether the
  first argument is the before the second argument. Precision must be one of: year, month, day, hour, minute, second, or millisecond.
For comparisons involving date/time or time values with imprecision, note that the result of the comparison may be null,
  depending on whether the values involved are specified to the level of precision used for the comparison.
If either or both arguments are null, the result is null.
*/

public class BeforeEvaluator extends org.cqframework.cql.elm.execution.Before {

    public static Boolean before(Object left, Object right, String precision) {

        if (left == null || right == null) {
            return null;
        }

        // before (Interval, Interval)
        if (left instanceof Interval && right instanceof Interval) {
            return LessEvaluator.less(((Interval)left).getStart(), ((Interval)right).getEnd());
        }

        // before (Interval, Point)
        else if (left instanceof Interval) {
            return LessEvaluator.less(((Interval)left).getEnd(), right);
        }

        // before (Point, Interval)
        else if (right instanceof Interval) {
            return LessEvaluator.less(left, ((Interval)right).getStart());
        }

        // before precision of (DateTime, DateTime)
        else if (left instanceof DateTime && right instanceof DateTime) {
            if (((BaseTemporal) left).equal(right)) {
                return false;
            }
            Boolean after = ((DateTime) left).after((DateTime) right, Precision.toPrecision(precision));
            return after == null ? null : !after;
        }

        // before precision of (Time, Time)
        else if (left instanceof Time && right instanceof Time) {
            if (((BaseTemporal) left).equal(right)) {
                return false;
            }
            Boolean after = ((Time) left).after((Time) right, Precision.toPrecision(precision));
            return after == null ? null : !after;
        }

        throw new IllegalArgumentException(String.format("Cannot Before arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        String precision = getPrecision() == null ? null : getPrecision().value();

        return context.logTrace(this.getClass(), before(left, right, precision), left, right);
    }
}
