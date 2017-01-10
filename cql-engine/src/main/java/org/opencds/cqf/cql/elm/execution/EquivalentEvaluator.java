package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.math.BigDecimal;
import java.util.Iterator;

/*
*** NOTES FOR CLINICAL OPERATORS ***
~(left Code, right Code) Boolean

The ~ operator for Code values returns true if the code, system, and version elements are equivalent.
  The display element is ignored for the purposes of determining Code equivalence.
For Concept values, equivalence is defined as a non-empty intersection of the codes in each Concept.
  The display element is ignored for the purposes of determining Concept equivalence.
Note that this operator will always return true or false, even if either or both of its arguments are null,
  or contain null components.
Note carefully that this notion of equivalence is not the same as the notion of equivalence used in terminology:
  "these codes represent the same concept." CQL specifically avoids defining terminological equivalence.
    The notion of equivalence defined here is used to provide consistent and intuitive semantics when dealing with
      missing information in membership contexts.

*** NOTES FOR INTERVAL ***
~(left Interval<T>, right Interval<T>) Boolean

The ~ operator for intervals returns true if and only if the intervals are over the same point type,
  and the starting and ending points of the intervals as determined by the Start and End operators are equivalent.

*** NOTES FOR LIST ***
~(left List<T>, right List<T>) Boolean

The ~ operator for lists returns true if and only if the lists contain elements of the same type, have the same number of elements,
  and for each element in the lists, in order, the elements are equivalent.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class EquivalentEvaluator extends org.cqframework.cql.elm.execution.Equivalent {

    @Override
    public Object doOperation(Boolean leftOperand, Boolean rightOperand) {
        return leftOperand.equals(rightOperand);
    }

    @Override
    public Object doOperation(Integer leftOperand, Integer rightOperand) {
        return leftOperand.equals(rightOperand);
    }

    @Override
    public Object doOperation(BigDecimal leftOperand, BigDecimal rightOperand) {
        return leftOperand.compareTo(rightOperand) == 0;
    }

    @Override
    public Object doOperation(Quantity leftOperand, Quantity rightOperand) {
        return leftOperand.equals(rightOperand);
    }

    @Override
    public Object doOperation(String leftOperand, String rightOperand) {
        return leftOperand.equals(rightOperand);
    }

    @Override
    public Object doOperation(Code leftOperand, Code rightOperand) {
        return leftOperand.equals(rightOperand);
    }

    @Override
    public Object doOperation(Concept leftOperand, Concept rightOperand) {
        return leftOperand.equals(rightOperand);
    }

    @Override
    public Object doOperation(DateTime leftOperand, DateTime rightOperand) {
        return leftOperand.equal(rightOperand);
    }

    @Override
    public Object doOperation(Time leftOperand, Time rightOperand) {
        return leftOperand.equal(rightOperand);
    }

    @Override
    public Object doOperation(Interval leftOperand, Interval rightOperand) {
        return leftOperand.equal(rightOperand);
    }

    @Override
    public Object doOperation(Tuple leftOperand, Tuple rightOperand) {
        return leftOperand.equal(rightOperand);
    }

    @Override
    public Object doOperation(Uncertainty leftOperand, Uncertainty rightOperand) {
        return leftOperand.equal(rightOperand);
    }

    @Override
    public Object doOperation(Iterable<Object> leftOperand, Iterable<Object> rightOperand) {
        Iterator<Object> leftIterator = leftOperand.iterator();
        Iterator<Object> rightIterator = rightOperand.iterator();

        while (leftIterator.hasNext()) {
            Object leftObject = leftIterator.next();
            if (rightIterator.hasNext()) {
                Object rightObject = rightIterator.next();
                Boolean elementEquals = (Boolean) Execution.resolveComparisonDoOperation(this, leftObject, rightObject);
                if (elementEquals == null || !elementEquals) {
                    return elementEquals;
                }
            }
            else {
                return false;
            }
        }

        if (rightIterator.hasNext()) { return rightIterator.next() == null ? null : false; }

        return true;
    }

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return Execution.resolveComparisonDoOperation(this, left, right);
    }
}
