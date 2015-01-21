package org.archive.accesscontrol.model;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.archive.util.ArchiveUtils;

@SuppressWarnings("unchecked")
public class HibernateRegexRuleDao extends HibernateRuleDao {

    @Override
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
    
    public void deleteRule(Long id) {
        Object record = getHibernateTemplate().load(RegexRule.class, id);
        getHibernateTemplate().delete(record);
    }
    
    public void deleteAllRules() {
        getHibernateTemplate().bulkUpdate("delete from RegexRule");
    }
    
}