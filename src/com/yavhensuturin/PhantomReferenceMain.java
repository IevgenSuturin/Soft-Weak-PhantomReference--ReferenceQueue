package com.yavhensuturin;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.function.DoubleToIntFunction;

class MyObject{
    private int[] ints =new int[1000];
    private String name;

    public MyObject(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.printf("%s is finalizing.\n", name);
    }
}


public class PhantomReferenceMain {
    public static void main(String[] args) {
        ReferenceQueue<MyObject> referenceQueue = new ReferenceQueue<>();

        MyObject myObject1 = new MyObject("phantom");
        Reference<MyObject> reference = new PhantomReference<>(myObject1, referenceQueue);
        System.out.println("ref#get(): "+ reference.get());
        System.out.println("ref#: "+ reference);
        MyObject myObject2 = new MyObject("normal");

        //make objects unreachable
        myObject2 = null;
        myObject1 = null;

        System.out.println("refq.poll: "+referenceQueue.poll());

        System.out.println("-- do some memory intensive work --");
        for(int i=0; i< 1000; i++){
            int[] ints = new int[1000];
            try{
                Thread.sleep(1);
            }catch (InterruptedException e){}
        }

        System.gc();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){}

        if(checkObjectGet(reference, referenceQueue)){
            takeAction();
        }
    }

    private static boolean checkObjectGet(Reference<MyObject> ref, ReferenceQueue<MyObject> referenceQueue){
        boolean result = false;
        System.out.println("-- Checking whether object garbage collection due --");
        Reference<? extends MyObject> polledRef = referenceQueue.poll();
        System.out.println("polledRef: "+polledRef);
        result=(polledRef==ref);
        System.out.println("Is polledRef same: "+result);
        if(polledRef!=null){
            System.out.println("Ref#get: "+polledRef.get());
        }

        return result;
    }

    private static void takeAction(){
        System.out.println("pre-mortem cleanup actions");
    }
}
