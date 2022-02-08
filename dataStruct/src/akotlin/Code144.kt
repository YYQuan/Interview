package akotlin
import akotlin.Util.TreeNode

fun main() {
    Util.transferTreeNode4Array(arrayOf(1,null,2,null,null,3,null)).let {
        Util.print(it!!)
        Code144().preorderTraversal(it).let {
            println(it)
        }
    }

}
class Code144 {
    fun preorderTraversal(root: TreeNode?): List<Int> {
        val result = mutableListOf<Int>()
        preorderTraversal(root,result)
        return result
    }
    fun preorderTraversal(root: TreeNode?,result: MutableList<Int>) {
        if(root== null) return
        result.add(root.`val`)
        preorderTraversal(root.left,result)
        preorderTraversal(root.right,result)

    }
}