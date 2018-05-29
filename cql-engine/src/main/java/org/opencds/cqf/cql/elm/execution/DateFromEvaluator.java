package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Precision;

import java.math.BigDecimal;

/*
date from(argument DateTime) DateTime

NOTE: Description available in DateTimeComponentFrom class
*/

public class DateFromEvaluator extends org.cqframework.cql.elm.execution.DateFrom {

    public static Object dateFrom(Object operand) {

        if (operand == null) {
            return null;
        }

        if (operand instanceof DateTime) {
            Integer year = ((DateTime)operand).getComponentFrom(Precision.YEAR);
            Integer month = ((DateTime)operand).getComponentFrom(Precision.MONTH);
            Integer day = ((DateTime)operand).getComponentFrom(Precision.DAY);
            BigDecimal offset = ((DateTime) operand).getDecimalOffset();
            return new DateTime(year, month, day, null, null, null, null, offset);
        }

        throw new IllegalArgumentException(String.format("Cannot DateFrom arguments of type '%s'.", operand.getClass().getName()));
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return context.logTrace(this.getClass(), dateFrom(operand), operand);
    }
}
