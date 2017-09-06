package loadbalancer.logic;

import loadbalancer.configuration.GroupsConfiguration;
import loadbalancer.model.Range;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Pawel Mielniczuk on 2017-09-04.
 */
public class LoadbalancerImplTest {

    private Loadbalancer loadbalancer;
    private GroupsConfiguration groupsConfiguration = mock(GroupsConfiguration.class);

    private Map<String, Range> groupsConfigurationRanges;

    @Before
    public void setUp() {
        initializeGroupsConfigurationMap();
        loadbalancer = new LoadbalancerImpl(groupsConfiguration);
    }

    @Test
    public void shouldAssignUserToAGroup() throws Exception {
        //given
        String userId = "abc:123"; //Hashcode -1207594214
        when(groupsConfiguration.getGroupsRanges()).thenReturn(groupsConfigurationRanges);

        //when
        String group = loadbalancer.getUserGroup(userId);

        //then
        Assert.assertEquals("groupB", group);
    }

    @Test
    public void shouldReturnSameUserGroupAsAssigned() throws Exception {
        //given
        String userId = "123:abc"; //Hashcode 2018388762
        when(groupsConfiguration.getGroupsRanges()).thenReturn(groupsConfigurationRanges);

        //when
        String group = loadbalancer.getUserGroup(userId);
        String groupRetry = loadbalancer.getUserGroup(userId);

        //then
        Assert.assertEquals(group, groupRetry);
    }

    private void initializeGroupsConfigurationMap() {
        groupsConfigurationRanges = new LinkedHashMap<>();
        groupsConfigurationRanges.put("groupA", new Range(Integer.MIN_VALUE, -1288490189));
        groupsConfigurationRanges.put("groupB", new Range(-1288490188, -1));
        groupsConfigurationRanges.put("groupC", new Range(0, Integer.MAX_VALUE));
    }

}