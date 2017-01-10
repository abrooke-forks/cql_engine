package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
and (left Boolean, right Boolean) Boolean

The and operator returns true if both its arguments are true.
If either argument is false, the result is false. Otherwise, the result is null.

The following examples illustrate the behavior of the and operator:
define IsTrue = true and true
define IsFalse = true and false
define IsAlsoFalse = false and null
define IsNull = true and null
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class AndEvaluator extends org.cqframework.cql.elm.execution.And {

    @Override
    public Object doOperation(Boolean leftOperand, Boolean rightOperand) {
        if (leftOperand == null || rightOperand == null) {
            if ((leftOperand != null && !leftOperand) || (rightOperand != null && !rightOperand)) {
                return false;
            }

            return null;
        }

        return (leftOperand && rightOperand);
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return Execution.resolveLogicalDoOperation(this, left, right);
    }
}
