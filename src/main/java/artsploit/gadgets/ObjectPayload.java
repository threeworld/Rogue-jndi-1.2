package artsploit.gadgets;

import artsploit.RogueJndi;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Set;

/**
 * @className: ObjectPayload
 * @description: TODO 类描述
 * @author: two_day
 * @date: 2022/5/11
 **/

@SuppressWarnings ( "rawtypes" )
public interface ObjectPayload <T> {

    public T getObject(String command) throws Exception;

    public static class Utils {

        // get payload classes by classpath scanning
        public static Set<Class<? extends ObjectPayload>> getPayloadClasses () {
            final Reflections reflections = new Reflections(ObjectPayload.class.getPackage().getName());
            final Set<Class<? extends ObjectPayload>> payloadTypes = reflections.getSubTypesOf(ObjectPayload.class);
            for (Iterator<Class<? extends ObjectPayload>> iterator = payloadTypes.iterator(); iterator.hasNext(); ) {
                Class<? extends ObjectPayload> pc = iterator.next();
                if ( pc.isInterface() || Modifier.isAbstract(pc.getModifiers()) ) {
                    iterator.remove();
                }
            }
            return payloadTypes;
        }

        @SuppressWarnings( "unchecked" )
        public static Class<? extends ObjectPayload> getPayloadClass(final String className){
            Class<? extends ObjectPayload> clazz = null;

            try {
                clazz = (Class<? extends ObjectPayload>) Class.forName(className);
            } catch (Exception ignored){}

            if (clazz == null){
                try {
                    clazz = (Class<? extends ObjectPayload>) Class.forName(RogueJndi.class.getPackage().getName() + ".gadgets." + className);
                }catch (Exception ignored){}
            }
            if (clazz != null && !ObjectPayload.class.isAssignableFrom(clazz)){
                clazz = null;
            }
            return clazz;
        }
    }



}
