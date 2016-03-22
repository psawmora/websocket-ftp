package psaw.websocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p></p>
 *
 * @author prabath.
 */
public class TestList {

    public static void main(String[] args) {
        int[] intTestArr = {1, 2, 3, 4, 5};
        List<int[]> lists = TestA.testCreation(intTestArr);
        System.out.println(lists);
        System.out.println(intTestArr.getClass());
    }

    private static class TestA {

        public static <T> List<T> testCreation(T... t) {
            List<T> list = new ArrayList<>();
            Collections.addAll(list, t);
            return list;
        }
    }
}
