package org.opencds.cqf.cql.runtime;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.Partial;
import org.opencds.cqf.cql.elm.execution.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by Bryn on 4/15/2016.
 */
public class Interval {
    public Interval(Object low, boolean lowClosed, Object high, boolean highClosed) {
        this.low = low;
        this.lowClosed = lowClosed;
        this.high = high;
        this.highClosed = highClosed;

        if (this.low != null) {
            pointType = this.low.getClass();
        }
        else if (this.high != null) {
            pointType = this.high.getClass();
        }

        if (pointType == null) {
            throw new IllegalArgumentException("Low or high boundary of an interval must be present.");
        }

        if ((this.low != null && this.low.getClass() != pointType)
            || (this.high != null && this.high.getClass() != pointType)) {
            throw new IllegalArgumentException("Low and high boundary values of an interval must be of the same type.");
        }
    }

    public static Comparator<Interval> sortInterval = new Comparator<Interval>() {
        @Override
        public int compare(Interval o1, Interval o2) {
            if (o1.getStart() instanceof Integer) {
                return ((Integer)o1.getStart()).compareTo((Integer)o2.getStart());
            }
            else if (o1.getStart() instanceof DateTime) {
                return ((DateTime)o1.getStart()).compareTo((DateTime)o2.getStart());
            }
            else if (o1.getStart() instanceof Time) {
                return ((Time)o1.getStart()).compareTo((Time)o2.getStart());
            }
            else if (o1.getStart() instanceof BigDecimal) {
                return ((BigDecimal)o1.getStart()).compareTo((BigDecimal)o2.getStart());
            }
            else if (o1.getStart() instanceof Quantity) {
                return (((Quantity)o1.getStart()).getValue().compareTo(((Quantity)o2.getStart()).getValue()));
            }
            throw new IllegalArgumentException(String.format("Cannot Collapse arguments of type '%s' and '%s'.", o1.getClass().getName(), o1.getClass().getName()));
        }
    };

    public static Object getSize(Object start, Object end) {
      if (start == null || end == null) { return null; }

      if (start instanceof Integer || start instanceof BigDecimal || start instanceof Quantity) {
        return Execution.resolveArithmeticDoOperation(new SubtractEvaluator(), end, start);
      }
      else if (start instanceof DateTime) {
        return new Quantity()
          .withValue(new BigDecimal(DurationBetweenEvaluator.between((DateTime)start, (DateTime)end, ((DateTime)start).getPartial().size() - 1)))
          .withUnit(DateTime.getUnit(((DateTime)start).getPartial().size() - 1));
      }
      else if (start instanceof Time) {
        return new Quantity()
          .withValue(new BigDecimal(DurationBetweenEvaluator.between((Time)start, (Time)end, ((Time)start).getPartial().size() - 1)))
          .withUnit(Time.getUnit(((Time)start).getPartial().size() - 1));
      }

      throw new IllegalArgumentException(String.format("Cannot getIntervalSize argument of type '%s'.", start.getClass().getName()));

    }

    private Object low;
    public Object getLow() {
        return low;
    }

    private boolean lowClosed;
    public boolean getLowClosed() {
        return lowClosed;
    }

    private Object high;
    public Object getHigh() {
        return high;
    }

    private boolean highClosed;
    public boolean getHighClosed() {
        return highClosed;
    }

    private Type pointType;

    /*
    Returns the starting point of the interval.

    If the low boundary of the interval is open, returns the Successor of the low value of the interval.
    Note that if the low value of the interval is null, the result is null.

    If the low boundary of the interval is closed and the low value of the interval is not null,
    returns the low value of the interval. Otherwise, the result is the minimum value of
    the point type of the interval.
     */
    public Object getStart() {
        if (!lowClosed) {
            return successor(low);
        }
        else {
            return low == null ? minValue(pointType) : low;
        }
    }

    public Boolean equal(Interval other) {
        Object leftStart = this.getStart();
        Object leftEnd = this.getEnd();
        Object rightStart = other.getStart();
        Object rightEnd = other.getEnd();

        if (leftStart == null || leftEnd == null || rightStart == null || rightEnd == null) { return null; }

        return ((Boolean) Execution.resolveComparisonDoOperation(new EqualEvaluator(), leftStart, rightStart)
                && this.getLowClosed() == other.getLowClosed()
                && (Boolean) Execution.resolveComparisonDoOperation(new EqualEvaluator(), leftEnd, rightEnd))
                && this.getHighClosed() == other.getHighClosed();
    }

    /*
    Returns the ending point of an interval.

    If the high boundary of the interval is open, returns the Predecessor of the high value of the interval.
    Note that if the high value of the interval is null, the result is null.

    If the high boundary of the interval is closed and the high value of the interval is not null,
    returns the high value of the interval. Otherwise, the result is the maximum value of
    the point type of the interval.
     */
    public Object getEnd() {
        if (!highClosed) {
            return predecessor(high);
        }
        else {
            return high == null ? maxValue(pointType) : high;
        }
    }

    public static Object successor(Object value) {
        if (value == null) {
            return null;
        }

        else if (value instanceof Integer) {
          return ((Integer)value) + 1;
        }
        else if (value instanceof BigDecimal) {
          return ((BigDecimal)value).add(new BigDecimal("0.00000001"));
        }
        else if (value instanceof Quantity) {
          Quantity quantity = (Quantity)value;
          return new Quantity().withValue((BigDecimal)successor(quantity.getValue())).withUnit(quantity.getUnit());
        }
        else if (value instanceof DateTime) {
          if ((Boolean) Execution.resolveComparisonDoOperation(new GreaterOrEqualEvaluator(), value, maxValue(DateTime.class))) {
            throw new RuntimeException("The result of the successor operation exceeds the maximum value allowed for type DateTime.");
          }
          DateTime dt = (DateTime)value;
          return new DateTime().withPartial(dt.getPartial().property(DateTime.getField(dt.getPartial().size() - 1)).addToCopy(1))
                  .withTimezoneOffset(dt.getTimezoneOffset());
        }
        else if (value instanceof Time) {
          if ((Boolean) Execution.resolveComparisonDoOperation(new GreaterOrEqualEvaluator(), value, maxValue(Time.class))) {
            throw new RuntimeException("The result of the successor operation exceeds the maximum value allowed for type Time.");
          }
          Time t = (Time)value;
          return new Time().withPartial(t.getPartial().property(Time.getField(t.getPartial().size() - 1)).addToCopy(1))
                  .withTimezoneOffset(t.getTimezoneOffset());
        }

        throw new NotImplementedException(String.format("Successor is not implemented for type %s", value.getClass().getName()));
    }

    public static Object predecessor(Object value) {
        if (value == null) {
            return null;
        }
        else if (value instanceof Integer) {
          return ((Integer)value) - 1;
        }
        else if (value instanceof BigDecimal) {
          return ((BigDecimal)value).subtract(new BigDecimal("0.00000001"));
        }
        else if (value instanceof Quantity) {
          Quantity quantity = (Quantity)value;
          return new Quantity().withValue((BigDecimal)predecessor(quantity.getValue())).withUnit(quantity.getUnit());
        }
        else if (value instanceof DateTime) {
          if ((Boolean) Execution.resolveComparisonDoOperation(new LessOrEqualEvaluator(), value, minValue(DateTime.class))) {
            throw new RuntimeException("The result of the predecessor operation exceeds the minimum value allowed for type DateTime.");
          }
          DateTime dt = (DateTime)value;
          return new DateTime().withPartial(dt.getPartial().property(DateTime.getField(dt.getPartial().size() - 1)).addToCopy(-1))
                  .withTimezoneOffset(dt.getTimezoneOffset());
        }
        else if (value instanceof Time) {
          if ((Boolean) Execution.resolveComparisonDoOperation(new LessOrEqualEvaluator(), value, minValue(Time.class))) {
            throw new RuntimeException("The result of the predecessor operation exceeds the minimum value allowed for type Time.");
          }
          Time t = (Time)value;
          return new Time().withPartial(t.getPartial().property(Time.getField(t.getPartial().size() - 1)).addToCopy(-1))
                  .withTimezoneOffset(t.getTimezoneOffset());
        }

        throw new NotImplementedException(String.format("Predecessor is not implemented for type %s", value.getClass().getName()));
    }

    public static Object minValue(Type type) {
        if (type == Integer.class) {
            return Integer.MIN_VALUE;
        }
        else if (type == BigDecimal.class) {
            return new BigDecimal("-9999999999999999999999999999.99999999");
        }
        else if (type == Quantity.class) {
            return new Quantity().withValue((BigDecimal)minValue(BigDecimal.class));
        }
        else if (type == DateTime.class) {
          return new DateTime().withPartial(new Partial(DateTime.fields, new int[] {0001, 1, 1, 0, 0, 0, 0}));
        }
        else if (type == Time.class) {
          return new Time().withPartial(new Partial(Time.fields, new int[] {0, 0, 0, 0}));
        }

        throw new NotImplementedException(String.format("MinValue is not implemented for type %s.", type.getTypeName()));
    }

    public static Object maxValue(Type type) {
        if (type == Integer.class) {
            return Integer.MAX_VALUE;
        }
        else if (type == BigDecimal.class) {
            return new BigDecimal("9999999999999999999999999999.99999999");
        }
        else if (type == Quantity.class) {
            return new Quantity().withValue((BigDecimal)maxValue(BigDecimal.class));
        }
        else if (type == DateTime.class) {
          return new DateTime().withPartial(new Partial(DateTime.fields, new int[] {9999, 12, 31, 23, 59, 59, 999}));
        }
        else if (type == Time.class) {
          return new Time().withPartial(new Partial(Time.fields, new int[] {23, 59, 59, 999}));
        }

        throw new NotImplementedException(String.format("MaxValue is not implemented for type %s.", type.getTypeName()));
    }
}
