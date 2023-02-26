package org.example;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.text.MessageFormat;
import java.util.*;

public class ADConnect {
    private static final String MEMBER_OF = "memberOf";

    public static List<String> authenticate(String user,
                                            String securityToken,
                                            String domain) throws NamingException {

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "LDAP://" + domain);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, user + "@" + domain);
        env.put(Context.SECURITY_CREDENTIALS, securityToken);

        InitialDirContext ctx = new InitialDirContext(env);

        String[] dcParts = domain.split("\\.");
        String domainSearch = "";
        for (String dcPart : dcParts) {
            domainSearch += "DC=" + dcPart + ",";
        }
        domainSearch = domainSearch.substring(0, domainSearch.length() - 1);

        // Create the search controls
        SearchControls searchCtls = new SearchControls();
        String[] attributes = new String[]{MEMBER_OF};
        searchCtls.setReturningAttributes(attributes);

        // Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        // Search for objects using the filter
        NamingEnumeration result = ctx.search(domainSearch,
                MessageFormat.format("(SAMAccountName={0})", user), searchCtls);

        //Get the first result
        SearchResult sr = (SearchResult) result.next();

        Attribute memberOf = sr.getAttributes().get(MEMBER_OF);
        List<String> memberOfGroups = new ArrayList<>();
        if (memberOf != null) {
            for (Enumeration e1 = memberOf.getAll(); e1.hasMoreElements(); ) {
                memberOfGroups.add(e1.nextElement().toString());
            }
        }
        return memberOfGroups;
    }

}