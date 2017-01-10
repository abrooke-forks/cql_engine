package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

import java.util.ArrayList;
import java.util.List;

/*
PopulationVariance(argument List<Decimal>) Decimal
PopulationVariance(argument List<Quantity>) Quantity

The PopulationVariance operator returns the statistical population variance of the elements in source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
Return types: BigDecimal & Quantity
*/

/**
* Created by Chris Schuler on 6/14/2016
*/
public class PopulationVarianceEvaluator extends org.cqframework.cql.elm.execution.PopulationVariance {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    Object mean = new AvgEvaluator().doOperation(operand);

    List<Object> newVals = new ArrayList<>();

    operand.forEach(ae -> newVals.add(
            Execution.resolveArithmeticDoOperation(new MultiplyEvaluator(),
                    Execution.resolveArithmeticDoOperation(new SubtractEvaluator(), ae, mean),
                    Execution.resolveArithmeticDoOperation(new SubtractEvaluator(), ae, mean))));

    return Execution.resolveAggregateDoOperation(new AvgEvaluator(), newVals);
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getSource().evaluate(context);

    if ((Boolean) new IsNullEvaluator().doOperation((Iterable<Object>) operand)) { return null; }

    return Execution.resolveAggregateDoOperation(this, operand);
  }
}
