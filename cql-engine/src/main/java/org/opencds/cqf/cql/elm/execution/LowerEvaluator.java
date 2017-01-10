package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
Lower(argument String) String

The Lower operator returns the lower case of its argument.
If the argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class LowerEvaluator extends org.cqframework.cql.elm.execution.Lower {

    @Override
    public Object doOperation(String operand) {
        return operand.toLowerCase();
    }

    @Override
    public Object evaluate(Context context) {
        Object operand = getOperand().evaluate(context);

        if (operand == null) { return null; }

        return Execution.resolveStringDoOperation(this, operand);
    }
}
