package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
Substring(stringToSub String, startIndex Integer) String
Substring(stringToSub String, startIndex Integer, length Integer) String

The Substring operator returns the string within stringToSub, starting at the 0-based index startIndex,
  and consisting of length characters.
If length is ommitted, the substring returned starts at startIndex and continues to the end of stringToSub.
If stringToSub or startIndex is null, or startIndex is out of range, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class SubstringEvaluator extends org.cqframework.cql.elm.execution.Substring {

    @Override
    public Object doOperation(String leftOperand, Integer rightOperand) {
        if (rightOperand < 0 || rightOperand >= leftOperand.length()) {
            return null;
        }

        return leftOperand.substring(rightOperand);
    }

    @Override
    public Object doOperation(String operand1, Integer operand2, Integer operand3) {
        operand3 = operand2 + operand3;

        if (operand3 > operand1.length()) {
            operand3 = operand1.length();
        }

        if (operand3 < operand2) { return null; }

        return operand1.substring(operand2, operand3);
    }

    @Override
    public Object evaluate(Context context) {
        Object operand1 = getStringToSub().evaluate(context);
        Object operand2 = getStartIndex().evaluate(context);
        Object operand3 = getLength() == null ? null : getLength().evaluate(context);

        if (operand1 == null || operand2 == null) { return null; }

        return operand3 == null ? Execution.resolveStringDoOperation(this, operand1, operand2) :
                Execution.resolveStringDoOperation(this, operand1, operand2, operand3);
    }
}
