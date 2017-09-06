package loadbalancer.logic;

import loadbalancer.configuration.GroupsConfiguration;
import loadbalancer.model.Range;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
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

    @Test
    public void shouldAssignMultipleUsersToAppropriateGroups() throws Exception {
        //given
        String userId = "ObRQPPlnRX"; //Hashcode -1259122886
        String userId2 = "ybSC7uVlj9"; //Hashcode 597690076
        String userId3 = "IKrnAhDiwR"; //Hashcode -1963571387
        String userId4 = "DmLVh4T3w9"; //Hashcode 1431457216
        String userId5 = "VBKIJtBusN"; //Hashcode 1404613602
        String userId6 = "gTp1M7BApE"; //Hashcode 153165740
        String userId7 = "5ddDc8z0jl"; //Hashcode -884331364
        String userId8 = "IXsn0mKzhA"; //Hashcode 1570281135
        String userId9 = "ac2mDOhTEd"; //Hashcode 786439699
        String userId10 = "oy8XQN4PVd"; //Hashcode 1999847697
        when(groupsConfiguration.getGroupsRanges()).thenReturn(groupsConfigurationRanges);

        //when
        loadbalancer.getUserGroup(userId);
        loadbalancer.getUserGroup(userId2);
        loadbalancer.getUserGroup(userId3);
        loadbalancer.getUserGroup(userId4);
        loadbalancer.getUserGroup(userId5);
        loadbalancer.getUserGroup(userId6);
        loadbalancer.getUserGroup(userId7);
        loadbalancer.getUserGroup(userId8);
        loadbalancer.getUserGroup(userId9);
        loadbalancer.getUserGroup(userId10);

        int groupACount = Collections.frequency(loadbalancer.getUserGroups().values(), "groupA");
        int groupBCount = Collections.frequency(loadbalancer.getUserGroups().values(), "groupB");
        int groupCCount = Collections.frequency(loadbalancer.getUserGroups().values(), "groupC");

        //then
        Assert.assertEquals(1, groupACount);
        Assert.assertEquals(2, groupBCount);
        Assert.assertEquals(7, groupCCount);
    }

    private void initializeGroupsConfigurationMap() {
        groupsConfigurationRanges = new LinkedHashMap<>();
        groupsConfigurationRanges.put("groupA", new Range(Integer.MIN_VALUE, -1288490189));
        groupsConfigurationRanges.put("groupB", new Range(-1288490188, -1));
        groupsConfigurationRanges.put("groupC", new Range(0, Integer.MAX_VALUE));
    }

}