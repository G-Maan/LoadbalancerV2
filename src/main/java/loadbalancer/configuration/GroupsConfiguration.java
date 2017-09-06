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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Pawel Mielniczuk on 2017-08-31.
 */
@Component
@PropertySource(value = {"groups-configuration.properties"})
public class GroupsConfiguration {

    private static final int MAX_RANGE = Integer.MAX_VALUE; //Max possible hashcode
    private static final int MIN_RANGE = Integer.MIN_VALUE; //Min possible hashcode
    private static final int MAX_PERCENTAGE = 100;
    private static final BigInteger INTEGER_RANGE = BigInteger.valueOf(MIN_RANGE).abs().add(BigInteger.valueOf(MAX_RANGE));

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
        Map<String, Integer> ranges = initializeRanges();

        Iterator<Entry<String, Integer>> iterator = ranges.entrySet().iterator();
        Entry<String, Integer> entry = iterator.next();
        groupsRanges.put(entry.getKey(), new Range(MIN_RANGE, MIN_RANGE + entry.getValue()));
        Entry<String, Integer> currentEntry;
        int previousEnd;

        while (iterator.hasNext()) {
            currentEntry = iterator.next();
            previousEnd = groupsRanges.get(entry.getKey()).getEnd();
            if (iterator.hasNext()) {
                groupsRanges.put(currentEntry.getKey(), new Range(previousEnd + 1, previousEnd + currentEntry.getValue()));
                entry = currentEntry;
            } else {
                groupsRanges.put(currentEntry.getKey(), new Range(previousEnd + 1, MAX_RANGE));
            }
        }
    }

    private Map<String, Integer> initializeRanges() {
        Map<String, Integer> ranges = new LinkedHashMap<>();
        groupsConfiguration.entrySet().forEach(entry ->
                ranges.put(entry.getKey(),
                        calculateRangePercentage(entry.getValue())
                )
        );
        return ranges;
    }

    private int calculateRangePercentage(Integer percentage) {
        return INTEGER_RANGE.multiply(BigInteger.valueOf(percentage)).divide(BigInteger.valueOf(MAX_PERCENTAGE)).intValue();
    }
}
