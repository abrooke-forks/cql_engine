package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.math.BigDecimal;
import org.opencds.cqf.cql.runtime.Time;

/*
simple type Time

The Time type represents time-of-day values within CQL.
CQL supports time values in the range @T00:00:00.0 to @T23:59:59.999 with a step size of 1 millisecond.
*/

public class TimeEvaluator extends org.cqframework.cql.elm.execution.Time {

    @Override
    public Object evaluate(Context context) {
        if (this.getHour() == null) {
            return null;
        }

        Integer hour = (Integer)this.getHour().evaluate(context);
        Integer minute = (this.getMinute() == null) ? null : (Integer)this.getMinute().evaluate(context);
        Integer second = (this.getSecond() == null) ? null : (Integer)this.getSecond().evaluate(context);
        Integer millis = (this.getMillisecond() == null) ? null : (Integer)this.getMillisecond().evaluate(context);
        BigDecimal timezoneOffset = (this.getTimezoneOffset() == null) ? null : (BigDecimal) this.getTimezoneOffset().evaluate(context);
        return new Time(hour, minute, second, millis, timezoneOffset);
    }
}
