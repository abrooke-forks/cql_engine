package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.math.BigDecimal;

/*
is<T>(argument Any) Boolean

The is operator allows the type of a result to be tested.
If the run-time type of the argument is of the type being tested, the result of the operator is true;
  otherwise, the result is false.
*/

/**
* Created by Chris Schuler on 6/14/2016
*/
public class IsEvaluator extends org.cqframework.cql.elm.execution.Is {

    private Class type;
    public void setType(Class type) {
        this.type = type;
    }
    public IsEvaluator withType(Class type) {
        setType(type);
        return this;
    }

    private Class resolveType(Context context) {
      if (this.getIsTypeSpecifier() != null) {
          return context.resolveType(this.getIsTypeSpecifier());
      }

      return context.resolveType(this.getIsType());
    }

    @Override
    public Object doOperation(Boolean operand) {
        return type.isAssignableFrom(Boolean.class);
    }

    @Override
    public Object doOperation(BigDecimal operand) {
        return type.isAssignableFrom(BigDecimal.class);
    }

    @Override
    public Object doOperation(Code operand) {
        return type.isAssignableFrom(Code.class);
    }

    @Override
    public Object doOperation(Concept operand) {
        return type.isAssignableFrom(Concept.class);
    }

    @Override
    public Object doOperation(DateTime operand) {
        return type.isAssignableFrom(DateTime.class);
    }

    @Override
    public Object doOperation(Integer operand) {
        return type.isAssignableFrom(Integer.class);
    }

    @Override
    public Object doOperation(Quantity operand) {
        return type.isAssignableFrom(Quantity.class);
    }

    @Override
    public Object doOperation(String operand) {
        return type.isAssignableFrom(String.class);
    }

    @Override
    public Object doOperation(Time operand) {
        return type.isAssignableFrom(Time.class);
    }

    @Override
        public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);
        type = resolveType(context);

        if (operand == null) { return null; }

        return Execution.resolveTypeDoOperation(this, operand);
    }
}
