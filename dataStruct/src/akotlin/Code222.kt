package akotlin
import akotlin.Util.TreeNode
import java.util.*

class Code222 {
    fun countNodes(root: TreeNode?): Int {
        if(root == null) return 0
        val queue = LinkedList<TreeNode>()
        queue.add(root)
        var result = 0
        while(queue.isNotEmpty()){

            var size = queue.size
            while(size-->0){
                val node = queue.removeFirst()
                result++
                node.left?.let {
                    queue.addLast(node.left)
                }
                node.right?.let {
                    queue.addLast(node.right)
                }
            }

        }
        return result
    }
}