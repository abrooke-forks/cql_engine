package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
Upper(argument String) String

The Upper operator returns the upper case of its argument.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class UpperEvaluator extends org.cqframework.cql.elm.execution.Upper {

    @Override
    public Object doOperation(String operand) {
        return operand.toUpperCase();
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) { return null; }

        return Execution.resolveStringDoOperation(this, operand);
    }
}
