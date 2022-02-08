package akotlin
import  akotlin.Util.TreeNode

fun main() {
    Util.transferTreeNode4Array(arrayOf(4,2,7,1,3,6,9)).let {
        Code226().invertTree(it).let {
            Util.print(it!!)
        }
    }
}
class Code226 {
    fun invertTree(root: TreeNode?): TreeNode? {
        if(root == null)  return root
        val tmp  = root.left
        root.left = root.right
        root.right = tmp

        invertTree(root.left)
        invertTree(root.right)

        return root
    }
}