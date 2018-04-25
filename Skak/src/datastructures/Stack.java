package datastructures;

import java.util.Collection;

public class Stack<T> {

        private Element top = null;

        public void push(T value)
        {
            Element e = new Element();
            e.value = value;
            e.next = top;
            top = e;
        }

        public void pushAll(Collection<T> collection)
        {
            Element e = null;
            for(T value : collection)
            {
                e = new Element();
                e.value = value;
                e.next = top;
                top = e;
            }
        }

        public T pop()
        {
            if(top == null)
                return null;
            T value = top.value;
            top = top.next;
            return value;

        }

        public boolean isEmpty()
        {
            return top == null;
        }

        private class Element
        {
            T value = null;
            Element next = null;
        }

}
