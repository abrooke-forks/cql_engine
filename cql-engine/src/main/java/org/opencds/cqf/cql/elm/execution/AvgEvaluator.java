package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.math.BigDecimal;

/*
Avg(argument List<Decimal>) Decimal
Avg(argument List<Quantity>) Quantity

* The Avg operator returns the average of the non-null elements in the source.
* If the source contains no non-null elements, null is returned.
* If the source is null, the result is null.
* Returns values of type BigDecimal or Quantity
*/

/**
* Created by Chris Schuler on 6/13/2016
*/
public class AvgEvaluator extends org.cqframework.cql.elm.execution.Avg {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    int size = 0;
    for (Object value : operand) {
      if (value != null) { size++; }
    }

    if (size == 0) { return null; } // all elements null
    return Execution.resolveArithmeticDoOperation(
            new DivideEvaluator(), Execution.resolveAggregateDoOperation(
                    new SumEvaluator(), operand), new BigDecimal(size));
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getSource().evaluate(context);

    if ((Boolean) new IsNullEvaluator().doOperation((Iterable<Object>) operand)) { return null; }

    return Execution.resolveAggregateDoOperation(this, operand);
  }
}
