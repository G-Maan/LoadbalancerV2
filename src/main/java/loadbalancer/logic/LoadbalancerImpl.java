package loadbalancer.logic;

import loadbalancer.configuration.GroupsConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Pawel Mielniczuk on 2017-08-30.
 */
@Component
public class LoadbalancerImpl implements Loadbalancer{

    private static final Logger logger = Logger.getLogger(LoadbalancerImpl.class);

    private Map<String, String> usersGroups = new ConcurrentHashMap<>();

    private GroupsConfiguration groupsConfiguration;

    @Autowired
    public LoadbalancerImpl(GroupsConfiguration groupsConfiguration) {
        this.groupsConfiguration = groupsConfiguration;
    }

    @Override
    public String getUserGroup(String userId) {
        return calculateGroup(userId);
    }

    private int hashUserId(String userId) {
        return userId.hashCode();
    }

    private String calculateGroup(String userId) {
        String userGroup = usersGroups.get(userId);
        if (userGroup == null) {
            userGroup = assignUserGroup(userId);
        }
        return userGroup;
    }

    private String assignUserGroup(String userId) {
        int userIdHashcode = hashUserId(userId);
        String userGroup = groupsConfiguration
                .getGroupsRanges()
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().includes(userIdHashcode))
                .findFirst()
                .get()
                .getKey();
        usersGroups.put(userId, userGroup);
        logger.info("New user group assigned: " + userGroup);
        return userGroup;
    }

    @Override
    public Map<String, String> getUserGroups() {
        return new ConcurrentHashMap<>(usersGroups);
    }
}
