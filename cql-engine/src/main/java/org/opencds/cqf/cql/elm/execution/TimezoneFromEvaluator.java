package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Time;

/*
timezone from(argument DateTime) Decimal
timezone from(argument Time) Decimal

NOTE: Description available in DateTimeComponentFrom
*/

/**
* Created by Chris Schuler on 6/22/2016
*/
public class TimezoneFromEvaluator extends org.cqframework.cql.elm.execution.TimezoneFrom {

  @Override
  public Object doOperation(DateTime operand) {
    return operand.getTimezoneOffset();
  }

  @Override
  public Object doOperation(Time operand) {
    return operand.getTimezoneOffset();
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);

    if (operand == null) { return null; }

    return Execution.resolveDateTimeDoOperation(this, operand);
  }
}
