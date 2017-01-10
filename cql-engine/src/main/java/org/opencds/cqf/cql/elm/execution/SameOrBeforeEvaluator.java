package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Time;

/*
same precision or before(left DateTime, right DateTime) Boolean
same precision or before(left DateTime, right DateTime) Boolean

The same-precision-or before operator compares two date/time values to the specified precision to determine
  whether the first argument is the same or before the second argument.
For DateTime values, precision must be one of: year, month, day, hour, minute, second, or millisecond.
For Time values, precision must be one of: hour, minute, second, or millisecond.
For comparisons involving date/time or time values with imprecision, note that the result of the comparison may be null,
  depending on whether the values involved are specified to the level of precision used for the comparison.
If either or both arguments are null, the result is null.
*/

/**
* Created by Chris Schuler on 6/23/2016
*/
public class SameOrBeforeEvaluator extends org.cqframework.cql.elm.execution.SameOrBefore {

  @Override
  public Object doOperation(DateTime leftOperand, DateTime rightOperand) {
    SameAsEvaluator sameAs = new SameAsEvaluator().withPrecision(this.getPrecision());
    BeforeEvaluator before = new BeforeEvaluator().withPrecision(this.getPrecision());
    Boolean isSame = (Boolean) sameAs.doOperation(leftOperand, rightOperand);
    if (isSame == null || isSame) { return isSame; }
    Boolean isBefore = (Boolean) before.doOperation(leftOperand, rightOperand);
    if (isBefore == null || isBefore) { return isBefore; }
    return false;
  }

  @Override
  public Object doOperation(Time leftOperand, Time rightOperand) {
    SameAsEvaluator sameAs = new SameAsEvaluator().withPrecision(this.getPrecision());
    BeforeEvaluator before = new BeforeEvaluator().withPrecision(this.getPrecision());
    Boolean isSame = (Boolean) sameAs.doOperation(leftOperand, rightOperand);
    if (isSame == null || isSame) { return isSame; }
    Boolean isBefore = (Boolean) before.doOperation(leftOperand, rightOperand);
    if (isBefore == null || isBefore) { return isBefore; }
    return false;
  }

  @Override
  public Object evaluate(Context context) {
    Object left = getOperand().get(0).evaluate(context);
    Object right = getOperand().get(1).evaluate(context);

    if (left == null || right == null) { return null; }

    return Execution.resolveDateTimeDoOperation(this, left, right);
  }
}
