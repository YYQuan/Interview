package akotlin
import akotlin.Util.TreeNode

fun main() {
    val t1 = TreeNode(1)
    val t2 = TreeNode(2)
    val t3 = TreeNode(3)
    t1.right = t2
    t2.left = t3
    Code94().inorderTraversal(t1).let {
        println(it)
    }

}
class Code94 {
    fun inorderTraversal(root: TreeNode?): List<Int> {
        val result =mutableListOf<Int>()
        inorderTraversal(root,result )
        return  result
    }
    fun inorderTraversal(root: TreeNode?,result: MutableList<Int>) {
        if(root == null){
            return
        }
        inorderTraversal(root.left,result)
        result.add(root.`val`)
        inorderTraversal(root.right,result)

    }
}