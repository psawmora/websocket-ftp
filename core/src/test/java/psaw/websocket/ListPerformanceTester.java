package psaw.websocket;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author prabath.
 */
public class ListPerformanceTester {

    private static final long REPETITIONS = 10;

    public static void main(String[] args) {
        ListPerformanceTester performanceTester = new ListPerformanceTester();

        int time = performanceTester.insertToArrayList(1000000);
        System.out.println("Average insert time for array lists : " + (time));

        time = performanceTester.insertToLinkList(1000000);
        System.out.println("Average insert time for linked lists : " + (time));

        time = performanceTester.deleteFromArrayList(100000);
        System.out.println("Average Deletion time for array lists : " + (time));

        time = performanceTester.deleteFromLinkedList(1000000);
        System.out.println("Average Deletion time for Linked lists : " + (time));


    }

    private int deleteFromArrayList(int nElements) {
        List<String> testList = new ArrayList<>();
        for (int i = 0; i < nElements; i++) {
            testList.add(String.valueOf(i));
        }
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < nElements; i++) {
            testList.remove(String.valueOf(i));
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Size : " + testList.size());
        return (int) ((endTime - startTime));
    }

    private int deleteFromLinkedList(int nElements) {
        List<String> testList = new LinkedList<>();
        for (int i = 0; i < nElements; i++) {
            testList.add(String.valueOf(i));
        }
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < nElements; i++) {
            testList.remove(String.valueOf(i));
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Size : " + testList.size());
        return (int) ((endTime - startTime));
    }

    private int insertToArrayList(long nElements) {
        long startTime = System.currentTimeMillis();
        List<String> testList = new ArrayList<>();
        for (long i = 0; i < nElements; i++) {
            testList.add(String.valueOf(i));
        }

        long endTime = System.currentTimeMillis();
        return (int) ((endTime - startTime));
    }

    private int insertToLinkList(long nElements) {
        long startTime = System.currentTimeMillis();
        List<String> testList = new LinkedList<>();
        for (long i = 0; i < nElements; i++) {
            testList.add(String.valueOf(i));
        }

        long endTime = System.currentTimeMillis();
        return (int) ((endTime - startTime));

    }
}
