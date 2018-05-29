package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;

import java.math.BigDecimal;

/*
simple type DateTime

The DateTime type represents date and time values with potential uncertainty within CQL.
CQL supports date and time values in the range @0001-01-01T00:00:00.0 to @9999-12-31T23:59:59.999 with a 1 millisecond step size.
*/
public class DateTimeEvaluator extends org.cqframework.cql.elm.execution.DateTime {

    @Override
    public Object evaluate(Context context) {
        if (this.getYear() == null) {
            return null;
        }

        Integer year = (Integer)this.getYear().evaluate(context);

        if (year < DateTime.YEAR_RANGE_MIN) {
            throw new IllegalArgumentException(String.format("The year: %d falls below the accepted bounds of 0001-9999.", year));
        }
        else if (year > DateTime.YEAR_RANGE_MAX) {
            throw new IllegalArgumentException(String.format("The year: %d falls above the accepted bounds of 0001-9999.", year));
        }

        Integer month = (this.getMonth() == null) ? null : (Integer)this.getMonth().evaluate(context);
        Integer day = (this.getDay() == null) ? null : (Integer)this.getDay().evaluate(context);
        Integer hour = (this.getHour() == null) ? null : (Integer)this.getHour().evaluate(context);
        Integer minute = (this.getMinute() == null) ? null : (Integer)this.getMinute().evaluate(context);
        Integer second = (this.getSecond() == null) ? null : (Integer)this.getSecond().evaluate(context);
        Integer millis = (this.getMillisecond() == null) ? null : (Integer)this.getMillisecond().evaluate(context);
        BigDecimal timezoneOffset = (this.getTimezoneOffset() == null) ? null : (BigDecimal) this.getTimezoneOffset().evaluate(context);
        return new DateTime(year, month, day, hour, minute, second, millis, timezoneOffset);
    }
}
