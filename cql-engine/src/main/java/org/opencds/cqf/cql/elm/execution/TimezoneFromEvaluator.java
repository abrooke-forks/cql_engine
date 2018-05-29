package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.BaseTemporal;

/*
timezone from(argument DateTime) Decimal
timezone from(argument Time) Decimal

NOTE: Description available in DateTimeComponentFrom class
*/

public class TimezoneFromEvaluator extends org.cqframework.cql.elm.execution.TimezoneFrom {

    public static Object timezoneFrom(Object operand) {
        if (operand instanceof BaseTemporal) {
            return ((BaseTemporal)operand).getDecimalOffset();
        }

        throw new IllegalArgumentException(String.format("Cannot perform TimezoneFrom operation with arguments of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        return context.logTrace(this.getClass(), timezoneFrom(operand), operand);
    }
}
