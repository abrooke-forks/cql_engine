package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.math.BigDecimal;

/*
is null(argument Any) Boolean

The is null operator determines whether or not its argument evaluates to null.
If the argument evaluates to null, the result is true; otherwise, the result is false.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class IsNullEvaluator extends org.cqframework.cql.elm.execution.IsNull {

    @Override
    public Object doOperation(Boolean operand) {
        return operand == null;
    }

    @Override
    public Object doOperation(BigDecimal operand) {
        return operand == null;
    }

    @Override
    public Object doOperation(Code operand) {
        return operand == null;
    }

    @Override
    public Object doOperation(Concept operand) {
        return operand == null;
    }

    @Override
    public Object doOperation(DateTime operand) {
        return operand == null;
    }

    @Override
    public Object doOperation(Integer operand) {
        return operand == null;
    }

    @Override
    public Object doOperation(Quantity operand) {
        return operand == null;
    }

    @Override
    public Object doOperation(String operand) {
        return operand == null;
    }

    @Override
    public Object doOperation(Time operand) {
        return operand == null;
    }

    @Override
    public Object doOperation(Iterable<Object> operand) {
        if (operand.spliterator().getExactSizeIfKnown() > 0) {
            for (Object element : operand) {
                if (element != null)
                    return false;
            }
        }
        return true; // empty or all elements null
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        return Execution.resolveNullogicalDoOperation(this, operand);
    }
}
