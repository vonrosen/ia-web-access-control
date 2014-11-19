package org.archive.accesscontrol.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import org.archive.surt.NewSurtTokenizer;

/**
 * A set of acess control rules which can be queried to find the governing rule
 * for a particular request.
 * 
 * @author aosborne
 * 
 */
public class RuleSet<T extends Rule> implements Iterable<T> {
    protected HashMap<String, TreeSet<T>> rulemap = new HashMap<String, TreeSet<T>>();

    class RuleSetIterator implements Iterator<T> {
        private Iterator<TreeSet<T>> mapIterator;
        private Iterator<T> setIterator;

        public RuleSetIterator() {
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

        public T next() {
            if (hasNext()) {
                return setIterator.next();
            }
            return null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public RuleSet() {
        super();
    }

    /**
     * Return the most specific matching rule for the given request.
     * 
     * @param surt
     * @param captureDate
     * @param retrievalDate
     * @param who
     *            group
     * @return
     */
    public T getMatchingRule(String surt, Date captureDate,
            Date retrievalDate, String who) {

        NewSurtTokenizer tok = new NewSurtTokenizer(surt);
        
        // Best general rule (when accessGroup is blank)
        T ruleGeneral = null;

        for (String key: tok.getSearchList()) {
            Iterable<T> rules = rulemap.get(key); 
            if (rules != null) {
                for (T rule : rules) {
                    if (rule.matches(surt, captureDate, retrievalDate, who)) {
                    	// Return this if accessGroup (who) matches exactly
                    	if ((who != null) && who.equals(rule.getWho())) {
                    		return rule;
                    	// otherwise, store the first/best one
                    	} else if (ruleGeneral == null) {
                    		ruleGeneral = rule;
                    	}
                    }
                }
            }
        }
        
        return ruleGeneral;
    }

    public void addAll(Iterable<T> rules) {
        for (T rule : rules) {
            add(rule);
        }
    }

    public void add(T rule) {
        String surt = rule.getSurt();
        TreeSet<T> set = rulemap.get(surt);
        if (set == null) {
            set = new TreeSet<T>();
            rulemap.put(surt, set);
        }
        set.add(rule);
    }

    public Iterator<T> iterator() {
        return new RuleSetIterator();
    }

}
