package loadbalancer.model;

import java.math.BigInteger;

/**
 * Created by Pawel Mielniczuk on 2017-08-30.
 */
public class Range {

    private BigInteger start;
    private BigInteger end;

    public Range(BigInteger start, BigInteger end) {
        this.start = start;
        this.end = end;
    }

    public BigInteger getEnd() {
        return end;
    }

    public boolean includes(int value) {
        return start.compareTo(BigInteger.valueOf(value)) != 1 && end.compareTo(BigInteger.valueOf(value)) != -1;
    }

    @Override
    public String toString() {
        return "Range start: " +
                start +
                " , end: " +
                end;
    }
}
