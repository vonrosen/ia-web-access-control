package org.archive.accesscontrol;

import java.util.Collection;

import org.archive.accesscontrol.model.Rule;
import org.archive.accesscontrol.model.RuleSet;
import org.archive.surt.NewSurtTokenizer;

public class CacheOnceHttpRulesDao<T extends Rule> extends HttpRuleDao {

	protected RuleSet<T> allRules = null;
	
	public CacheOnceHttpRulesDao(String oracleUrl) {
		super(oracleUrl);
	}

	@Override
	public RuleSet getRuleTree(String surt)
			throws RuleOracleUnavailableException {
		
        RuleSet<T> rules = new RuleSet<T>();
        
        // add the root SURT
        fillRulesWithExactSurt(rules, "(");
        
        boolean first = true;
        for (String search: new NewSurtTokenizer(surt).getSearchList()) {
            if (first) {
                first = false;
                fillRulesWithSurtPrefix(rules, search);
            } else {
                fillRulesWithExactSurt(rules, search);
            }
        }
        
        return rules;
	}

	private void fillRulesWithExactSurt(RuleSet<T> rules, String surt) {
		for (T rule : allRules) {
			String matchSurt = rule.getSurt();
			if (matchSurt == null) continue;
			if (matchSurt.equals(surt)) {
				rules.add(rule);
			}
		}
	}

	private void fillRulesWithSurtPrefix(RuleSet<T> rules, String surt) {
		for (T rule : allRules) {
			String matchSurt = rule.getSurt();
			if (matchSurt == null) continue;
			if (matchSurt.startsWith(surt)) {
				rules.add(rule);
			}
		}
	}

	@Override
	public void prepare(Collection<String> surts) {
		try {
			allRules = super.getRuleTree("all");
		} catch (RuleOracleUnavailableException e) {
			throw new RuntimeException("Failure to load rules");
		}
	}
}
