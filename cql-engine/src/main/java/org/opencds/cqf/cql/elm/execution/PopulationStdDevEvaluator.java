package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;

import java.math.BigDecimal;

/*
PopulationStdDev(argument List<Decimal>) Decimal
PopulationStdDev(argument List<Quantity>) Quantity

The PopulationStdDev operator returns the statistical standard deviation of the elements in source.
If the source contains no non-null elements, null is returned.
If the source is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/14/2016
*/
public class PopulationStdDevEvaluator extends org.cqframework.cql.elm.execution.PopulationStdDev {

  @Override
  public Object doOperation(Iterable<Object> operand) {
    Object variance = new PopulationVarianceEvaluator().doOperation(operand);

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
