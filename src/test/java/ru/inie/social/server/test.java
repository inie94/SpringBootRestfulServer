package ru.inie.social.server;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class test {
    @Test
    public void testMethod() {

        List<String> list1= new LinkedList<>(Arrays.asList("FIRST", "SECOND", "THIRD"));
        List<String> list2= new LinkedList<>(Arrays.asList("SECOND", "FIVES"));

//        list1.removeAll(list2);
        list1.remove("FIVES");
        System.out.println(list1);
    }
}
