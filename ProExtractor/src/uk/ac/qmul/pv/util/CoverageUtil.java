/*
 * @(#) CoverageUtil    Version 1.0.0    02-09-2016
 *
 */

package uk.ac.qmul.pv.util;

/**
 *
 * @author Suresh Hewapathirana
 */

import uk.ac.qmul.pv.model.Interval;
import java.util.*;

public class CoverageUtil {

    public ArrayList<Interval> merge(ArrayList<Interval> intervals) {
       
        if (intervals.isEmpty()) {
            return intervals;
        }
        if (intervals.size() == 1) {
            return intervals;
        }

        Collections.sort(intervals, new IntervalComparator());

        Interval first = intervals.get(0);
        int start = first.getStart();
        int end = first.getEnd();

        ArrayList<Interval> result = new ArrayList<>();

        for (int i = 1; i < intervals.size(); i++) {
            Interval current = intervals.get(i);
            if (current.getStart() <= end) {
                end = Math.max(current.getEnd(), end);
            } else {
                result.add(new Interval(start, end));
                start = current.getStart();
                end = current.getEnd();
            }
        }
        result.add(new Interval(start, end));

        return result;
    }
}

class IntervalComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Interval i1 = (Interval) o1;
        Interval i2 = (Interval) o2;
        return i1.getStart() - i2.getStart();
    }
}
