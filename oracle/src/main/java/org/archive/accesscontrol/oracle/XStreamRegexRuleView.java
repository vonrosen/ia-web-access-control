package org.archive.accesscontrol.oracle;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.archive.accesscontrol.model.RegexReplacement;
import org.archive.accesscontrol.model.RegexRule;
import org.archive.accesscontrol.model.RegexRuleSet;
import org.archive.accesscontrol.model.RuleChange;
import org.hibernate.collection.PersistentBag;
import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentMap;
import org.hibernate.collection.PersistentSet;
import org.hibernate.collection.PersistentSortedMap;
import org.hibernate.collection.PersistentSortedSet;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class XStreamRegexRuleView extends XStreamView {

    public XStreamRegexRuleView(String format) {
        super(format);
    }

    protected void configureXStream() {

        xstream.alias("ruleSet", RegexRuleSet.class);
        xstream.alias("rule", RegexRule.class);
        xstream.alias("regexReplacements", PersistentSortedSet.class);
        xstream.alias("regexReplacement", RegexReplacement.class);
        xstream.alias("error", SimpleError.class);
        xstream.alias("ruleChange", RuleChange.class);
        xstream.omitField(RegexReplacement.class, "rules");
        xstream.omitField(Object.class, "initialized");
        xstream.omitField(Object.class, "owner");
        xstream.omitField(Object.class, "cachedSize");
        xstream.omitField(Object.class, "role");
        xstream.omitField(Object.class, "dirty");
        xstream.omitField(Object.class, "storedSnapshot");
        
        xstream.registerConverter(new Converter() {

            public boolean canConvert(Class clazz) {
                return clazz.equals(PersistentBag.class);
            }

            public void marshal(Object value, HierarchicalStreamWriter writer,
                    MarshallingContext context) {

                PersistentBag bag = (PersistentBag) value;

                for (int i = 0; i < bag.size(); ++i) {
                    if (bag.get(i) instanceof RegexReplacement) {
                        RegexReplacement regexReplacment = (RegexReplacement)bag.get(i);
                        writer.startNode("regexReplacement");
                        writer.startNode("regex");
                        writer.setValue(regexReplacment.getRegex());
                        writer.endNode();
                        writer.startNode("replacement");
                        writer.setValue(regexReplacment.getReplacement());
                        writer.endNode();
                        writer.startNode("id");
                        writer.setValue(String.valueOf(regexReplacment.getId()));
                        writer.endNode();
                        writer.endNode();
                    }
                }
            }

            public Object unmarshal(HierarchicalStreamReader reader,
                    UnmarshallingContext context) {

                return null;
            }
        }

        );
        
        xstream.addDefaultImplementation(PersistentList.class, List.class); 
        xstream.addDefaultImplementation(PersistentBag.class, List.class); 
        xstream.addDefaultImplementation(PersistentSet.class, Set.class); 
        xstream.addDefaultImplementation(PersistentSortedSet.class, SortedSet.class); 
        xstream.addDefaultImplementation(PersistentMap.class, Map.class); 
        xstream.addDefaultImplementation(PersistentSortedMap.class, SortedMap.class);
   }
    
}
