package akotlin
import akotlin.Util.TreeNode
fun main() {
    Util.transferTreeNode4Array(arrayOf(1,null,2,null,null,3,null)).let {
        Util.print(it!!)
        Code145().postorderTraversal(it).let {
            println(it)
        }
    }

}
class Code145 {
    fun postorderTraversal(root: TreeNode?): List<Int> {
        val result = mutableListOf<Int>()
        postorderTraversal(root,result)
        return result
    }
    fun postorderTraversal(root: Util.TreeNode?, result: MutableList<Int>) {
        if(root== null) return
        postorderTraversal(root.left,result)
        postorderTraversal(root.right,result)
        result.add(root.`val`)
    }
}