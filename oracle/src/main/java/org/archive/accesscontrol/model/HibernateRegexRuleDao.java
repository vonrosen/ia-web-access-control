package org.archive.accesscontrol.model;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.archive.accesscontrol.RuleOracleUnavailableException;
import org.archive.surt.NewSurtTokenizer;
import org.archive.util.ArchiveUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

@SuppressWarnings("unchecked")
public class HibernateRegexRuleDao extends HibernateRuleDao {

    public RegexRule getRule(Long id) {
        return (RegexRule) getHibernateTemplate().get(RegexRule.class, id);
    }

    public List<Rule> getAllRules() {
        return  getHibernateTemplate().find("from RegexRule");
    }

    public List<Rule> getRulesWithSurtPrefix(String prefix) {
        // escape wildcard characters % and _ using ! as the escape character.
        prefix = prefix.replace("!", "!!").replace("%", "!%")
                .replace("_", "!_");
        return getHibernateTemplate().find(
                "from RegexRule rule where rule.surt like ? escape '!'",
                prefix + "%");
    }

    public List<Rule> getRulesWithExactSurt(String surt) {
        return getHibernateTemplate().find(
                "from RegexRule rule where rule.surt = ?", surt);
    }
    
    public List<Rule> getRulesModifiedAfter(String timestamp, String who, String customRestrict) throws ParseException {
        
        Date date = (timestamp != null ? ArchiveUtils.getDate(timestamp) : null);
        
        String ruleWhereQuery = "from RegexRule rule where ";
        
        if (customRestrict != null) {
            ruleWhereQuery += customRestrict;
        }
        
        if (who == null && date != null) {
            return getHibernateTemplate().find(ruleWhereQuery + " rule.lastModified >= ?", date);
        } else if (who != null && date == null) {
            return getHibernateTemplate().find(ruleWhereQuery + " rule.who = ? or rule.who = \'\'", who);           
        }
        
        Object[] params = {date, who};
        return getHibernateTemplate().find(ruleWhereQuery + " rule.lastModified >= ? and (rule.who = ? or rule.who = \'\')", params);
    }
    
    /**
     * Returns the "rule tree" for a given SURT. This is a sorted set of all
     * rules equal or lower in specificity than the given SURT plus all rules on
     * the path from this SURT to the root SURT "(".
     * 
     * The intention is to call this function with a domain or public suffix,
     * then queries within that domain can be made very fast by searching the
     * resulting list.
     * 
     * @param surt
     * @return
     */
    public RegexRuleSet getRuleTree(String surt) {
        RuleSet rules = super.getRuleTree(surt);
        
        RegexRuleSet regexRuleSet = new RegexRuleSet();
        
        for (Rule rule : rules) {
            regexRuleSet.add((RegexRule)rule);
        }
        
        return regexRuleSet;
    }
    
    public RegexRuleSet getAllRulesSet()
    {
        RegexRuleSet rules = new RegexRuleSet();
        rules.addAll(getHibernateTemplate().find("from RegexRule"));
        return rules;
    }

    public void deleteRule(Long id) {
        Object record = getHibernateTemplate().load(RegexRule.class, id);
        getHibernateTemplate().delete(record);
    }
    
    public void deleteAllRules() {
        getHibernateTemplate().bulkUpdate("delete from RegexRule");
    }
    
}