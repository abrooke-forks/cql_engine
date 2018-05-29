package org.opencds.cqf.cql.execution;

import org.joda.time.DateTimeZone;
import org.joda.time.Partial;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;
import org.opencds.cqf.cql.runtime.Time;
import org.opencds.cqf.cql.runtime.Uncertainty;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class CqlDateTimeOperatorsTest extends CqlExecutionTestBase {

    /**
     * {@link org.opencds.cqf.cql.elm.execution.AddEvaluator#evaluate(Context)}
     */
    @Test
    public void testAdd() throws JAXBException {
        Context context = new Context(library);

        // simple cache test -- watching behavior while stepping through tests
        context.setExpressionCaching(true);

        Object result = context.resolveExpressionRef("DateTimeAdd5Years").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2010, 10, 10})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2010, 10, 10, null, null, null, null, null)));

        try {
            context.resolveExpressionRef("DateTimeAddInvalidYears").evaluate(context);
        }
        catch (ArithmeticException ae) {
            assertThat(ae.getMessage(), is("The date time addition results in a year greater than the accepted range."));
        }

        result = context.resolveExpressionRef("DateTimeAdd5Months").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2005, 10, 10})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 10, 10, null, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeAddMonthsOverflow").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2006, 3, 10})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2006, 3, 10, null, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeAdd5Days").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2005, 5, 15})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 5, 15, null, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeAddDaysOverflow").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2016, 7, 1})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, 7, 1, null, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeAdd5Hours").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(4), new int[] {2005, 5, 10, 10})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 5, 10, 10, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeAddHoursOverflow").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(4), new int[] {2016, 6, 11, 0})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2015, 6, 11, 0, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeAdd5Minutes").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2005, 5, 10, 5, 10})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 5, 10, 5, 10, null, null, null)));

        result = context.resolveExpressionRef("DateTimeAddMinutesOverflow").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2016, 6, 10, 6, 0})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, 6, 10, 6, 0, null, null, null)));

        result = context.resolveExpressionRef("DateTimeAdd5Seconds").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2005, 5, 10, 5, 5, 10})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 5, 10, 5, 5, 10, null, null)));

        result = context.resolveExpressionRef("DateTimeAddSecondsOverflow").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2016, 6, 10, 5, 6, 0})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, 6, 10, 5, 6, 0, null, null)));

        result = context.resolveExpressionRef("DateTimeAdd5Milliseconds").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2005, 5, 10, 5, 5, 5, 10})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 5, 10, 5, 5, 5, 10, null)));

        result = context.resolveExpressionRef("DateTimeAddMillisecondsOverflow").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2016, 6, 10, 5, 5, 6, 0})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, 6, 10, 5, 5, 6, 0, null)));

        result = context.resolveExpressionRef("DateTimeAddLeapYear").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2013, 2, 28})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2013, 2, 28, null, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeAdd2YearsByMonths").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2016})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, null, null, null, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeAdd2YearsByDays").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2016})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, null, null, null, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeAdd2YearsByDaysRem5Days").evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2016})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, null, null, null, null, null, null, null)));

        result = context.resolveExpressionRef("TimeAdd5Hours").evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(20, 59, 59, 999, null)));

        result = context.resolveExpressionRef("TimeAdd1Minute").evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {16, 0, 59, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(16, 0, 59, 999, null)));

        result = context.resolveExpressionRef("TimeAdd1Second").evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {16, 0, 0, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(16, 0, 0, 999, null)));

        result = context.resolveExpressionRef("TimeAdd1Millisecond").evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {16, 0, 0, 0})));
        Assert.assertTrue(((Time) result).equal(new Time(16, 0, 0, 0, null)));

        result = context.resolveExpressionRef("TimeAdd5Hours1Minute").evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {21, 0, 59, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(21, 0, 59, 999, null)));

        // checking access ordering and returning correct result
        result = context.resolveExpressionRef("TimeAdd1Second").evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {16, 0, 0, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(16, 0, 0, 999, null)));

        result = context.resolveExpressionRef("TimeAdd5hoursByMinute").evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {20, 59, 59, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(20, 59, 59, 999, null)));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.AfterEvaluator#evaluate(Context)}
     */
    @Test
    public void testAfter() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("DateTimeAfterYearTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeAfterYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeAfterMonthTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeAfterMonthFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeAfterDayTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeAfterDayTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeAfterDayFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeAfterHourTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeAfterHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeAfterMinuteTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeAfterMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeAfterSecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeAfterSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeAfterMillisecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeAfterMillisecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeAfterUncertain").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeAfterHourTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeAfterHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeAfterMinuteTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeAfterMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeAfterSecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeAfterSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeAfterMillisecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeAfterMillisecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        // result = context.resolveExpressionRef("TimeAfterTimeCstor").getExpression().evaluate(context);
        // assertThat(result, is(true));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.BeforeEvaluator#evaluate(Context)}
     */
    @Test
    public void testBefore() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("DateTimeBeforeYearTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeBeforeMonthTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeBeforeMonthFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeBeforeDayTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeBeforeDayTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeBeforeDayFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeBeforeHourTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeBeforeHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeBeforeMinuteTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeBeforeMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeBeforeSecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeBeforeSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeBeforeMillisecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeBeforeMillisecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("BeforeTimezoneTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("BeforeTimezoneFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeBeforeHourTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeBeforeHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeBeforeMinuteTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeBeforeMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeBeforeSecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeBeforeSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeBeforeMillisecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeBeforeMillisecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.DateTimeEvaluator#evaluate(Context)}
     */
    @Test
    public void testDateTime() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("DateTimeYear").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2003})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2003, null, null, null, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeMonth").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(2), new int[] {2003, 10})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2003, 10, null, null, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeDay").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2003, 10, 29})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2003, 10, 29, null, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeHour").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(4), new int[] {2003, 10, 29, 20})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2003, 10, 29, 20, null, null, null, null)));

        result = context.resolveExpressionRef("DateTimeMinute").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2003, 10, 29, 20, 50})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2003, 10, 29, 20, 50, null, null, null)));

        result = context.resolveExpressionRef("DateTimeSecond").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2003, 10, 29, 20, 50, 33})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2003, 10, 29, 20, 50, 33, null, null)));

        result = context.resolveExpressionRef("DateTimeMillisecond").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2003, 10, 29, 20, 50, 33, 955})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2003, 10, 29, 20, 50, 33, 955, null)));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.DateTimeComponentFromEvaluator#evaluate(Context)}
     */
    @Test
    public void testDateTimeComponentFrom() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("DateTimeComponentFromYear").getExpression().evaluate(context);
        assertThat(result, is(2003));

        result = context.resolveExpressionRef("DateTimeComponentFromMonth").getExpression().evaluate(context);
        assertThat(result, is(10));

        result = context.resolveExpressionRef("DateTimeComponentFromMonthMinBoundary").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("DateTimeComponentFromDay").getExpression().evaluate(context);
        assertThat(result, is(29));

        result = context.resolveExpressionRef("DateTimeComponentFromHour").getExpression().evaluate(context);
        assertThat(result, is(20));

        result = context.resolveExpressionRef("DateTimeComponentFromMinute").getExpression().evaluate(context);
        assertThat(result, is(50));

        result = context.resolveExpressionRef("DateTimeComponentFromSecond").getExpression().evaluate(context);
        assertThat(result, is(33));

        result = context.resolveExpressionRef("DateTimeComponentFromMillisecond").getExpression().evaluate(context);
        assertThat(result, is(955));

        result = context.resolveExpressionRef("DateTimeComponentFromTimezone").getExpression().evaluate(context);
        assertThat(result, is(new BigDecimal("1.00")));

        result = context.resolveExpressionRef("DateTimeComponentFromDate").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2003, 10, 29})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2003, 10, 29, null, null, null, null, null)));

        result = context.resolveExpressionRef("TimeComponentFromHour").getExpression().evaluate(context);
        assertThat(result, is(23));

        result = context.resolveExpressionRef("TimeComponentFromMinute").getExpression().evaluate(context);
        assertThat(result, is(20));

        result = context.resolveExpressionRef("TimeComponentFromSecond").getExpression().evaluate(context);
        assertThat(result, is(15));

        result = context.resolveExpressionRef("TimeComponentFromMilli").getExpression().evaluate(context);
        assertThat(result, is(555));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.DifferenceBetweenEvaluator#evaluate(Context)}
     */
    @Test
    public void testDifference() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("DateTimeDifferenceYear").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef("DateTimeDifferenceMonth").getExpression().evaluate(context);
        assertThat(result, is(8));

        result = context.resolveExpressionRef("DateTimeDifferenceDay").getExpression().evaluate(context);
        assertThat(result, is(10));

        result = context.resolveExpressionRef("DateTimeDifferenceHour").getExpression().evaluate(context);
        assertThat(result, is(8));

        result = context.resolveExpressionRef("DateTimeDifferenceMinute").getExpression().evaluate(context);
        assertThat(result, is(9));

        result = context.resolveExpressionRef("DateTimeDifferenceSecond").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef("DateTimeDifferenceMillisecond").getExpression().evaluate(context);
        assertThat(result, is(3600400));

        result = context.resolveExpressionRef("DateTimeDifferenceWeeks").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("DateTimeDifferenceWeeks2").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef("DateTimeDifferenceWeeks2").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef("DateTimeDifferenceNegative").getExpression().evaluate(context);
        assertThat(result, is(-18));

        result = context.resolveExpressionRef("DateTimeDifferenceUncertain").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeDifferenceHour").getExpression().evaluate(context);
        assertThat(result, is(3));

        result = context.resolveExpressionRef("TimeDifferenceMinute").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef("TimeDifferenceSecond").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef("TimeDifferenceMillis").getExpression().evaluate(context);
        assertThat(result, is(-5));

        // TODO: Uncomment once translator issue https://github.com/cqframework/clinical_quality_language/issues/256 is resolved
//        result = context.resolveExpressionRef("DifferenceInHoursA").getExpression().evaluate(context);
//        assertThat(result, is(1));
//
//        result = context.resolveExpressionRef("DifferenceInMinutesA").getExpression().evaluate(context);
//        assertThat(result, is(45));
//
//        result = context.resolveExpressionRef("DifferenceInDaysA").getExpression().evaluate(context);
//        assertThat(result, is(1));

        result = context.resolveExpressionRef("DifferenceInHoursAA").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("DifferenceInMinutesAA").getExpression().evaluate(context);
        assertThat(result, is(45));

        result = context.resolveExpressionRef("DifferenceInDaysAA").getExpression().evaluate(context);
        assertThat(result, is(1));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.DurationBetweenEvaluator#evaluate(Context)}
     */
    @Test
    public void testDuration() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("DateTimeDurationBetweenYear").getExpression().evaluate(context);
        assertThat(result, is(5));

        result = context.resolveExpressionRef("DurationInWeeks").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("DurationInWeeks2").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("DurationInWeeks3").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef("DateTimeDurationBetweenYearOffset").getExpression().evaluate(context);
        assertThat(result, is(4));

        result = context.resolveExpressionRef("DateTimeDurationBetweenMonth").getExpression().evaluate(context);
        assertThat(result, is(0));

        result = context.resolveExpressionRef("DateTimeDurationBetweenDaysDiffYears").getExpression().evaluate(context);
        assertThat(result, is(-788));

        result = context.resolveExpressionRef("DateTimeDurationBetweenUncertainInterval").getExpression().evaluate(context);
        Assert.assertTrue(((Uncertainty)result).getUncertaintyInterval().equal(new Interval(17, true, 44, true)));

        // result = context.resolveExpressionRef("DateTimeDurationBetweenUncertainInterval2").getExpression().evaluate(context);
        // assertThat(((Uncertainty)result).getUncertaintyInterval(), is(new Interval(5, true, 17, true)));

        result = context.resolveExpressionRef("DateTimeDurationBetweenUncertainAdd").getExpression().evaluate(context);
        Assert.assertTrue(((Uncertainty)result).getUncertaintyInterval().equal(new Interval(34, true, 88, true)));

        result = context.resolveExpressionRef("DateTimeDurationBetweenUncertainSubtract").getExpression().evaluate(context);
        Assert.assertTrue(((Uncertainty)result).getUncertaintyInterval().equal(new Interval(12, true, 28, true)));

        result = context.resolveExpressionRef("DateTimeDurationBetweenUncertainMultiply").getExpression().evaluate(context);
        Assert.assertTrue(((Uncertainty)result).getUncertaintyInterval().equal(new Interval(289, true, 1936, true)));

        try {
            result = context.resolveExpressionRef("DateTimeDurationBetweenUncertainDiv").getExpression().evaluate(context);
            Assert.fail();
        } catch (RuntimeException re) {
            // pass
        }

        result = context.resolveExpressionRef("DateTimeDurationBetweenMonthUncertain").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeDurationBetweenMonthUncertain2").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("DateTimeDurationBetweenMonthUncertain3").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeDurationBetweenMonthUncertain4").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeDurationBetweenMonthUncertain5").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeDurationBetweenMonthUncertain6").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeDurationBetweenMonthUncertain7").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DurationInYears").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("TimeDurationBetweenHour").getExpression().evaluate(context);
        assertThat(result, is(2));

        result = context.resolveExpressionRef("TimeDurationBetweenHourDiffPrecision").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("TimeDurationBetweenMinute").getExpression().evaluate(context);
        assertThat(result, is(4));

        result = context.resolveExpressionRef("TimeDurationBetweenSecond").getExpression().evaluate(context);
        assertThat(result, is(4));

        result = context.resolveExpressionRef("TimeDurationBetweenMillis").getExpression().evaluate(context);
        assertThat(result, is(5));

        // TODO: Uncomment once translator issue https://github.com/cqframework/clinical_quality_language/issues/256 is resolved
//        result = context.resolveExpressionRef("DurationInHoursA").getExpression().evaluate(context);
//        assertThat(result, is(1));
//
//        result = context.resolveExpressionRef("DurationInMinutesA").getExpression().evaluate(context);
//        assertThat(result, is(45));
//
//        result = context.resolveExpressionRef("DurationInDaysA").getExpression().evaluate(context);
//        assertThat(result, is(1));

        result = context.resolveExpressionRef("DurationInHoursAA").getExpression().evaluate(context);
        assertThat(result, is(1));

        result = context.resolveExpressionRef("DurationInMinutesAA").getExpression().evaluate(context);
        assertThat(result, is(45));

        result = context.resolveExpressionRef("DurationInDaysAA").getExpression().evaluate(context);
        assertThat(result, is(0));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.NowEvaluator#evaluate(Context)}
     */
    @Test
    public void testNow() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("DateTimeNow").getExpression().evaluate(context);
        assertThat(result, is(true));

//        context = new Context(library, new DateTime(new Partial(DateTime.getFields(7), new int[] {2016, 6, 10, 5, 5, 4, 999}), DateTimeZone.forOffsetHours(-7)));
        context = new Context(library, new DateTime(2016, 6, 10, 5, 5, 4, 999, null));
        result = context.resolveExpressionRef("Issue34A").getExpression().evaluate(context);
//        assertThat(((DateTime) result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2016, 6, 10, 5, 5, 4, 999})));
//        assertThat(((DateTime) result).getTimezoneOffset(), is(new BigDecimal("-7.00")));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, 6, 10, 5, 5, 4, 999, new BigDecimal("-7.0"))));
        Assert.assertTrue(((DateTime) result).getDecimalOffset().compareTo(new BigDecimal("-7.0")) == 0);
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.SameAsEvaluator#evaluate(Context)}
     */
    @Test
    public void testSameAs() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("DateTimeSameAsYearTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameAsYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameAsMonthTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameAsMonthFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameAsDayTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameAsDayFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameAsHourTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameAsHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameAsMinuteTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameAsMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameAsSecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameAsSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameAsMillisecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameAsMillisecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameAsNull").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SameAsTimezoneTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("SameAsTimezoneFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeSameAsHourTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameAsHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeSameAsMinuteTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameAsMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeSameAsSecondTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameAsSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeSameAsMillisTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameAsMillisFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.SameOrAfterEvaluator#evaluate(Context)}
     */
    @Test
    public void testSameOrAfter() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("DateTimeSameOrAfterYearTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterYearTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrAfterMonthTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterMonthTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterMonthFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrAfterDayTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterDayTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterDayFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrAfterHourTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterHourTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrAfterMinuteTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterMinuteTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrAfterSecondTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterSecondTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrAfterMillisecondTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterMillisecondTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrAfterMillisecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrAfterNull1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SameOrAfterTimezoneTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("SameOrAfterTimezoneFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeSameOrAfterHourTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameOrAfterHourTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameOrAfterHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeSameOrAfterMinuteTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameOrAfterMinuteTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameOrAfterMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeSameOrAfterSecondTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameOrAfterSecondTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameOrAfterSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("TimeSameOrAfterMillisTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameOrAfterMillisTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("TimeSameOrAfterMillisFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("OnOrAfterTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("Issue32DateTime").getExpression().evaluate(context);
        assertThat(result, is(true));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.SameOrBeforeEvaluator#evaluate(Context)}
     */
    @Test
    public void testSameOrBefore() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("DateTimeSameOrBeforeYearTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeMonthTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeMonthTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeMonthFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeDayTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeDayTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeDayFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeHourTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeHourTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeHourFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeMinuteTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeMinuteTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeMinuteFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeSecondTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeSecondTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeSecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeMillisecondTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeMillisecondTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeMillisecondFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeNull1").getExpression().evaluate(context);
        assertThat(result, is(nullValue()));

        result = context.resolveExpressionRef("SameOrBeforeTimezoneTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("SameOrBeforeTimezoneFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeYearFalse").getExpression().evaluate(context);
        assertThat(result, is(false));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.SubtractEvaluator#evaluate(Context)}
     */
    @Test
    public void testSubtract() throws JAXBException {
        Context context = new Context(library);
        Object result = context.resolveExpressionRef("DateTimeSubtract5Years").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2000, 10, 10})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2000, 10, 10, null, null, null, null, new BigDecimal("-7.0"))));

        try {
            context.resolveExpressionRef("DateTimeSubtractInvalidYears").getExpression().evaluate(context);
        } catch (ArithmeticException ae) {
            assertThat(ae.getMessage(), is("The date time addition results in a year less than the accepted range."));
        }

        result = context.resolveExpressionRef("DateTimeSubtract5Months").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2005, 1, 10})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 1, 10, null, null, null, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtractMonthsUnderflow").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2004, 11, 10})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2004, 11, 10, null, null, null, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtract5Days").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2005, 5, 5})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 5, 5, null, null, null, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtractDaysUnderflow").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2016, 5, 30})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, 5, 30, null, null, null, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtract5Hours").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(4), new int[] {2005, 5, 10, 5})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 5, 10, 5, null, null, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtractHoursUnderflow").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(4), new int[] {2016, 6, 9, 23})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, 6, 9, 23, null, null, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtract5Minutes").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2005, 5, 10, 5, 5})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 5, 10, 5, 5, null, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtractMinutesUnderflow").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(5), new int[] {2016, 6, 10, 4, 59})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, 6, 10, 4, 59, null, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtract5Seconds").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2005, 5, 10, 5, 5, 5})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 5, 10, 5, 5, 5, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtractSecondsUnderflow").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(6), new int[] {2016, 6, 10, 5, 4, 59})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, 6, 10, 5, 4, 59, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtract5Milliseconds").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2005, 5, 10, 5, 5, 5, 5})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2005, 5, 10, 5, 5, 5, 5, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtractMillisecondsUnderflow").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(7), new int[] {2016, 6, 10, 5, 5, 4, 999})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, 6, 10, 5, 5, 4, 999, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtract2YearsAsMonths").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2012})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2012, null, null, null, null, null, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("DateTimeSubtract2YearsAsMonthsRem1").getExpression().evaluate(context);
//        assertThat(((DateTime)result).getPartial(), is(new Partial(DateTime.getFields(1), new int[] {2011})));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2012, null, null, null, null, null, null, new BigDecimal("-7.0"))));

        result = context.resolveExpressionRef("TimeSubtract5Hours").getExpression().evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {10, 59, 59, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(10, 59, 59, 999, null)));

        result = context.resolveExpressionRef("TimeSubtract1Minute").getExpression().evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 58, 59, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(15, 58, 59, 999, null)));

        result = context.resolveExpressionRef("TimeSubtract1Second").getExpression().evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 58, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(15, 59, 58, 999, null)));

        result = context.resolveExpressionRef("TimeSubtract1Millisecond").getExpression().evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {15, 59, 58, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(15, 59, 58, 999, null)));

        result = context.resolveExpressionRef("TimeSubtract5Hours1Minute").getExpression().evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {10, 58, 59, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(10, 58, 59, 999, null)));

        result = context.resolveExpressionRef("TimeSubtract5hoursByMinute").getExpression().evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {10, 59, 59, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(10, 59, 59, 999, null)));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.TimeEvaluator#evaluate(Context)}
     */
    @Test
    public void testTime() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("TimeTest2").getExpression().evaluate(context);
//        assertThat(((Time)result).getPartial(), is(new Partial(Time.getFields(4), new int[] {23, 59, 59, 999})));
        Assert.assertTrue(((Time) result).equal(new Time(23, 59, 59, 999, null)));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.TimeOfDayEvaluator#evaluate(Context)}
     */
    @Test
    public void testTimeOfDay() throws JAXBException {
        Context context = new Context(library);
        // TODO: uncomment once Time(x,x,x,x,x) format is fixed
        // Object result = context.resolveExpressionRef("TimeOfDayTest").getExpression().evaluate(context);
        // assertThat(((Time)result).getPartial().getValue(0), is(10));
    }

    /**
     * {@link org.opencds.cqf.cql.elm.execution.TodayEvaluator#evaluate(Context)}
     */
    @Test
    public void testToday() throws JAXBException {
        Context context = new Context(library);

        Object result = context.resolveExpressionRef("DateTimeSameOrBeforeTodayTrue1").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeTodayTrue2").getExpression().evaluate(context);
        assertThat(result, is(true));

        result = context.resolveExpressionRef("DateTimeSameOrBeforeTodayFalse").getExpression().evaluate(context);
        assertThat(result, is(false));

        result = context.resolveExpressionRef("DateTimeAddTodayTrue").getExpression().evaluate(context);
        assertThat(result, is(true));

//        context = new Context(library, new DateTime(new Partial(DateTime.getFields(7), new int[] {2016, 6, 10, 5, 5, 4, 999}), DateTimeZone.forOffsetHours(-7)));
        context = new Context(library, new DateTime(2016, 6, 10, 5, 5, 4, 999, null));

        result = context.resolveExpressionRef("Issue34B").getExpression().evaluate(context);
//        assertThat(((DateTime) result).getPartial(), is(new Partial(DateTime.getFields(3), new int[] {2016, 6, 10})));
//        assertThat(((DateTime) result).getTimezoneOffset(), is(new BigDecimal("-7.00")));
        Assert.assertTrue(((DateTime) result).equal(new DateTime(2016, 6, 10, null, null, null, null, new BigDecimal("-7.0"))));
        Assert.assertTrue(((DateTime) result).getDecimalOffset().compareTo(new BigDecimal("-7.0")) == 0);
    }

}
