package akotlin
import akotlin.Util.TreeNode

fun main() {
    Util.transferTreeNode4Array(arrayOf(
        5,
        4,8,
        11,null,13,4,
        7,2,null,null,null,null,null,1
    )).let {
        Code112().hasPathSum(it,22).let {
            println(it)
        }
    }
//    Util.transferTreeNode4Array(arrayOf(1,2,null)).let {
//        Code112().hasPathSum(it,0).let {
//            println(it)
//        }
//    }
}
class Code112 {


    fun hasPathSum(root: TreeNode?, targetSum: Int): Boolean {
        if(root != null&&root.left==null&&root.right==null&&targetSum == root.`val`) return true
        if(root == null) return false
        return hasPathSum(root.left,targetSum-root.`val`)||hasPathSum(root.right,targetSum-root.`val`)
    }
}