package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import java.math.BigDecimal;

/*
Ceiling(argument Decimal) Integer

The Ceiling operator returns the first integer greater than or equal to the argument.
When invoked with an Integer argument, the argument will be implicitly converted to Decimal.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class CeilingEvaluator extends org.cqframework.cql.elm.execution.Ceiling {

  @Override
  public Object doOperation(BigDecimal operand) {
    return BigDecimal.valueOf(Math.ceil(operand.doubleValue())).intValue();
  }

  @Override
  public Object doOperation(Quantity operand) {
    return BigDecimal.valueOf(Math.ceil(operand.getValue().doubleValue())).intValue();
  }

  @Override
  public Object evaluate(Context context) {

    Object operand = getOperand().evaluate(context);

    if (operand == null) { return null; }

    return Execution.resolveArithmeticDoOperation(this, operand);
  }
}
