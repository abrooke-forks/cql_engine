package org.opencds.cqf.cql.runtime;

import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public enum Precision {
    YEAR(0),
    MONTH(1),
    WEEK(2),
    DAY(3),
    HOUR(4),
    MINUTE(5),
    SECOND(6),
    MILLI(7);

    public final int field;

    Precision(int field) {
        this.field = field;
    }

    public static Precision getPrecisionFromField(int field) {
        switch (field) {
            case 0: return YEAR;
            case 1: return MONTH;
            case 2: return WEEK;
            case 3: return DAY;
            case 4: return HOUR;
            case 5: return MINUTE;
            case 6: return SECOND;
            case 7: return MILLI;
            default: throw new IllegalArgumentException("Invalid field: " + field);
        }
    }

    public static Precision getPrecisionFromFieldNoWeeks(int field) {
        switch (field) {
            case 0: return YEAR;
            case 1: return MONTH;
            case 2: return DAY;
            case 3: return HOUR;
            case 4: return MINUTE;
            case 5: return SECOND;
            case 6: return MILLI;
            default: throw new IllegalArgumentException("Invalid field: " + field);
        }
    }

    public static Precision toPrecision(String precisionString) {
        switch (precisionString.toLowerCase()) {
            case"a":case"y":case"yr":case"year":case"years": return YEAR;
            case"mo":case"month":case"months": return MONTH;
            case"wk":case"week":case"weeks": return WEEK;
            case"d":case"day":case"days": return DAY;
            case"h":case"hr":case"hour":case"hours": return HOUR;
            case"min":case"minute":case"minutes": return MINUTE;
            case"s":case"sec":case"second":case"seconds": return SECOND;
            case"ms":case"millisecond":case"milliseconds": return MILLI;
            default: throw new IllegalArgumentException("Invalid precision unit: " + precisionString);
        }
    }

    public String getUnitFromPrecision() {
        // todo - handle weeks case
        switch (this) {
            case YEAR: return "years";
            case MONTH: return "months";
            case DAY: return "days";
            case HOUR: return "hours";
            case MINUTE: return "minutes";
            case SECOND: return "seconds";
            default: return "milliseconds";
        }
    }

    public int getCalendarFieldIndex() {
        // todo - handle weeks case
        switch (this) {
            case YEAR: return Calendar.YEAR;
            case MONTH: return Calendar.MONTH;
            case DAY: return Calendar.DAY_OF_MONTH;
            case HOUR: return Calendar.HOUR_OF_DAY;
            case MINUTE: return Calendar.MINUTE;
            case SECOND: return Calendar.SECOND;
            default: return Calendar.MILLISECOND;
        }
    }

    public ChronoField getChronoFieldIndex() {
        // todo - handle weeks case
        switch (this) {
            case YEAR: return ChronoField.YEAR;
            case MONTH: return ChronoField.MONTH_OF_YEAR;
            case DAY: return ChronoField.DAY_OF_MONTH;
            case HOUR: return ChronoField.HOUR_OF_DAY;
            case MINUTE: return ChronoField.MINUTE_OF_HOUR;
            case SECOND: return ChronoField.SECOND_OF_MINUTE;
            default: return ChronoField.MILLI_OF_SECOND;
        }
    }

    public ChronoUnit getChronoUnitIndex() {
        // todo - handle weeks case
        switch (this) {
            case YEAR: return ChronoUnit.YEARS;
            case MONTH: return ChronoUnit.MONTHS;
            case DAY: return ChronoUnit.DAYS;
            case HOUR: return ChronoUnit.HOURS;
            case MINUTE: return ChronoUnit.MINUTES;
            case SECOND: return ChronoUnit.SECONDS;
            default: return ChronoUnit.MILLIS;
        }
    }

    public int getQuantityForPrecision(Quantity quantity) {
        switch (toPrecision(quantity.getUnit())) {
            case YEAR:
                return quantity.getValue().intValue();
            case MONTH:
                switch (this) {
                    case YEAR:
                        return quantity.getValue().intValue() / 12;
                    default:
                        return quantity.getValue().intValue();
                }
            case WEEK:
                switch (this) {
                    case YEAR:
                        return quantity.getValue().intValue() * 7 / 365;
                    case MONTH:
                        return quantity.getValue().intValue() * 7 / 30;
                    default:
                        return quantity.getValue().intValue() * 7;
                }
            case DAY:
                switch (this) {
                    case YEAR:
                        return quantity.getValue().intValue() / 365;
                    case MONTH:
                        return quantity.getValue().intValue() / 30;
                    default:
                        return quantity.getValue().intValue();
                }
            case HOUR:
                switch (this) {
                    case YEAR:
                        return quantity.getValue().intValue() / 24 / 365;
                    case MONTH:
                        return quantity.getValue().intValue() / 24 / 30;
                    case DAY:
                        return quantity.getValue().intValue() / 24;
                    default:
                        return quantity.getValue().intValue();
                }
            case MINUTE:
                switch (this) {
                    case YEAR:
                        return quantity.getValue().intValue() / 60 / 24 / 365;
                    case MONTH:
                        return quantity.getValue().intValue() / 60 / 24 / 30;
                    case DAY:
                        return quantity.getValue().intValue() / 60 / 24;
                    case HOUR:
                        return quantity.getValue().intValue() / 60;
                    default:
                        return quantity.getValue().intValue();
                }
            case SECOND:
                switch (this) {
                    case YEAR:
                        return quantity.getValue().intValue() / 60 / 60 / 24 / 365;
                    case MONTH:
                        return quantity.getValue().intValue() / 60 / 60 / 24 / 30;
                    case DAY:
                        return quantity.getValue().intValue() / 60 / 60 / 24;
                    case HOUR:
                        return quantity.getValue().intValue() / 60 / 60;
                    case MINUTE:
                        return quantity.getValue().intValue() / 60;
                    default:
                        return quantity.getValue().intValue();
                }
            default:
                switch (this) {
                    case YEAR:
                        return quantity.getValue().intValue() / 1000 / 60 / 60 / 24 / 365;
                    case MONTH:
                        return quantity.getValue().intValue() / 1000 / 60 / 60 / 24 / 30;
                    case DAY:
                        return quantity.getValue().intValue() / 1000 / 60 / 60 / 24;
                    case HOUR:
                        return quantity.getValue().intValue() / 1000 / 60 / 60;
                    case MINUTE:
                        return quantity.getValue().intValue() / 1000 / 60;
                    case SECOND:
                        return quantity.getValue().intValue() / 1000;
                    default:
                        return quantity.getValue().intValue();
                }
        }
    }

    public int getHighestPrecision(Precision other) {
        return this.getCalendarFieldIndex() > other.getCalendarFieldIndex() ? this.getCalendarFieldIndex() : other.getCalendarFieldIndex();
    }

    public boolean equals(Precision other) {
        return this == other;
    }
}

