package loadbalancer.rest;

import loadbalancer.logic.LoadbalancerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Pawel Mielniczuk on 2017-08-30.
 */
@RestController
public class UserGroupController {

    @Autowired
    private LoadbalancerImpl loadbalancerImpl;

    @GetMapping(value = "/route")
    public String getUserGroup(@RequestParam(value = "id") String userId) {
        return loadbalancerImpl.getUserGroup(userId);
    }

}
