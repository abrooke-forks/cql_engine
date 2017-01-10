package org.opencds.cqf.cql.elm.execution;

import org.joda.time.*;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Time;
import org.opencds.cqf.cql.runtime.Uncertainty;

// for Uncertainty

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

  private String precision;
  public void setPrecision(String precision) {
    this.precision = precision;
  }

  public DifferenceBetweenEvaluator withPrecision(String precision) {
    setPrecision(precision);
    return this;
  }

  private Partial truncatePartial(DateTime source, int size) {
    int [] a = new int[size + 1];
    for (int i = 0; i < size + 1; ++i) {
      a[i] = source.getPartial().getValue(i);
    }
    return new Partial(DateTime.getFields(size + 1), a);
  }

  private Partial truncatePartial(Time source, int size) {
    int [] a = new int[size + 1];
    for (int i = 0; i < size + 1; ++i) {
      a[i] = source.getPartial().getValue(i);
    }
    return new Partial(Time.getFields(size + 1), a);
  }

  @Override
  public Object doOperation(DateTime leftOperand, DateTime rightOperand) {
    int idx = DateTime.getFieldIndex(precision);

    if (Uncertainty.isUncertain(leftOperand, precision) || Uncertainty.isUncertain(rightOperand, precision)) {
      return new DurationBetweenEvaluator().withPrecision(precision).doOperation(leftOperand, rightOperand);
    }

    if (idx != -1) {
      DateTime leftTrunc = new DateTime().withPartial(truncatePartial(leftOperand, idx))
                                         .withTimezoneOffset(leftOperand.getTimezoneOffset());
      DateTime rightTrunc = new DateTime().withPartial(truncatePartial(rightOperand, idx))
                                          .withTimezoneOffset(rightOperand.getTimezoneOffset());

      return new DurationBetweenEvaluator().withPrecision(precision).doOperation(leftTrunc, rightTrunc);
    }

    throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
  }

  @Override
  public Object doOperation(Time leftOperand, Time rightOperand) {
    int idx = Time.getFieldIndex(precision);

    if (Uncertainty.isUncertain(leftOperand, precision) || Uncertainty.isUncertain(rightOperand, precision)) {
      return new DurationBetweenEvaluator().withPrecision(precision).doOperation(leftOperand, rightOperand);
    }

    if (idx != -1) {
      Time leftTrunc = new Time().withPartial(truncatePartial(leftOperand, idx))
                                 .withTimezoneOffset(leftOperand.getTimezoneOffset());
      Time rightTrunc = new Time().withPartial(truncatePartial(rightOperand, idx))
                                  .withTimezoneOffset(rightOperand.getTimezoneOffset());

      return new DurationBetweenEvaluator().withPrecision(precision).doOperation(leftTrunc, rightTrunc);
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
