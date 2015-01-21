package org.archive.accesscontrol;

import org.archive.accesscontrol.model.RegexReplacement;
import org.archive.accesscontrol.model.RegexRule;
import org.archive.accesscontrol.model.RuleChange;
import org.archive.accesscontrol.model.RuleSet;

public class HttpRegexRuleDao extends HttpRuleDao {

    public HttpRegexRuleDao(String oracleUrl) {
        super(oracleUrl);

        xstream.alias("ruleSet", RuleSet.class);
        xstream.alias("rule", RegexRule.class);
        xstream.alias("regexReplacement", RegexReplacement.class);
        xstream.alias("ruleChange", RuleChange.class);

        //ignore any fields that have been dynamically added by Hibernate
        xstream.omitField(RegexReplacement.class, "rules");
        xstream.omitField(RegexRule.class, "initialized");
        xstream.omitField(RegexRule.class, "owner");
        xstream.omitField(RegexRule.class, "cachedSize");
        xstream.omitField(RegexRule.class, "role");
        xstream.omitField(RegexRule.class, "dirty");
        xstream.omitField(RegexRule.class, "storedSnapshot");
    }
}
