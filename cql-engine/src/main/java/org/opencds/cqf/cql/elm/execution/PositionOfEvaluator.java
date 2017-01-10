package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
PositionOf(pattern String, argument String) Integer

The PositionOf operator returns the 0-based index of the given pattern in the given string.
If the pattern is not found, the result is -1.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class PositionOfEvaluator extends org.cqframework.cql.elm.execution.PositionOf {

    @Override
    public Object doOperation(String leftOperand, String rightOperand) {
        return rightOperand.indexOf(leftOperand);
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getPattern().evaluate(context);
        Object right = getString().evaluate(context);

        if (left == null || right == null) { return null; }

        return Execution.resolveStringDoOperation(this, left, right);
    }
}
