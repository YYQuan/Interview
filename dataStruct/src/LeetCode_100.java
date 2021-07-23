package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LeetCode_100 {

    public static void main(String[] args) {
        LeetCode_100 code = new LeetCode_100();
//        TreeNode node3 =new TreeNode(3);
//        TreeNode node20 =new TreeNode(20);
//        node3.left = new TreeNode(9);
//        node3.right = node20;
//        node20.left = new TreeNode(15);
//        node20.right = new TreeNode(7);

        TreeNode node6 = new TreeNode(6);
        TreeNode node5 = new TreeNode(5);
        TreeNode node4 = new TreeNode(4);
        TreeNode node3 = new TreeNode(3);
        TreeNode node2 = new TreeNode(2);
        TreeNode node1 = new TreeNode(1);

        node2.right = node3;
        node2.left = node1;
        node3.right =node4;
        node4.right =node5;
        node5.right =node6;

        List<Integer> list = new ArrayList<>();

//        code.orderTree(node2,list);


        TreeNode nodeT1 = new TreeNode(-685);
        TreeNode nodeT2 = new TreeNode(-685);

//
//        System.out.println(new Integer(1)==new Integer(1));
//        System.out.println(new Integer(1).intValue()==new Integer(1).intValue());
//        System.out.println(new Integer(-1)==new Integer(-1));
//        System.out.println(new Integer(-1).intValue()==new Integer(-1).intValue());

        List<Integer> ints = new ArrayList<>();
        ints.add(1);
        ints.add(1);
        ints.add(-1);
        ints.add(-685);
        ints.add(-254);
        ints.add(-129);
        ints.add(-100);
        List<Integer> ints2 = new ArrayList<>();
        ints2.add(1);
        ints2.add(1);
        ints2.add(-1);
        ints2.add(-685);
        ints2.add(-254);

        ints2.add(-129);
        ints2.add(-100);

//        System.out.println(ints.get(0)==ints.get(1));
//        System.out.println(ints.get(0)==ints2.get(0));
//        System.out.println(ints.get(0)==ints2.get(1));
//        System.out.println(ints.get(2)==ints2.get(2));


//        System.out.println(ints.get(3)==ints2.get(3));
//        System.out.println(ints.get(4)==ints2.get(4));
//        System.out.println(ints.get(5)==ints2.get(5));


        boolean result = code.isSameTree(nodeT1,nodeT2);
        System.out.println(result);
        System.out.print(Arrays.toString(list.toArray()));

        boolean result2 = code.isSameTree2(nodeT1,nodeT2);
        System.out.println(result2);

    }

    // 这个是算拆箱装箱的一个bug Integer(-685)==Integer(-685)  返回的是false
    // 经过测试 是Integer(-128)==Integer(-128)  是true
    // 经过测试 是Integer(-129)==Integer(-129)  是false
    // 可以理解成 一个char 以下的 就是同一个对象， 这个对象可能是由内置的
    // 超过  某一个长度 就需要额外new出来了。 并且是不保存在系统内置对象中的。

    // 核心 ： null 也得加入到list当中
    // int 不能 表示null 所以要用Integer
    // 但是 Integer()负数 是有可能会不相等的 要用Integer.intValue;
    public boolean isSameTree(TreeNode p, TreeNode q) {


        if(p==null&&q==null)  return true;

        List<Integer> pList = new ArrayList<>();
        List<Integer> qList = new ArrayList<>();

        orderTree(p,pList);
        orderTree(q,qList);

        if(pList.size()!=qList.size()) return false;

        for(int i =0 ;i<pList.size();i++){

            if(pList.get(i) == null &&qList.get(i) == null){
                continue;
            }else if(pList.get(i) == null ||qList.get(i) == null){
                return false;
            }



            if(pList.get(i).intValue() != qList.get(i).intValue()){
//                continue;
                System.out.print(pList.get(i));
                System.out.print(qList.get(i));

                return false;
            }



        }

        return true;

    }

    public void orderTree(TreeNode node,List<Integer> list){
        if(node == null){
            list.add(null);
            return ;
        }


        list.add(node.val);
        orderTree(node.left,list);

        orderTree(node.right,list);



    }



    public static  class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
          this.right = right;
      }
    }


    public boolean isSameTree2(TreeNode p, TreeNode q) {
        if(p == null &&q ==null) return true;

        if(p == null) return false;
        if(q == null) return false;

        if(p.val != q.val) return false;

        if(!isSameTree2(p.left,q.left)){
            return false;
        }
        if(!isSameTree2(p.right,q.right)){
            return false;
        }

        return true;


    }



}
