 package com.yavhensuturin;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;



//Reference Queues
//        In the previous posts we had a look at weak and soft references. The java.lang.ref package also includes a class
//        ReferenceQueue. As per the class documentation:
//
//         Reference queues, to which registered reference objects are appended by the garbage
//         collector after the appropriate reachability changes are detected.
//
//         What does a reachability change mean ?
//         We know that the Garbage collector will free up any weak reference objects or weakly reachable objects as a part of the garbage collection process.
//         The flow for the same is:
//
//         If the garbage collector discovers an object that is weakly reachable, the following occurs:
//         1. The WeakReference object's referent field is set to null, thereby making it not refer to the heap object any longer.
//         2. The heap object that had been referenced by the WeakReference is declared finalizable.
//         3. The WeakReference object is added to its ReferenceQueue. Then the heap object's finalize() method is run and its memory freed.
//
//         So what is this ReferenceQueue ?
//         When creating non-strong reference objects we have the option of passing a reference queue as a part of the Reference constructor. As seen from the above explanation,
//         this reference queue is a way for the GC to inform the program that a certain object is no longer reachable. Consider the below example:

class SavePoint{
    String name;

    public SavePoint(String name) {
        this.name = name;
    }
}


public class ReferenceQueueExample {
    public static void main(String[] args) throws InterruptedException {
        SavePoint savePoint = new SavePoint("SavePoint");

        ReferenceQueue<SavePoint> savePointQ = new ReferenceQueue<>();
        WeakReference<SavePoint> savePointWeakReference = new WeakReference<>(savePoint, savePointQ);

        System.out.println("SavePoint created as a weak ref "+savePointWeakReference);
        Runtime.getRuntime().gc();
        System.out.println("Any weak reference in Queue? " + (savePointQ.poll() != null));
        //the string reference has been removed. The object is now only weakly reachable
        savePoint = null;

        System.out.println("Now call gc ...");
        Runtime.getRuntime().gc();
        System.out.println("Does the weak reference still hold the heap object? " + (savePointWeakReference.get() != null));
        System.out.println("Is the weak reference added th the referenceQ ? " + (savePointWeakReference.isEnqueued()));
        Reference<? extends SavePoint> reCreatedSavePoint = savePointQ.remove();
        System.out.println("Any weak reference in Queue? " + (reCreatedSavePoint != null));
        System.out.println("Is same as original weak reference? " + (reCreatedSavePoint == savePointWeakReference));
        System.out.println(" and heap object is " + reCreatedSavePoint.get() );
    }
}


//    The program :
//         Creates a strong reference and adds it to a Weak reference savePointWRefernce. The object in memory is now referenced by a strong reference and a weak reference - hence strongly reachable.
//         The first call to garbage collector will not clear our savepoint object as it is a strong reference. Hence the poll method of the referenceQ will return null. (poll method is non - blocking it checks and returns immediately.)
//         The savePoint reference variable is set to null. Our heap object is now referenced only by the weak reference - hence it is weakly reachable.
//         The second gc call will now locate the object, executes its finalize method and mark this object to be freed. The object is also added to the ReferenceQ.
//         A call to the remove method of the ReferenceQ will return the object. remove is a blocking method. it will wait till an object has been made available in the Queue. (poll method might not work as the recycling process is happening on a separate thread.)

//    As seen from above the Reference queue actually holds within it the WeakReference which lost its heap object to clean up.
//    The WeakReference does not have any association to the memory object. The get call above returns null.
//    Unlike with finalize when we can make the object alive again, with the ReferenceQ there is no way to reach the released java object.
//    Reference Queues are just for References that got freed by garbage collection. They cannot be used to make alive our objects again.
//    They can only be used to notify our code about the loss of memory objects referred to by these non- strong references.