package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
*** LIST NOTES ***
Length(argument List<T>) Integer

The Length operator returns the number of elements in a list.
If the argument is null, the result is null.

*** STRING NOTES ***
Length(argument String) Integer

The Length operator returns the number of characters in a string.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class LengthEvaluator extends org.cqframework.cql.elm.execution.Length {

    @Override
    public Object doOperation(Iterable<Object> operand) {
        return (int) operand.spliterator().getExactSizeIfKnown();
    }

    @Override
    public Object doOperation(String operand) {
        return operand.length();
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) { return null; }

        return Execution.resolveSharedDoOperation(this, operand);
    }
}
