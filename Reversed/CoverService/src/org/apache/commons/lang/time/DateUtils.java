package org.apache.commons.lang.time;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TimeZone;

public class DateUtils {
    public static final int MILLIS_IN_DAY = 86400000;
    public static final int MILLIS_IN_HOUR = 3600000;
    public static final int MILLIS_IN_MINUTE = 60000;
    public static final int MILLIS_IN_SECOND = 1000;
    public static final long MILLIS_PER_DAY = 86400000;
    public static final long MILLIS_PER_HOUR = 3600000;
    public static final long MILLIS_PER_MINUTE = 60000;
    public static final long MILLIS_PER_SECOND = 1000;
    public static final int RANGE_MONTH_MONDAY = 6;
    public static final int RANGE_MONTH_SUNDAY = 5;
    public static final int RANGE_WEEK_CENTER = 4;
    public static final int RANGE_WEEK_MONDAY = 2;
    public static final int RANGE_WEEK_RELATIVE = 3;
    public static final int RANGE_WEEK_SUNDAY = 1;
    public static final int SEMI_MONTH = 1001;
    public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
    private static final int[][] fields;

    static class DateIterator implements Iterator {
        private final Calendar endFinal;
        private final Calendar spot;

        DateIterator(Calendar startFinal, Calendar endFinal) {
            this.endFinal = endFinal;
            this.spot = startFinal;
            this.spot.add(5, -1);
        }

        public boolean hasNext() {
            return this.spot.before(this.endFinal);
        }

        public Object next() {
            if (this.spot.equals(this.endFinal)) {
                throw new NoSuchElementException();
            }
            this.spot.add(5, 1);
            return this.spot.clone();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    static {
        r0 = new int[8][];
        r0[0] = new int[]{14};
        r0[1] = new int[]{13};
        r0[2] = new int[]{12};
        r0[3] = new int[]{11, 10};
        r0[4] = new int[]{5, 5, 9};
        r0[5] = new int[]{2, SEMI_MONTH};
        r0[6] = new int[]{1};
        r0[7] = new int[]{0};
        fields = r0;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isSameInstant(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            return date1.getTime() == date2.getTime();
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameInstant(Calendar cal1, Calendar cal2) {
        if (cal1 != null && cal2 != null) {
            return cal1.getTime().getTime() == cal2.getTime().getTime();
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (cal1.get(14) == cal2.get(14) && cal1.get(13) == cal2.get(13) && cal1.get(12) == cal2.get(12) && cal1.get(10) == cal2.get(10) && cal1.get(6) == cal2.get(6) && cal1.get(1) == cal2.get(1) && cal1.get(0) == cal2.get(0) && cal1.getClass() == cal2.getClass()) {
            return true;
        } else {
            return false;
        }
    }

    public static Date parseDate(String str, String[] parsePatterns) throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }
        SimpleDateFormat parser = null;
        ParsePosition pos = new ParsePosition(0);
        for (int i = 0; i < parsePatterns.length; i++) {
            if (i == 0) {
                parser = new SimpleDateFormat(parsePatterns[0]);
            } else {
                parser.applyPattern(parsePatterns[i]);
            }
            pos.setIndex(0);
            Date date = parser.parse(str, pos);
            if (date != null && pos.getIndex() == str.length()) {
                return date;
            }
        }
        throw new ParseException(new StringBuffer().append("Unable to parse the date: ").append(str).toString(), -1);
    }

    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        return add(date, 3, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    public static Date addMilliseconds(Date date, int amount) {
        return add(date, 14, amount);
    }

    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    public static Date round(Date date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar gval = Calendar.getInstance();
        gval.setTime(date);
        modify(gval, field, true);
        return gval.getTime();
    }

    public static Calendar round(Calendar date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar rounded = (Calendar) date.clone();
        modify(rounded, field, true);
        return rounded;
    }

    public static Date round(Object date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (date instanceof Date) {
            return round((Date) date, field);
        } else {
            if (date instanceof Calendar) {
                return round((Calendar) date, field).getTime();
            }
            throw new ClassCastException(new StringBuffer().append("Could not round ").append(date).toString());
        }
    }

    public static Date truncate(Date date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar gval = Calendar.getInstance();
        gval.setTime(date);
        modify(gval, field, false);
        return gval.getTime();
    }

    public static Calendar truncate(Calendar date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar truncated = (Calendar) date.clone();
        modify(truncated, field, false);
        return truncated;
    }

    public static Date truncate(Object date, int field) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (date instanceof Date) {
            return truncate((Date) date, field);
        } else {
            if (date instanceof Calendar) {
                return truncate((Calendar) date, field).getTime();
            }
            throw new ClassCastException(new StringBuffer().append("Could not truncate ").append(date).toString());
        }
    }

    private static void modify(Calendar val, int field, boolean round) {
        if (val.get(1) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        } else if (field != 14) {
            Date date = val.getTime();
            long time = date.getTime();
            boolean done = false;
            int millisecs = val.get(14);
            if (!round || millisecs < 500) {
                time -= (long) millisecs;
                if (field == 13) {
                    done = true;
                }
            }
            int seconds = val.get(13);
            if (!done && (!round || seconds < 30)) {
                time -= ((long) seconds) * 1000;
                if (field == 12) {
                    done = true;
                }
            }
            int minutes = val.get(12);
            if (!done && (!round || minutes < 30)) {
                time -= ((long) minutes) * MILLIS_PER_MINUTE;
            }
            if (date.getTime() != time) {
                date.setTime(time);
                val.setTime(date);
            }
            boolean roundUp = false;
            for (int i = 0; i < fields.length; i++) {
                int j = 0;
                while (j < fields[i].length) {
                    if (fields[i][j] != field) {
                        j++;
                    } else if (!round || !roundUp) {
                        return;
                    } else {
                        if (field != 1001) {
                            val.add(fields[i][0], 1);
                            return;
                        } else if (val.get(5) == 1) {
                            val.add(5, 15);
                            return;
                        } else {
                            val.add(5, -15);
                            val.add(2, 1);
                            return;
                        }
                    }
                }
                int offset = 0;
                boolean offsetSet = false;
                switch (field) {
                    case 9:
                        if (fields[i][0] == 11) {
                            offset = val.get(11);
                            if (offset >= 12) {
                                offset -= 12;
                            }
                            roundUp = offset > 6;
                            offsetSet = true;
                            break;
                        }
                        break;
                    case SEMI_MONTH /*1001*/:
                        if (fields[i][0] == 5) {
                            offset = val.get(5) - 1;
                            if (offset >= 15) {
                                offset -= 15;
                            }
                            roundUp = offset > 7;
                            offsetSet = true;
                            break;
                        }
                        break;
                }
                if (!offsetSet) {
                    int min = val.getActualMinimum(fields[i][0]);
                    offset = val.get(fields[i][0]) - min;
                    roundUp = offset > (val.getActualMaximum(fields[i][0]) - min) / 2;
                }
                if (offset != 0) {
                    val.set(fields[i][0], val.get(fields[i][0]) - offset);
                }
            }
            throw new IllegalArgumentException(new StringBuffer().append("The field ").append(field).append(" is not supported").toString());
        }
    }

    public static Iterator iterator(Date focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar gval = Calendar.getInstance();
        gval.setTime(focus);
        return iterator(gval, rangeStyle);
    }

    public static Iterator iterator(Calendar focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar start;
        Calendar end;
        int startCutoff = 1;
        int endCutoff = 7;
        switch (rangeStyle) {
            case 1:
            case 2:
            case 3:
            case RANGE_WEEK_CENTER /*4*/:
                start = truncate(focus, 5);
                end = truncate(focus, 5);
                switch (rangeStyle) {
                    case 1:
                        break;
                    case 2:
                        startCutoff = 2;
                        endCutoff = 1;
                        break;
                    case 3:
                        startCutoff = focus.get(7);
                        endCutoff = startCutoff - 1;
                        break;
                    case RANGE_WEEK_CENTER /*4*/:
                        startCutoff = focus.get(7) - 3;
                        endCutoff = focus.get(7) + 3;
                        break;
                    default:
                        break;
                }
            case RANGE_MONTH_SUNDAY /*5*/:
            case RANGE_MONTH_MONDAY /*6*/:
                start = truncate(focus, 2);
                end = (Calendar) start.clone();
                end.add(2, 1);
                end.add(5, -1);
                if (rangeStyle == 6) {
                    startCutoff = 2;
                    endCutoff = 1;
                    break;
                }
                break;
            default:
                throw new IllegalArgumentException(new StringBuffer().append("The range style ").append(rangeStyle).append(" is not valid.").toString());
        }
        if (startCutoff < 1) {
            startCutoff += 7;
        }
        if (startCutoff > 7) {
            startCutoff -= 7;
        }
        if (endCutoff < 1) {
            endCutoff += 7;
        }
        if (endCutoff > 7) {
            endCutoff -= 7;
        }
        while (start.get(7) != startCutoff) {
            start.add(5, -1);
        }
        while (end.get(7) != endCutoff) {
            end.add(5, 1);
        }
        return new DateIterator(start, end);
    }

    public static Iterator iterator(Object focus, int rangeStyle) {
        if (focus == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else if (focus instanceof Date) {
            return iterator((Date) focus, rangeStyle);
        } else {
            if (focus instanceof Calendar) {
                return iterator((Calendar) focus, rangeStyle);
            }
            throw new ClassCastException(new StringBuffer().append("Could not iterate based on ").append(focus).toString());
        }
    }
}
