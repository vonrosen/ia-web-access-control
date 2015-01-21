package org.archive.accesscontrol;

import java.io.IOException;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.archive.accesscontrol.model.RegexReplacement;
import org.archive.accesscontrol.model.RegexRule;
import org.archive.accesscontrol.model.RegexRuleSet;
import org.archive.accesscontrol.model.RuleChange;

public class HttpRegexRuleDao extends HttpRuleDao {

    public HttpRegexRuleDao(String oracleUrl) {
        super(oracleUrl);

        xstream.alias("ruleSet", RegexRuleSet.class);
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
    
    /**
     * @throws RuleOracleUnavailableException 
     * @see RuleDao#getRuleTree(String)
     */
    public RegexRuleSet getRuleTree(String surt) throws RuleOracleUnavailableException {
        HttpMethod method = new GetMethod(oracleUrl + "/rules/tree/" + surt);
        RegexRuleSet rules;

        try {
            http.executeMethod(method);
//            String response = method.getResponseBodyAsString();
//            System.out.println(response);
            rules = (RegexRuleSet) xstream.fromXML(method.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new RuleOracleUnavailableException(surt, e);
        } finally {
            method.releaseConnection();
        }
        return rules;
    }    
}
