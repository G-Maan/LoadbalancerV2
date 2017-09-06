package loadbalancer.configuration;

import loadbalancer.model.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Pawel Mielniczuk on 2017-08-31.
 */
@Component
@PropertySource(value = {"groups-configuration.properties"})
public class GroupsConfiguration {

    private static final BigInteger MAX_RANGE = BigInteger.valueOf(Integer.MAX_VALUE); //Max possible hashcode
    private static final BigInteger MIN_RANGE = BigInteger.valueOf(Integer.MIN_VALUE); //Min possible hashcode
    private static final int MAX_PERCENTAGE = 100;

    @Value("#{${groups}}")
    private Map<String, Integer> groupsConfiguration = new LinkedHashMap<>();

    private Map<String, Range> groupsRanges = new ConcurrentHashMap<>();

    @PostConstruct
    public void initialize() {
        calculateGroupRanges();
    }

    public Map<String, Range> getGroupsRanges() {
        return groupsRanges;
    }

    private void calculateGroupRanges() {
        Map<String, BigInteger> ranges = new LinkedHashMap<>();
        groupsConfiguration.entrySet().forEach(entry ->
                ranges.put(entry.getKey(),
                        calculateRangePercentage(entry.getValue())
                )
        );
        Iterator<Map.Entry<String, BigInteger>> iterator = ranges.entrySet().iterator();
        Map.Entry<String, BigInteger> entry = iterator.next();
        groupsRanges.put(entry.getKey(), new Range(MIN_RANGE, MIN_RANGE.add(entry.getValue())));
        Map.Entry<String, BigInteger> currentEntry;
        Range previousRange;
        while (iterator.hasNext()) {
            currentEntry = iterator.next();
            previousRange = groupsRanges.get(entry.getKey());
            if (iterator.hasNext()) {
                groupsRanges.put(currentEntry.getKey(), new Range(previousRange.getEnd().add(BigInteger.ONE), previousRange.getEnd().add(currentEntry.getValue())));
                entry = currentEntry;
            } else {
                groupsRanges.put(currentEntry.getKey(), new Range(previousRange.getEnd().add(BigInteger.ONE), MAX_RANGE));
            }
        }
    }

    private BigInteger calculateRangePercentage(Integer percentage) {
        BigInteger range = MIN_RANGE.abs().add(MAX_RANGE).abs();
        return range.multiply(BigInteger.valueOf(percentage)).divide(BigInteger.valueOf(MAX_PERCENTAGE));
    }
}
