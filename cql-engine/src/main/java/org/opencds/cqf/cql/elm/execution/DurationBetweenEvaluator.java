package org.opencds.cqf.cql.elm.execution;

import org.joda.time.*;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Time;
import org.opencds.cqf.cql.runtime.Uncertainty;

import java.util.ArrayList;

// for Uncertainty

/*
duration between(low DateTime, high DateTime) Integer
duration between(low Time, high Time) Integer

The duration-between operator returns the number of whole calendar periods for the specified precision between
  the first and second arguments.
If the first argument is after the second argument, the result is negative.
The result of this operation is always an integer; any fractional periods are dropped.
For DateTime values, duration must be one of: years, months, days, hours, minutes, seconds, or milliseconds.
For Time values, duration must be one of: hours, minutes, seconds, or milliseconds.
If either argument is null, the result is null.

Additional Complexity: precison elements above the specified precision must also be accounted.
For example:
days between DateTime(2012, 5, 5) and DateTime(2011, 5, 0) = 365 + 5 = 370 days
*/

/**
* Created by Chris Schuler on 6/22/2016
*/
public class DurationBetweenEvaluator extends org.cqframework.cql.elm.execution.DurationBetween {

  private String precision;
  public void setPrecision(String precision) {
    this.precision = precision;
  }

  public DurationBetweenEvaluator withPrecision(String precision) {
    setPrecision(precision);
    return this;
  }

  public static Integer between(DateTime leftDT, DateTime rightDT, int idx) {
    Integer ret = 0;
    switch(idx) {
      case 0: ret = Years.yearsBetween(leftDT.getPartial(), rightDT.getPartial()).getYears();
              break;
      case 1: ret = Months.monthsBetween(leftDT.getPartial(), rightDT.getPartial()).getMonths();
              break;
      case 2: ret = Days.daysBetween(leftDT.getPartial(), rightDT.getPartial()).getDays();
              break;
      case 3: ret = Hours.hoursBetween(leftDT.getPartial(), rightDT.getPartial()).getHours();
              break;
      case 4: ret = Minutes.minutesBetween(leftDT.getPartial(), rightDT.getPartial()).getMinutes();
              break;
      case 5: ret = Seconds.secondsBetween(leftDT.getPartial(), rightDT.getPartial()).getSeconds();
              break;
      case 6: ret = Seconds.secondsBetween(leftDT.getPartial(), rightDT.getPartial()).getSeconds() * 1000;
              // now do the actual millisecond DurationBetween - add to ret
              ret += rightDT.getPartial().getValue(idx) - leftDT.getPartial().getValue(idx);
              break;
    }
    return ret;
  }

  public static Integer between(Time leftT, Time rightT, int idx) {
    Integer ret = 0;
    switch(idx) {
      case 0: ret = Hours.hoursBetween(leftT.getPartial(), rightT.getPartial()).getHours();
              break;
      case 1: ret = Minutes.minutesBetween(leftT.getPartial(), rightT.getPartial()).getMinutes();
              break;
      case 2: ret = Seconds.secondsBetween(leftT.getPartial(), rightT.getPartial()).getSeconds();
              break;
      case 3: ret = Seconds.secondsBetween(leftT.getPartial(), rightT.getPartial()).getSeconds() * 1000;
              // now do the actual millisecond DurationBetween - add to ret
              ret += rightT.getPartial().getValue(idx) - leftT.getPartial().getValue(idx);
              break;
    }
    return ret;
  }

  @Override
  public Object doOperation(DateTime leftOperand, DateTime rightOperand) {
    int idx = DateTime.getFieldIndex(precision);

    if (idx != -1) {

      // Uncertainty
      if (Uncertainty.isUncertain(leftOperand, precision)) {
        precision = DateTime.getUnit(rightOperand.getPartial().size() - 1);
        ArrayList<DateTime> highLow = Uncertainty.getHighLowList(leftOperand, precision);
        return new Uncertainty().withUncertaintyInterval(new Interval(
                between(highLow.get(1), rightOperand, idx), true,
                between(highLow.get(0), rightOperand, idx), true));
      }

      else if (Uncertainty.isUncertain(rightOperand, precision)) {
        precision = DateTime.getUnit(leftOperand.getPartial().size() - 1);
        ArrayList<DateTime> highLow = Uncertainty.getHighLowList(rightOperand, precision);
        return new Uncertainty().withUncertaintyInterval(new Interval(
                between(leftOperand, highLow.get(0), idx), true,
                between(leftOperand, highLow.get(1), idx), true));
      }

      else if (leftOperand.getPartial().size() > rightOperand.getPartial().size()) {
        // each partial must have same number of fields - expand rightDT
        rightOperand = DateTime.expandPartialMin(rightOperand, leftOperand.getPartial().size());
      }

      else if (rightOperand.getPartial().size() > leftOperand.getPartial().size()) {
        // each partial must have same number of fields - expand leftDT
        leftOperand = DateTime.expandPartialMin(leftOperand, rightOperand.getPartial().size());
      }

      return between(leftOperand, rightOperand, idx);
    }

    throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
  }

  @Override
  public Object doOperation(Time leftOperand, Time rightOperand) {
    int idx = Time.getFieldIndex(precision);

    if (idx != -1) {

      // Uncertainty
      if (Uncertainty.isUncertain(leftOperand, precision)) {
        precision = Time.getUnit(rightOperand.getPartial().size() - 1);
        ArrayList<Time> highLow = Uncertainty.getHighLowList(leftOperand, precision);
        return new Uncertainty().withUncertaintyInterval(new Interval(
                between(highLow.get(1), rightOperand, idx), true,
                between(highLow.get(0), rightOperand, idx), true));
      }

      else if (Uncertainty.isUncertain(rightOperand, precision)) {
        precision = Time.getUnit(leftOperand.getPartial().size() - 1);
        ArrayList<Time> highLow = Uncertainty.getHighLowList(rightOperand, precision);
        return new Uncertainty().withUncertaintyInterval(new Interval(
                between(leftOperand, highLow.get(0), idx), true,
                between(leftOperand, highLow.get(1), idx), true));
      }

      return between(leftOperand, rightOperand, idx);
    }

    throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);
    precision = getPrecision().value();

    if (left == null || right == null) { return null; }

    return Execution.resolveDateTimeDoOperation(this, left, right);
  }
}
