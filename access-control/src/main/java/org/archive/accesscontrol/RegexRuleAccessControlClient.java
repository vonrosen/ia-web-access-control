package org.archive.accesscontrol;

import java.util.Date;

import org.archive.accesscontrol.model.RegexRuleSet;
import org.archive.accesscontrol.model.Rule;
import org.archive.accesscontrol.robotstxt.CachingRobotClient;
import org.archive.accesscontrol.robotstxt.RobotClient;
import org.archive.net.PublicSuffixes;
import org.archive.util.ArchiveUtils;
import org.archive.util.SURT;

public class RegexRuleAccessControlClient extends AccessControlClient {
    
    public RegexRuleAccessControlClient(RuleDao ruleDao, RobotClient robotClient) {
        super(ruleDao, robotClient);
    }
    
    public RegexRuleAccessControlClient(String oracleUrl) {
        this(new CachingRegexRuleDao(oracleUrl), new CachingRobotClient());
    }
    
    public RegexRuleAccessControlClient(String oracleUrl, boolean cacheOnce) {
        this(cacheOnce ? new CacheOnceHttpRegexRulesDao(oracleUrl) : new CachingRegexRuleDao(oracleUrl), new CachingRobotClient());
    }

    /**
     * Return the most specific matching rule for the requested document.
     * 
     * @param url
     *            URL of the requested document.
     * @param captureDate
     *            Date the document was archived.
     * @param retrievalDate
     *            Date of retrieval (usually now).
     * @param who
     *            Group name of the user accessing the document.
     * @return
     * @throws RuleOracleUnavailableException 
     */
    public Rule getRule(String url, Date captureDate, Date retrievalDate,
            String who) throws RuleOracleUnavailableException {
        url = ArchiveUtils.addImpliedHttpIfNecessary(url);
        String surt = SURT.fromURI(url);
//        PublicSuffixes.reduceSurtToAssignmentLevel(surt)
        String publicSuffix = PublicSuffixes
                .reduceSurtToAssignmentLevel(getSurtAuthority(surt));

        RegexRuleSet rules =  (RegexRuleSet)ruleDao.getRuleTree(getScheme(surt) + "(" + publicSuffix);

        Rule matchingRule = rules.getMatchingRule(surt, captureDate,
                retrievalDate, who);
        return matchingRule;
    }
    
}
