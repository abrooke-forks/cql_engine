package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.runtime.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Issue33 extends CqlExecutionTestBase {
    @Test
    public void testInterval() {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("Issue33").getExpression().evaluate(context);
//        assertThat(((DateTime)((Interval)result).getStart()).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2017, 12, 20, 11, 0, 0})));
//        assertThat(((DateTime)((Interval)result).getEnd()).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2017, 12, 20, 23, 59, 59, 999})));
        Assert.assertTrue(((DateTime)((Interval)result).getStart()).equal(new DateTime(2017, 12, 20, 11, 0, 0, null, null)));
        Assert.assertTrue(((DateTime)((Interval)result).getEnd()).equal(new DateTime(2017, 12, 20, 23, 59, 59, 999, null)));
    }
}
