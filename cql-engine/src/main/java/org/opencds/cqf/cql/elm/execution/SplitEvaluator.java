package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import java.util.ArrayList;
import java.util.Collections;

/*
Split(stringToSplit String, separator String) List<String>

The Split operator splits a string into a list of strings using a separator.
If the stringToSplit argument is null, the result is null.
If the stringToSplit argument does not contain any appearances of the separator,
  the result is a list of strings containing one element that is the value of the stringToSplit argument.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class SplitEvaluator extends org.cqframework.cql.elm.execution.Split {

    @Override
    public Object doOperation(String leftOperand, String rightOperand) {
        ArrayList<Object> result = new ArrayList<>();
        if (rightOperand == null) {
            result.add(leftOperand);
        }
        else {
            Collections.addAll(result, (leftOperand).split(rightOperand));
        }
        return result;
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getStringToSplit().evaluate(context);
        Object right = getSeparator().evaluate(context);

        if (left == null) { return null; }

        return Execution.resolveStringDoOperation(this, left, right);
    }
}
