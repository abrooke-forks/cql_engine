package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.Quantity;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Time;
import java.math.BigDecimal;

/*
ToString(argument Boolean) String
ToString(argument Integer) String
ToString(argument Decimal) String
ToString(argument Quantity) String
ToString(argument DateTime) String
ToString(argument Time) String

The ToString operator converts the value of its argument to a String value.
The operator uses the following string representations for each type:
Boolean	true|false
Integer	   (-)?#0
Decimal	   (-)?#0.0#
Quantity	 (-)?#0.0# '<unit>'
DateTime	 YYYY-MM-DDThh:mm:ss.fff(+|-)hh:mm
Time	     Thh:mm:ss.fff(+|-)hh:mm
If the argument is null, the result is null.
*/

/**
* Created by Chris Schuler on 6/14/2016
*/
public class ToStringEvaluator extends org.cqframework.cql.elm.execution.ToString {

  @Override
  public Object doOperation(Boolean operand) {
    return Boolean.toString(operand);
  }

  @Override
  public Object doOperation(Integer operand) {
    return Integer.toString(operand);
  }

  @Override
  public Object doOperation(BigDecimal operand) {
    return operand.toString();
  }

  @Override
  public Object doOperation(Quantity operand) {
    return operand.getValue().toString() + operand.getUnit();
  }

  @Override
  public Object doOperation(DateTime operand) {
    return operand.getPartial().toString();
  }

  @Override
  public Object doOperation(Time operand) {
    return operand.getPartial().toString();
  }

  @Override
  public Object evaluate(Context context) {
    Object operand = getOperand().evaluate(context);

    if (operand == null) { return null; }

    return Execution.resolveTypeDoOperation(this, operand);
  }
}
