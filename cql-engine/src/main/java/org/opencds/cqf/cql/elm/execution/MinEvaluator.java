package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.util.Iterator;

/*
Min(argument List<Integer>) Integer
Min(argument List<Decimal>) Decimal
Min(argument List<Quantity>) Quantity
Min(argument List<DateTime>) DateTime
Min(argument List<Time>) Time
Min(argument List<String>) String

The Min operator returns the minimum element in the source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/13/2016
*/
public class MinEvaluator extends org.cqframework.cql.elm.execution.Min {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    Iterator<Object> itr = operand.iterator();

    if (!itr.hasNext()) { return null; } // empty list

    Object min = itr.next();
    while (min == null && itr.hasNext()) {
      min = itr.next();
    }
    while (itr.hasNext()) {
      Object value = itr.next();

      if (value == null) { continue; } // skip null

      if ((Boolean) Execution.resolveComparisonDoOperation(new LessEvaluator(), value, min)) {
        min = value;
      }

    }
    return min;
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getSource().evaluate(context);

    if ((Boolean) new IsNullEvaluator().doOperation((Iterable<Object>) operand)) { return null; }

    return Execution.resolveListDoOperation(this, operand);
  }
}
