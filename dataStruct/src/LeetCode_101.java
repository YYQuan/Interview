package src;

import java.util.ArrayList;
import java.util.List;
import  src.TreeNodeUtil.TreeNode;

public class LeetCode_101 {

    public static void main(String[] args) {
        LeetCode_101 code = new LeetCode_101();
        TreeNode node = TreeNodeUtil.transferArrays2Tree(new Integer[]{1,2,2,2,null,2});
        System.out.println(code.isSymmetric(node));
        System.out.println(code.isSymmetric2(node));

    }

    // 击败 6
    public boolean isSymmetric(TreeNode root) {

        if(root == null)  return true;

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        r2lOrder(root,list1);
        l2rOrder(root,list2);

        for(int i = 0 ;i<list1.size();i++){

            if(list1.get(i)==null&&list2.get(i)==null){
                continue;
            }else if(list1.get(i)==null||list2.get(i)==null){
                return false;
            }else{
                if(list1.get(i).intValue()!=list2.get(i).intValue()){
                    return false;
                }

            }

        }

        return true;


    }


    public  void r2lOrder(TreeNode root ,List<Integer> list ){

        if(root==null){
            list.add(null);
            return ;
        }

        list.add(root.val);
        r2lOrder(root.right,list);
        r2lOrder(root.left,list);
    }
    public  void l2rOrder(TreeNode root ,List<Integer> list ){

        if(root==null){
            list.add(null);
            return ;
        }

        list.add(root.val);
        l2rOrder(root.left,list);
        l2rOrder(root.right,list);

    }





    public boolean isSymmetric2(TreeNode root) {

        if(root==null)  return true;
        List<Integer> resultL =fOrder(root.left);
        List<Integer> resultR =lOrder(root.right);

        if(resultL ==null && resultR == null) return true;
        if(resultL ==null) return false;
        if(resultR ==null) return false;

        if(resultL.size() != resultR.size()){
            return false;
        }

        for(int i = 0 ; i<resultL.size();i++){
            System.out.println(resultL.get((int)i) +"   "+resultR.get((int)i));
            if((resultL.get((int)i)) != (resultR.get((int)i))){
                return false;
            }
        }
        return true;
    }

    public List<Integer>  lOrder(TreeNode root){
        List<Integer>  result = new ArrayList<>();
        lOrder(result,root);
        return result;
    }

    public void lOrder(List<Integer> result ,TreeNode node){
        if(node == null) {
            result.add(null);
            return ;
        }
        result.add(node.val);
        lOrder(result, node.right);

        lOrder(result,node.left);
    }


    public List<Integer>  fOrder(TreeNode node){
        List<Integer> result = new ArrayList<>();
        fOrder(result,node);
        return result;
    }
    public void fOrder(List<Integer> result,TreeNode node){
        if(node == null){
            result.add(null);
            return ;
        }
        result.add(node.val);
        fOrder(result,node.left);

        fOrder(result,node.right);
    }

}
