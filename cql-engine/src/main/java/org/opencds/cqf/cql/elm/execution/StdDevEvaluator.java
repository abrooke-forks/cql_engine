package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;

import java.math.BigDecimal;

/*
StdDev(argument List<Decimal>) Decimal
StdDev(argument List<Quantity>) Quantity

The StdDev operator returns the statistical standard deviation of the elements in source.
If the source contains no non-null elements, null is returned.
If the list is null, the result is null.
Return types: BigDecimal & Quantity
*/

/**
* Created by Chris Schuler on 6/14/2016
*/
public class StdDevEvaluator extends org.cqframework.cql.elm.execution.StdDev {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    Object variance = Execution.resolveAggregateDoOperation(new VarianceEvaluator(), operand);

    return variance instanceof BigDecimal ?
            new PowerEvaluator().doOperation((BigDecimal) variance, new BigDecimal("0.5")) :
            new Quantity().withValue((BigDecimal) new PowerEvaluator().doOperation(((Quantity) variance).getValue(),
                    new BigDecimal("0.5"))).withUnit(((Quantity)variance).getUnit());
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getSource().evaluate(context);

    if ((Boolean) new IsNullEvaluator().doOperation((Iterable<Object>) operand)) { return null; }

    return Execution.resolveAggregateDoOperation(this, operand);
  }
}
