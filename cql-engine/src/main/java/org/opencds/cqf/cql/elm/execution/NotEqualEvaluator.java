package org.opencds.cqf.cql.elm.execution;

import org.opencds.cqf.cql.execution.Context;

/*
*** NOTES FOR INTERVAL ***
!=(left Interval<T>, right Interval<T>) Boolean

The not equal (!=) operator for intervals returns true if its arguments are not the same value.
The not equal operator is a shorthand for invocation of logical negation (not) of the equal operator.

*** NOTES FOR LIST ***
!=(left List<T>, right List<T>) Boolean

The not equal (!=) operator for lists returns true if its arguments are not the same value.
The not equal operator is a shorthand for invocation of logical negation (not) of the equal operator.
*/

public class NotEqualEvaluator extends org.cqframework.cql.elm.execution.NotEqual {

    @Override
    public Object evaluate(Context context) {
        Object left = getOperand().get(0).evaluate(context);
        Object right = getOperand().get(1).evaluate(context);

        return context.logTrace(this.getClass(), !EqualEvaluator.equal(left, right), left, right);
    }
}
