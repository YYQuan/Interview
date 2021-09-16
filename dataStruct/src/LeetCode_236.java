package src;

import src.TreeNodeUtil.TreeNode;

import java.util.LinkedList;

public class LeetCode_236 {

    public static void main(String[] args) {
        LeetCode_236 code = new LeetCode_236();
        Integer[] ints = new Integer[]{3,5,1,6,2,0,8,null,null,7,4};
//        Integer[] ints = new Integer[]{4,2,6,1,3,null,null};
//        Integer[] ints = new Integer[]{6,2,8,0,4,7,9,null,null,3,5};
//        Integer[] ints = new Integer[]{5,1,4,null,null,3,6};
//        Integer[] ints = new Integer[]{5,3,6,2,4,null,7};
//        int[] ints = new int[]{1,2,3,4,5,6,7,8,9};
//        Integer[] ints = new Integer[]{3,1,4,null,2};
//        Integer[] ints = new Integer[]{1,null,2};


        TreeNode head = TreeNodeUtil.transferArrays2Tree(ints);
//        TreeNode  result = code.lowestCommonAncestor(head,new TreeNode(2),new TreeNode(8));

        TreeNode result = code.lowestCommonAncestor(head,
                new TreeNode(5),new TreeNode(4));
        TreeNode result2 = code.lowestCommonAncestor2(head,
                new TreeNode(5),new TreeNode(4));

        System.out.println(result.val);
        System.out.println(result2.val);
//        TreeNodeUtil.printOrderTree(result);
    }

    // 方案3
    // left 有没有 p/q  right  有没有 p/q
    // 不需要知道 具体是 p还是q  只要有就行
    // 击败  百分之5
    // 但是 感觉比网友的答案好理解 思路清晰些。
    // 这个对比方案1的 思路  优化点就在于不关心 root 拥有的是p ，还是q
    public TreeNode  lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {


        boolean isLeftParentNode = containTreeNode(root.left,p,q);
        boolean isRightParentNode = containTreeNode(root.right,p,q);

        if(isLeftParentNode&&isRightParentNode){
            return root;
        }else if(!isLeftParentNode&&!isRightParentNode){
            // 两边都没有  正常没有这个情况
            return null;
        }else{
            // 只有一边有
            if(root.val == p.val||root.val == q.val){
                return root;
            }else if(isLeftParentNode){
                return lowestCommonAncestor(root.left,p,q);
            }else {
                return lowestCommonAncestor(root.right,p,q);
            }
        }
    }

    public boolean containTreeNode(TreeNode root , TreeNode p ,TreeNode q){

        if(root == null) return false;
        if(root.val == p.val) return true;
        if(root.val == q.val) return true;

        return containTreeNode(root.left,p,q)||containTreeNode(root.right,p,q);

    }

        // 方案2 思路  遍历一遍， 得到 p 和q的 序号 就可以 知道 公共祖先是多少了 但是这个数学关系是怎么样的？


    // 方案1 超时  时间复杂度是 O（n^2 log n ）
    public TreeNode  lowestCommonAncestor1(TreeNode root, TreeNode p, TreeNode q) {


        boolean isParentNode = containTreeNode(root,p)&&containTreeNode(root,q);
        boolean isLeftParentNode = containTreeNode(root.left,p)&&containTreeNode(root.left,q);
        boolean isRightParentNode = containTreeNode(root.right,p)&&containTreeNode(root.right,q);


        if(isParentNode&&!isLeftParentNode&&!isRightParentNode){
            return root;
        }else if(isLeftParentNode){
            return lowestCommonAncestor(root.left,p,q);
        }else if(isRightParentNode){
            return lowestCommonAncestor(root.right,p,q);
        }

        // 没有父节点
        throw new IllegalArgumentException();

    }




    public boolean containTreeNode(TreeNode root , TreeNode node){

        if(root == null) return false;
        if(root.val == node.val) return true;

        return containTreeNode(root.left,node)||containTreeNode(root.right,node);

    }

    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {

        if(root == null)  return null;

        TreeNode node = root;
        TreeNode result = null;
        LinkedList<TreeNode> nodes = new LinkedList<>();
        nodes.add(node);
        while(!nodes.isEmpty()){

            int size = nodes.size();
            while(size>0){
                size--;
                TreeNode tmpNode = nodes.poll();
                if (tmpNode == null) break;
                if(containNode(tmpNode,p)&&containNode(tmpNode,q)){
                    result = tmpNode;
                    nodes.clear();
                    if(tmpNode.left!=null) {
                        nodes.add(tmpNode.left);
                    }
                    if(tmpNode.right!=null) {
                        nodes.add(tmpNode.right);
                    }
                    break;
                }
            }



        }

        return result;

    }

   public boolean containNode(TreeNode root , TreeNode node){
        if(root == null)  return false;
        if(root.val ==node.val)  return true;
        return containNode(root.left,node)  || containNode(root.right,node);
   }

}
