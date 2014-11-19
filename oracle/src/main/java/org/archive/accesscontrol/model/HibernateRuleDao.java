package org.archive.accesscontrol.model;

import org.archive.accesscontrol.RuleDao;

/**
 * The rule data access object provides convenience methods for using Hibernate
 * to access stored rules.  The database connection is expected to be configured 
 * using the SpringFramework ORM layer.
 * 
 * @author aosborne
 */

public class HibernateRuleDao extends AbstractHibernateRuleDao<Rule> implements RuleDao {

    protected Class<Rule> getEntityClass() {
        return Rule.class;
    }
    
}
