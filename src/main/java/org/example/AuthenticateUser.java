package org.example;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class AuthenticateUser {
    public static void main(String[] args) {

        Context ctx = null;
        Hashtable env = new Hashtable();
        boolean isValid = false;
        String userName = "admin";

        try {
            //set the name of domain with the user name
            String fullName = userName + "@" + "krutik.local";
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL, "ldap://IP:389");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, fullName);
            env.put(Context.SECURITY_CREDENTIALS, "password");
            //validate user
            ctx = new InitialDirContext(env);
            isValid = true;
            System.out.println("IS GOOD isValid is: " + isValid);

        } catch (AuthenticationException ex) {
            System.out.println("AuthenticationException is: " + ex);
            isValid = false;
        } catch (NamingException ex) {
            System.out.println("NamingException is: " + ex);
        }

    }
}
