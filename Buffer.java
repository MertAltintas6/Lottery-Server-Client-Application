import java.util.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.*;
public class Buffer<E>{
   private int max;
   private int size = 0;
   private ArrayList<E> buffer;
   private Semaphore empty;  // control consumer
   private Semaphore full;   // control producer
   private Lock lock = new ReentrantLock();
   public Buffer(int s){
   	 buffer = new ArrayList<E>();
   	 max = s;
   	 empty = new Semaphore(0);
   	 full = new Semaphore(max);
   }

   public void put(E x){
   	 try{
   		full.acquire();
   	 }
   	 catch(InterruptedException e){}
   	 // synchronize update of buffer
   	 lock.lock();
   	 try{
   	 	buffer.add(x);
   	    size++;
   	    empty.release();
   	 }finally{
   	 	lock.unlock();
   	 }
   }
   public E get(){
   		try{
   			empty.acquire();
   		}
   		catch(InterruptedException e){}
   		// synchronize update of buffer
        lock.lock();
        try{
        	E temp = buffer.get(0);
   	      buffer.remove(0);
   	      size--;
   	      full.release();
   	      return temp;
        }finally{
        	lock.unlock();
        }
   }
   public int getSize() {
    lock.lock();
    int bufSize = size;
    lock.unlock();
    return bufSize;
  }

}