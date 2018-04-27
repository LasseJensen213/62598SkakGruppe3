package datastructures;

import java.util.Collection;

public class Stack<T> {

        private Element top = null;
        private int size = 0;

        public void push(T value)
        {
            if(value == null)
                return;
            Element e = new Element();
            e.value = value;
            e.next = top;
            top = e;
            size++;
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
            size += collection.size();
        }

        public T pop()
        {
            if(top == null)
                return null;
            T value = top.value;
            top = top.next;
            size--;
            return value;

        }

        public int size()
        {
            return size;
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
