package org.opencds.cqf.cql.elm.execution;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.format.ISODateTimeFormat;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;

import java.math.BigDecimal;

/*
ToDateTime(argument String) DateTime

The ToDateTime operator converts the value of its argument to a DateTime value.
The operator expects the string to be formatted using the ISO-8601 date/time representation:
  YYYY-MM-DDThh:mm:ss.fff(+|-)hh:mm
In addition, the string must be interpretable as a valid date/time value.
For example, the following are valid string representations for date/time values:
'2014-01-01'                  // January 1st, 2014
'2014-01-01T14:30:00.0Z'      // January 1st, 2014, 2:30PM UTC
'2014-01-01T14:30:00.0-07:00' // January 1st, 2014, 2:30PM Mountain Standard (GMT-7:00)
If the input string is not formatted correctly, or does not represent a valid date/time value, a run-time error is thrown.
As with date/time literals, date/time values may be specified to any precision. If no timezone is supplied,
  the timezone of the evaluation request timestamp is assumed.
If the argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 7/11/2016
*/
public class ToDateTimeEvaluator extends org.cqframework.cql.elm.execution.ToDateTime {

  public static Integer numElements(String isoDateString) {
    Integer dateSize = 0;
    Integer timeSize = 0;

    String[] dateAndTime = isoDateString.split("T");

    if (dateAndTime.length > 1) {
      String[] timeAndTimezone = dateAndTime[1].split("[+-]");
      timeSize = timeAndTimezone[0].split("\\W").length;
    }

    dateSize = dateAndTime[0].split("-").length;

    return dateSize + timeSize;
  }

  public int millisConverter(int millis, int idx) {
    int hours = millis / 1000 / 60 / 60;
    millis = millis - hours * 1000 * 60 * 60;
    int minutes = millis / 1000 / 60;
    millis = millis - minutes * 1000 * 60;
    int seconds = millis / 1000;
    millis = millis - seconds * 1000;
    switch (idx) {
      case 3: return hours;
      case 4: return minutes;
      case 5: return seconds;
      default: return millis;
    }
  }

  public Partial dateTimeToPartial(org.joda.time.DateTime dt, int precision) {
    Partial p = new Partial(dt.toLocalDateTime());
    if (p.get(DateTimeFieldType.millisOfDay()) == 0 && precision <= 3) {
      return p.without(DateTimeFieldType.millisOfDay());
    }
    int millis = p.get(DateTimeFieldType.millisOfDay());
    for (int i = 0; i < precision - 3; i++) {
      p = p.with(DateTime.getField(i + 3), millisConverter(millis, i + 3));
    }
    return p.without(DateTimeFieldType.millisOfDay());
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
    String[] date = isoDateString.split("T");
    return date.length >= 2 && (date[1].contains("Z") || date[1].contains("+") || date[1].contains("-"));
  }

  public String stripOffset(String isoDateString) {
    if (!hasOffset(isoDateString)) { return isoDateString; }
    if (isoDateString.contains("Z")) { return isoDateString.replace("Z", ""); }
    if (isoDateString.contains("+")) { return isoDateString.split("\\+")[0]; }
    return isoDateString.replace("-" + isoDateString.split("-")[3], "");
  }

  public BigDecimal getOffset(String isoDateString) {
    if (hasOffset(isoDateString)) {
      String[] offset = isoDateString.split("T")[1].split("[\\+\\-Z]");
      boolean sign = isoDateString.lastIndexOf("-") > 7;
      if (offset.length > 1) { return utcOffsetToDecimal(offset[1], sign); }
    }

    return new BigDecimal("0");
  }

  @Override
  public Object doOperation(String operand) {
    BigDecimal offset = getOffset(operand);
    org.joda.time.DateTime dt = ISODateTimeFormat.dateTimeParser().parseDateTime(stripOffset(operand));
    return new DateTime().withPartial(dateTimeToPartial(dt, numElements(operand)))
            .withTimezoneOffset(offset);
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);

    if (operand == null) { return null; }

    return Execution.resolveTypeDoOperation(this, operand);
  }
}
