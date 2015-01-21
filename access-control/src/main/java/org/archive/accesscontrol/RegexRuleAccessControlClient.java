package org.archive.accesscontrol;

import org.archive.accesscontrol.robotstxt.CachingRobotClient;
import org.archive.accesscontrol.robotstxt.RobotClient;

public class RegexRuleAccessControlClient extends AccessControlClient {
    
    public RegexRuleAccessControlClient(RuleDao ruleDao, RobotClient robotClient) {
        super(ruleDao, robotClient);
    }
    
    public RegexRuleAccessControlClient(String oracleUrl) {
        this(new CachingRegexRuleDao(oracleUrl), new CachingRobotClient());
    }
    
    public RegexRuleAccessControlClient(String oracleUrl, boolean cacheOnce) {
        this(cacheOnce ? new CacheOnceHttpRulesDao(oracleUrl)
                : new CachingRegexRuleDao(oracleUrl), new CachingRobotClient());
    }
    
}
