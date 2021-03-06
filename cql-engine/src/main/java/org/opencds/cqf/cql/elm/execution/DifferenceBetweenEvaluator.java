package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;

/*
difference in precision between(low DateTime, high DateTime) Integer
difference in precision between(low Time, high Time) Integer

The difference-between operator returns the number of boundaries crossed for the specified precision between the
    first and second arguments.
If the first argument is after the second argument, the result is negative.
The result of this operation is always an integer; any fractional boundaries are dropped.
For DateTime values, precision must be one of: years, months, days, hours, minutes, seconds, or milliseconds.
For Time values, precision must be one of: hours, minutes, seconds, or milliseconds.
If either argument is null, the result is null.

Additional Complexity: precison elements above the specified precision must also be accounted for (handled by Joda Time).
For example:
days between DateTime(2012, 5, 5) and DateTime(2011, 5, 0) = 365 + 5 = 370 days

NOTE: This is the same operation as DurationBetween, but the precision after the specified precision is truncated
to get the number of boundaries crossed instead of whole calendar periods.
For Example:
difference in days between DateTime(2014, 5, 12, 12, 10) and DateTime(2014, 5, 25, 15, 55)
will truncate the DateTimes to:
DateTime(2014, 5, 12) and DateTime(2014, 5, 25) respectively
*/

/**
 * Created by Chris Schuler on 6/22/2016
 */
public class DifferenceBetweenEvaluator extends org.cqframework.cql.elm.execution.DifferenceBetween {

    public static Object difference(Object left, Object right, Precision precision) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof BaseTemporal && right instanceof BaseTemporal) {
            boolean isWeeks = false;
            if (precision == Precision.WEEK) {
                isWeeks = true;
                precision = Precision.DAY;
            }
            boolean isLeftUncertain = ((BaseTemporal) left).isUncertain(precision);
            boolean isRightUncertain = ((BaseTemporal) right).isUncertain(precision);
            if (isLeftUncertain && isRightUncertain) {
                return null;
            }
            if (isLeftUncertain) {
                Interval leftUncertainInterval = ((BaseTemporal) left).getUncertaintyInterval(precision);
                return new Interval(
                        difference(leftUncertainInterval.getEnd(), right, isWeeks ? Precision.WEEK : precision), true,
                        difference(leftUncertainInterval.getStart(), right, isWeeks ? Precision.WEEK : precision), true
                ).setUncertain(true);
            }
            if (isRightUncertain) {
                Interval rightUncertainInterval = ((BaseTemporal) right).getUncertaintyInterval(precision);
                return new Interval(
                        difference(left, rightUncertainInterval.getStart(), isWeeks ? Precision.WEEK : precision), true,
                        difference(left, rightUncertainInterval.getEnd(), isWeeks ? Precision.WEEK : precision), true
                ).setUncertain(true);
            }

            if (left instanceof DateTime && right instanceof DateTime) {
                return isWeeks
                        ? (int) precision.toChronoUnit().between(
                                ((DateTime) left).expandPartialMinFromPrecision(precision).getDateTime(),
                                ((DateTime) right).expandPartialMinFromPrecision(precision).getDateTime()) / 7
                        : (int) precision.toChronoUnit().between(
                                ((DateTime) left).expandPartialMinFromPrecision(precision).getDateTime(),
                                ((DateTime) right).expandPartialMinFromPrecision(precision).getDateTime());
            }

            if (left instanceof Time && right instanceof Time) {
                return (int) precision.toChronoUnit().between(
                        ((Time) left).expandPartialMinFromPrecision(precision).getTime(),
                        ((Time) right).expandPartialMinFromPrecision(precision).getTime()
                );
            }
        }

        throw new IllegalArgumentException(String.format("Cannot perform DifferenceBetween operation with arguments of type '%s' and '%s'.", left.getClass().getName(), right.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);
        String precision = getPrecision().value();

        return difference(left, right, Precision.fromString(precision));
    }
}
