package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
+(left String, right String) String

The concatenate (+) operator performs string concatenation of its arguments.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class ConcatenateEvaluator extends org.cqframework.cql.elm.execution.Concatenate {

    @Override
    public Object doOperation(String leftOperand, String rightOperand) {
        return leftOperand.concat(rightOperand);
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        if (left == null || right == null) {  return null; }

        return Execution.resolveStringDoOperation(this, left, right);
    }
}
