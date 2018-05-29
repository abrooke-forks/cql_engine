package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Precision;
import org.opencds.cqf.cql.runtime.Time;

import java.math.BigDecimal;

/*
time from(argument DateTime) Time

NOTE: Description available in DateTimeComponentFrom class
*/

public class TimeFromEvaluator extends org.cqframework.cql.elm.execution.TimeFrom {

    public static Object timeFrom(Object operand) {
        if (operand == null) {
            return null;
        }

        if (operand instanceof DateTime) {
            Integer hour = ((DateTime)operand).getComponentFrom(Precision.HOUR);
            Integer minute = ((DateTime)operand).getComponentFrom(Precision.MINUTE);
            Integer second = ((DateTime)operand).getComponentFrom(Precision.SECOND);
            Integer millis = ((DateTime)operand).getComponentFrom(Precision.MILLI);
            BigDecimal offset = ((DateTime) operand).getDecimalOffset();
            return new Time(hour, minute, second, millis, offset);
        }

        throw new IllegalArgumentException(String.format("Cannot TimeFrom arguments of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), timeFrom(operand), operand);
    }
}
