package akotlin
import akotlin.Util.TreeNode

fun main() {

    Util.transferTreeNode4Array(arrayOf(3,9,20,null,null,15,7)).let {
        Code104().maxDepth(it).let {
            println(it)
        }
    }
}
class Code104 {
    fun maxDepth(root: TreeNode?): Int {
        return maxDepth(root,0)
    }
    fun maxDepth(root: TreeNode?,depth:Int): Int {
        if(root == null) return depth
        return Math.max(maxDepth(root.left ,depth+1) ,maxDepth(root.right ,depth+1))
    }
}