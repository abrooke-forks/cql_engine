package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;

/*
date from(argument DateTime) DateTime

NOTE: Description available in DateTimeComponentFrom
*/

/**
* Created by Chris Schuler on 6/22/2016
*/
public class DateFromEvaluator extends org.cqframework.cql.elm.execution.DateFrom {

  @Override
  public Object doOperation(DateTime operand) {
    return operand;
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);

    if (operand == null) { return null; }

    return Execution.resolveDateTimeDoOperation(this, operand);
  }
}
