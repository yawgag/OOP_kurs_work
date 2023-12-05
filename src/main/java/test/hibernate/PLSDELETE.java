package test.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PLSDELETE {
    private static final Logger logger = LoggerFactory.getLogger(PLSDELETE.class);
    void print_thread_1(){
        System.out.println("thread  1");
    }
    void print_thread_2(){
        System.out.println("thread  2");
    }
    void print_thread_3(){
        System.out.println("thread  3");
    }
}
