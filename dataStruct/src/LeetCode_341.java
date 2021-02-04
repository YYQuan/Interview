package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class LeetCode_341 {

    public static void main(String[] args) {
        LeetCode_341 code = new LeetCode_341();

//        List<Integer> result =code.postorderTraversal(node1);
//        System.out.println(Arrays.toString(result.toArray()));

    }


    // 题目有点难理解
    // 先略过
//    public class NestedIterator implements Iterator<Integer> {
//
//        public NestedIterator(List<NestedInteger> nestedList) {
//
//        }
//
//        @Override
//        public Integer next() {
//
//        }
//
//        @Override
//        public boolean hasNext() {
//
//        }
//    }


    public interface NestedInteger {

              // @return true if this NestedInteger holds a single integer, rather than a nested list.
              public boolean isInteger();

              // @return the single integer that this NestedInteger holds, if it holds a single integer
              // Return null if this NestedInteger holds a nested list
              public Integer getInteger();

              // @return the nested list that this NestedInteger holds, if it holds a nested list
              // Return null if this NestedInteger holds a single integer
              public List<NestedInteger> getList();
  }

}
