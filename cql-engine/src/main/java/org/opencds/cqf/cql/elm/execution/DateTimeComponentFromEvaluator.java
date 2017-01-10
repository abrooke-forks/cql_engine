package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Time;

/*
precision from(argument DateTime) Integer
precision from(argument Time) Integer
timezone from(argument DateTime) Decimal
timezone from(argument Time) Decimal
date from(argument DateTime) DateTime
time from(argument DateTime) Time

The component-from operator returns the specified component of the argument.
For DateTime values, precision must be one of: year, month, day, hour, minute, second, or millisecond.
For Time values, precision must be one of: hour, minute, second, or millisecond.
If the argument is null, or is not specified to the level of precision being extracted, the result is null.
*/

/**
* Created by Chris Schuler on 6/22/2016
*/
public class DateTimeComponentFromEvaluator extends org.cqframework.cql.elm.execution.DateTimeComponentFrom {

  private String precision;
  public void setPrecision(String precision) {
    this.precision = precision;
  }

  public DateTimeComponentFromEvaluator withPrecision(String precision) {
    setPrecision(precision);
    return this;
  }

  @Override
  public Object doOperation(DateTime operand) {
    int idx = DateTime.getFieldIndex(precision);

    if (idx != -1) {
      // check level of precision
      if (idx + 1 > operand.getPartial().size()) {
        return null;
      }
      return operand.getPartial().getValue(idx);
    }

    throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
  }

  @Override
  public Object doOperation(Time operand) {
    int idx = Time.getFieldIndex(precision);

    if (idx != -1) {
      // check level of precision
      if (idx + 1 > operand.getPartial().size()) {
        return null;
      }
      return operand.getPartial().getValue(idx);
    }

     throw new IllegalArgumentException(String.format("Invalid duration precision: %s", precision));
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);
    precision = getPrecision().value();

    if (operand == null) { return null; }

    return Execution.resolveDateTimeDoOperation(this, operand);
  }
}
