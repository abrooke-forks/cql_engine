package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
Variance(argument List<Decimal>) Decimal
Variance(argument List<Quantity>) Quantity

The Variance operator returns the statistical variance of the elements in source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
Return types: BigDecimal & Quantity
*/

/**
* Created by Chris Schuler on 6/14/2016
*/
public class VarianceEvaluator extends org.cqframework.cql.elm.execution.Variance {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    Object mean = new AvgEvaluator().doOperation(operand);

    List<Object> newVals = new ArrayList<>();

    for (Object element : operand) {
      if (element != null) {
        newVals.add(Execution.resolveArithmeticDoOperation(new MultiplyEvaluator(),
                Execution.resolveArithmeticDoOperation(new SubtractEvaluator(), element, mean),
                Execution.resolveArithmeticDoOperation(new SubtractEvaluator(), element, mean)));
      }
    }

    return Execution.resolveArithmeticDoOperation(
            new DivideEvaluator(), Execution.resolveAggregateDoOperation(
                    new SumEvaluator(), newVals), new BigDecimal(newVals.size() - 1)); // slight variation to Avg
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getSource().evaluate(context);

    if ((Boolean) new IsNullEvaluator().doOperation((Iterable<Object>) operand)) { return null; }

    return Execution.resolveAggregateDoOperation(this, operand);
  }
}
