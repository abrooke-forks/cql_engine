package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.*;

import java.math.BigDecimal;
import java.util.Iterator;

/*
*** NOTES FOR CLINICAL OPERATORS ***
=(left Code, right Code) Boolean
=(left Concept, right Concept) Boolean

The equal (=) operator for Codes and Concepts uses tuple equality semantics.
  This means that the operator will return true if and only if the values for each element by name are equal.
If either argument is null, or contains any null components, the result is null.

*** NOTES FOR INTERVAL ***
=(left Interval<T>, right Interval<T>) Boolean

The equal (=) operator for intervals returns true if and only if the intervals are over the same point type,
  and they have the same value for the starting and ending points of the intervals as determined by the Start and End operators.
If either argument is null, the result is null.

*** NOTES FOR LIST ***
=(left List<T>, right List<T>) Boolean

The equal (=) operator for lists returns true if and only if the lists have the same element type,
  and have the same elements by value, in the same order.
If either argument is null, or contains null elements, the result is null.

*** NOTES FOR COMPARISON ***
=<T>(left T, right T) Boolean

The equal (=) operator returns true if the arguments are equal; false if the arguments are known unequal, and null otherwise.
    Equality semantics are defined to be value-based.
For simple types, this means that equality returns true if and only if the result of each argument evaluates to the same value.
For quantities, this means that the dimensions of each quantity must be the same, but not necessarily the unit.
    For example, units of 'cm' and 'm' are comparable, but units of 'cm2' and  'cm' are not.
For tuple types, this means that equality returns true if and only if the tuples are of the same type,
    and the values for all elements by name are equal.
For comparisons involving date/time or time values with imprecision, note that the result of the comparison may be null,
    depending on whether the values involved are specified to the level of precision used for the comparison.
If either argument is null, the result is null.
*/

/**
 * Created by Bryn on 5/25/2016.
 */
public class EqualEvaluator extends org.cqframework.cql.elm.execution.Equal {

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
    public Object doOperation(Uncertainty leftOperand, Integer rightOperand) {
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

        if (left == null || right == null) { return null; }

        return Execution.resolveComparisonDoOperation(this, left, right);
    }
}
