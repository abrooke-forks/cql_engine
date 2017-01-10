package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/**
*   Created by Chris Schuler on 9/25/2016
*/
public class IfEvaluator extends org.cqframework.cql.elm.execution.If {

  private Context context;

  @Override
  public Object doOperation(Boolean operand) {
    return operand ? getThen().evaluate(context) : getElse().evaluate(context);
  }

  @Override
  public Object evaluate(Context context) {

    Object operand = getCondition().evaluate(context);
    this.context = context;

    // NOTE that if the condition evaluates to null, it is interpreted as false
    if (operand == null) { operand = false; }

    return Execution.resolveConditionalDoOperation(this, operand);
  }
}
