package org.archive.accesscontrol.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

public class RegexRuleSet extends RuleSet {

    protected HashMap<String, TreeSet<RegexRule>> rulemap = new HashMap<String, TreeSet<RegexRule>>();
    
    class RegexRuleSetIterator implements Iterator<RegexRule> {
        private Iterator<TreeSet<RegexRule>> mapIterator;
        private Iterator<RegexRule> setIterator;

        public RegexRuleSetIterator() {
            mapIterator = rulemap.values().iterator();
            setIterator = null;
            hasNext();
        }

        public boolean hasNext() {
            while (true) {
                if (setIterator != null && setIterator.hasNext())
                    return true;
                if (!mapIterator.hasNext())
                    return false;
                setIterator = mapIterator.next().iterator();
            }
        }

        public RegexRule next() {
            if (hasNext()) {
                return setIterator.next();
            }
            return null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }    
    
    public RegexRule getMatchingRule(String surt, Date captureDate,
            Date retrievalDate, String who) {
        
        return (RegexRule)super.getMatchingRule(surt, captureDate, retrievalDate, who);
    }
    
}
