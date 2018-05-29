package org.opencds.cqf.cql.execution;

import org.joda.time.Partial;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Time;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CqlAggregateFunctionsTest extends CqlExecutionTestBase {

    /**
     * {@link org.opencds.cqf.cql.elm.execution.AllTrueEvaluator#evaluate(Context)}
     */
    @Test
    public void testAllTrue() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("AllTrueAllTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AllTrueTrueFirst").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AllTrueFalseFirst").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AllTrueAllTrueFalseTrue").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AllTrueAllFalseTrueFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AllTrueNullFirst").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AllTrueEmptyList").getExpression().evaluate(context);
        assertThat(result, is(true));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.AnyTrueEvaluator#evaluate(Context)}
     */
    @Test
    public void testAnyTrue() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("AnyTrueAllTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueAllFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AnyTrueAllTrueFalseTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueAllFalseTrueFalse").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueTrueFirst").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueFalseFirst").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueNullFirstThenTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("AnyTrueNullFirstThenFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("AnyTrueEmptyList").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.AvgEvaluator#evaluate(Context)}
     */
    @Test
    public void testAvg() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("AvgTest1").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("3.0")));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.CountEvaluator#evaluate(Context)}
     */
    @Test
    public void testCount() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("CountTest1").getExpression().evaluate(context);
        assertThat(result, is(4));

        result = context.resolveExpressionRef("CountTestDateTime").getExpression().evaluate(context);
        assertThat(result, is(3));

        result = context.resolveExpressionRef("CountTestTime").getExpression().evaluate(context);
        assertThat(result, is(3));

        result = context.resolveExpressionRef("CountTestNull").getExpression().evaluate(context);
        assertThat(result, is(0));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.MaxEvaluator#evaluate(Context)}
     */
    @Test
    public void testMax() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("MaxTestInteger").getExpression().evaluate(context);
        assertThat(result, is(90));

        result = context.resolveExpressionRef("MaxTestString").getExpression().evaluate(context);
        assertThat(result, is("zebra"));

        result = context.resolveExpressionRef("MaxTestDateTime").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 10, 6})));
        Assert.assertTrue(((DateTime)result).equal(new DateTime(2012, 10, 6, null, null, null, null, null)));

        result = context.resolveExpressionRef("MaxTestTime").getExpression().evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
        Assert.assertTrue(((Time)result).equal(new Time(20, 59, 59, 999, null)));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.MedianEvaluator#evaluate(Context)}
     */
    @Test
    public void testMedian() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("MedianTestDecimal").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("3.5")));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.MinEvaluator#evaluate(Context)}
     */
    @Test
    public void testMin() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("MinTestInteger").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef("MinTestString").getExpression().evaluate(context);
        assertThat(result, is("bye"));

        result = context.resolveExpressionRef("MinTestDateTime").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 9, 5})));
        Assert.assertTrue(((DateTime)result).equal(new DateTime(2012, 9, 5, null, null, null, null, null)));

        result = context.resolveExpressionRef("MinTestTime").getExpression().evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {5, 59, 59, 999})));
        Assert.assertTrue(((Time)result).equal(new Time(5, 59, 59, 999, null)));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.ModeEvaluator#evaluate(Context)}
     */
    @Test
    public void testMode() throws JAXBException {
        Context context = new Context(library);

        // TODO: ModeTestInteger

        Object result = context.resolveExpressionRef("ModeTestDateTime").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2012, 9, 5})));
        Assert.assertTrue(((DateTime)result).equal(new DateTime(2012, 9, 5, null, null, null, null, null)));

        result = context.resolveExpressionRef("ModeTestTime").getExpression().evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {5, 59, 59, 999})));
        Assert.assertTrue(((Time)result).equal(new Time(5, 59, 59, 999, null)));}

    /**
     * {@link org.opencds.cqf.cql.elm.execution.StdDevEvaluator#evaluate(Context)}
     */
    @Test
    public void testPopulationStdDev() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("PopStdDevTest1").getExpression().evaluate(context);
        Assert.assertTrue(((BigDecimal) result).compareTo(new BigDecimal("1.41421356")) == 0); //23730951454746218587388284504413604736328125
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.PopulationVarianceEvaluator#evaluate(Context)}
     */
    @Test
    public void testPopulationVariance() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("PopVarianceTest1").getExpression().evaluate(context);
        Assert.assertTrue(((BigDecimal) result).compareTo(new BigDecimal("2.0")) == 0);
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.StdDevEvaluator#evaluate(Context)}
     */
    @Test
    public void testStdDev() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("StdDevTest1").getExpression().evaluate(context);
        Assert.assertTrue(((BigDecimal) result).compareTo(new BigDecimal("1.58113883")) == 0); //00841897613935316257993690669536590576171875
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.SumEvaluator#evaluate(Context)}
     */
    @Test
    public void testSum() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("SumTest1").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("20.0")));

        result = context.resolveExpressionRef("SumTestNull").getExpression().evaluate(context);
        assertThat(result, is(1));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.VarianceEvaluator#evaluate(Context)}
     */
    @Test
    public void testVariance() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("VarianceTest1").getExpression().evaluate(context);
        Assert.assertTrue(((BigDecimal) result).compareTo(new BigDecimal("2.5")) == 0);
    }
}
