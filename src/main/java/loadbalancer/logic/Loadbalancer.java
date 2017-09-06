package loadbalancer.logic;

import java.util.Map;

/**
 * Created by Pawel Mielniczuk on 2017-09-04.
 */
public interface Loadbalancer {

    String getUserGroup(String userId);

    Map<String, String> getUserGroups();
}
