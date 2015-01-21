package org.archive.accesscontrol;


public class CachingRegexRuleDao extends CachingRuleDao {

    public CachingRegexRuleDao(RuleDao ruleDao) {
        super(ruleDao);
    }

    public CachingRegexRuleDao(String oracleUrl) {
        this(new HttpRegexRuleDao(oracleUrl));
    }
}
