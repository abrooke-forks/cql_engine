package org.opencds.cqf.cql.elm.execution;

import org.joda.time.Partial;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Time;

import java.math.BigDecimal;

/*
ToTime(argument String) Time

The ToTime operator converts the value of its argument to a Time value.
The operator expects the string to be formatted using ISO-8601 time representation:
  Thh:mm:ss.fff(+|-)hh:mm
In addition, the string must be interpretable as a valid time-of-day value.
For example, the following are valid string representations for time-of-day values:
'T14:30:00.0Z'                // 2:30PM UTC
'T14:30:00.0-07:00'           // 2:30PM Mountain Standard (GMT-7:00)
If the input string is not formatted correctly, or does not represent a valid time-of-day value, a run-time error is thrown.
As with time-of-day literals, time-of-day values may be specified to any precision.
If no timezone is supplied, the timezone of the evaluation request timestamp is assumed.
If the argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 7/12/2016
*/
public class ToTimeEvaluator extends org.cqframework.cql.elm.execution.ToTime {

  public Partial timeToPartial(String isoDateString) {
    String[] timeAndTimezone = isoDateString.replace('T', ' ').replace('Z', ' ').trim().split("[\\+\\-]");
    String[] time = timeAndTimezone[0].split("\\W");
    int[] values = new int[time.length];
    for (int i = 0; i < values.length; ++i) {
      values[i] = Integer.parseInt(time[i]);
    }
    return new Partial(Time.getFields(values.length), values);
  }

  public String timeToOffset(String time) {
    switch (time) {
      case "15": return "25";
      case "30": return "50";
      case "45": return "75";
      default: return time;
    }
  }

  public BigDecimal utcOffsetToDecimal(String offset, boolean sign) {
    String[] utc = offset.split(":");
    if (utc.length > 1) {
      return sign ? new BigDecimal(utc[0] + "." + timeToOffset(utc[1])).negate()
              : new BigDecimal(utc[0] + "." + timeToOffset(utc[1]));
    }
    return sign ? new BigDecimal(offset + ".0").negate() : new BigDecimal(offset + ".0");
  }

  public boolean hasOffset(String isoDateString) {
    return isoDateString.contains("Z") || isoDateString.contains("+") || isoDateString.contains("-");
  }

  public BigDecimal getOffset(String isoDateString) {
    if (hasOffset(isoDateString)) {
      String[] offset = isoDateString.split("[\\+\\-Z]");
      boolean sign = isoDateString.contains("-");
      if (offset.length > 1) { return utcOffsetToDecimal(offset[1], sign); }
    }

    return new BigDecimal("0");
  }

  @Override
  public Object doOperation(String operand) {
    BigDecimal offset = getOffset(operand);
    return new Time().withPartial(timeToPartial(operand)).withTimezoneOffset(offset);
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);

    if (operand == null) { return null; }

    return Execution.resolveTypeDoOperation(this, operand);
  }
}
