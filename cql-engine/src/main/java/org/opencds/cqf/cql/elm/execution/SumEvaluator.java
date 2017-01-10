package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
Sum(argument List<Integer>) Integer
Sum(argument List<Decimal>) Decimal
Sum(argument List<Quantity>) Quantity

The Sum operator returns the sum of non-null elements in the source.
If the source contains no non-null elements, null is returned.
If the list is null, the result is null.
Return types: Integer, BigDecimal & Quantity
*/

/**
* Created by Chris Schuler on 6/14/2016
*/
public class SumEvaluator extends org.cqframework.cql.elm.execution.Sum {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    Object sum = null;
    for (Object element : operand) {
      if (sum == null) {
        sum = element;
        continue;
      }
      if (element == null) { continue; }
      sum = Execution.resolveArithmeticDoOperation(new AddEvaluator(), sum, element);
    }

    return sum;
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getSource().evaluate(context);

    if (operand == null) { return null; }

    return Execution.resolveAggregateDoOperation(this, operand);
  }
}
