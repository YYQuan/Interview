package akotlin
import akotlin.Util.TreeNode

fun main() {
    Util.transferTreeNode4Array(arrayOf(3,9,20,null,null,15,7)).let{
//    Util.transferTreeNode4Array(arrayOf(2,null,3,null,null,null,4,null,null,null,null,null,null,null,5,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,6)).let{
        Util.print(it!!)
        Code111().minDepth(it).let { println(it) }
    }

}
class Code111 {
    fun minDepth(root: TreeNode?): Int {
        if(root ==null) return 0

        return  minDepth(root,0)
    }
    fun minDepth(root: TreeNode?,depth:Int): Int {
        if(root == null) return Int.MAX_VALUE
        if(root.left == null &&root.right ==null) return depth+1
        return Math.min(minDepth(root.left,depth+1),minDepth(root.right,depth+1))
    }
}