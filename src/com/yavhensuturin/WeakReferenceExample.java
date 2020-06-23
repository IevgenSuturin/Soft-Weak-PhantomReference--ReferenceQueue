package com.yavhensuturin;

import java.lang.ref.WeakReference;
import java.util.Map;

class WeakRefClass{
    private String name;

    public WeakRefClass(String name) {
        this.name = name;
    }

    void printName(){
       System.out.println("Name: " + this.name);
    }
}

public class WeakReferenceExample {
    public static void main(String[] args) {
        WeakRefClass object1 = new WeakRefClass("Weak referenced Object");

        WeakReference<WeakRefClass> weakReference = new WeakReference<>(object1);

        object1 = null;
        try{
            Thread.sleep(100);
        } catch (InterruptedException e){}

        weakReference.get().printName();
    }
}

//    Weak references in Java
// May 26, 2019  Samyam_
//         This article discusses the concept of weak references in Java.
//         Before we start, let us get a few basics out of the way.
//         There are 4 types of references in Java:
//
//         Strong reference
//         Weak reference
//         Soft reference
//         Phantom reference
//         Here we will discuss about weak reference.
//
//         Weak reference is related to garbage collection in Java. Garbage collection is simply the automatic de-allocation of unreferenced object from memory.
//         A weak reference is a reference made that is not strong enough to make the object to remain in memory. So, weak references can let the garbage collector decide an object’s reachability and whether the object in question should be kept in memory or not.
//         Weak references need to be declared explicitly as by default Java marks a reference as a strong reference.
//        What is a weak reachability?
//        It means that an object has neither strong nor soft references pointing to it and can only be reached by traversing through a weak reference.
//        So if the object is weakly referenced then the garbage collector removes it from memory which clears up more space and make for better memory management.
//        After the garbage collector has removed the weak reference, the reference is placed in a reference queue and the formerly weak-reachable objects are finalized.
//
//        Where are weak references used?
//        Weak references are used mostly in the implementation of canonicalized mappings. Canonicalized mapping is when the maps hold only one instance of a particular value.
//
//        Weak references are also widely used in WeakHashMap class. This is the implementation of the Map interface where every key value is stored as a weak reference. Key-value pairs extend WeakReference class. So removal of this key by the garbage collector results in the entity being removed as well.
//        Code example:
//
//        Private static class TryingOut<K,V> extends WeakReference<Object> implements Map.Entry <K,V>
//        Lapsed Listener problem also uses weak references. Memory leak problems are handled by weak references in this case.
//
//
//        Implementing weak references:
//        java.lang.ref.WeakReference class is used while dealing and creating weak references.
//        A real live practical scenario where weak references can be used is when establishing a database connection which might be cleaned up by Garbage Collector when the application using the database gets closed.
//        A coding example of weak references in Java is shown below: