package loadbalancer.model;

/**
 * Created by Pawel Mielniczuk on 2017-08-30.
 */
public class Range {

    private int start;
    private int end;

    public Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getEnd() {
        return end;
    }

    public boolean includes(int value) {
        return value >= start && value <= end;
    }

}
