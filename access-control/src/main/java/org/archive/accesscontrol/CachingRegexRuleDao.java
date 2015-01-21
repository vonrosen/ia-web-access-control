package org.archive.accesscontrol;

import org.archive.accesscontrol.model.RegexRuleSet;
import org.archive.accesscontrol.model.Rule;
import org.archive.accesscontrol.model.RuleSet;

public class CachingRegexRuleDao extends CachingRuleDao {

    public CachingRegexRuleDao(RuleDao ruleDao) {
        super(ruleDao);
    }

    public CachingRegexRuleDao(String oracleUrl) {
        this(new HttpRegexRuleDao(oracleUrl));
    }

    public RegexRuleSet getRuleTree(String surt) throws RuleOracleUnavailableException {
        
        RuleSet ruleSet = super.getRuleTree(surt);
        RegexRuleSet regexRuleSet = new RegexRuleSet();
        
        for (Rule rule : ruleSet) {
            regexRuleSet.add(rule);
        }

        return regexRuleSet;
    }    

}
