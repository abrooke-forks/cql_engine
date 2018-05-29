package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Precision;
import org.opencds.cqf.cql.runtime.Time;

/*
precision from(argument DateTime) Integer
precision from(argument Time) Integer

The component-from operator returns the specified component of the argument.
For DateTime values, precision must be one of: year, month, day, hour, minute, second, or millisecond.
For Time values, precision must be one of: hour, minute, second, or millisecond.
If the argument is null, or is not specified to the level of precision being extracted, the result is null.
*/

public class DateTimeComponentFromEvaluator extends org.cqframework.cql.elm.execution.DateTimeComponentFrom {

    public static Object dateTimeComponentFrom(Object operand, String precision) {

        if (operand == null) {
            return null;
        }

        if (precision == null) {
            throw new IllegalArgumentException("Precision must be specified.");
        }

        if (operand instanceof BaseTemporal) {
            if (((BaseTemporal) operand).getPrecision().field < Precision.toPrecision(precision).field) {
                return null;
            }
            return ((BaseTemporal) operand).getComponentFrom(precision);
        }

        throw new IllegalArgumentException(String.format("Cannot DateTimeComponentFrom arguments of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        String precision = getPrecision().value();

        return context.logTrace(this.getClass(), dateTimeComponentFrom(operand, precision), operand);
    }
}
