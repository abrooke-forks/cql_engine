package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.util.Iterator;

/*
Max(argument List<Integer>) Integer
Max(argument List<Decimal>) Decimal
Max(argument List<Quantity>) Quantity
Max(argument List<DateTime>) DateTime
Max(argument List<Time>) Time
Max(argument List<String>) String

The Max operator returns the maximum element in the source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
Possible return types include: Integer, BigDecimal, Quantity, DateTime, Time, String
*/

/**
* Created by Chris Schuler on 6/13/2016
*/
public class MaxEvaluator extends org.cqframework.cql.elm.execution.Max {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    Iterator<Object> itr = operand.iterator();

    if (!itr.hasNext()) { return null; } // empty list

    Object max = itr.next();
    while (max == null && itr.hasNext()) { max = itr.next(); }
    while (itr.hasNext()) {
      Object value = itr.next();
      if (value == null) { continue; } // skip null
      if ((Boolean) Execution.resolveComparisonDoOperation(new GreaterEvaluator(), value, max)) {
        max = value;
      }
    }
    return max;
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getSource().evaluate(context);

    if ((Boolean) new IsNullEvaluator().doOperation((Iterable<Object>) operand)) { return null; }

    return Execution.resolveAggregateDoOperation(this, operand);
  }
}
