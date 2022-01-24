package akotlin
import akotlin.Util.TreeNode
import java.util.*

fun main() {
    Util.transferTreeNode4Array(arrayOf(3,9,20,null,null,15,7)).let {
        Code103().zigzagLevelOrder(it).let {
            it.forEach {
                println()
                it.forEach {
                    print("$it ")
                }
            }
        }
    }
}
class Code103 {
    fun zigzagLevelOrder(root: TreeNode?): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        val queue = LinkedList<TreeNode?>()
        queue.add(root)
        var isLeft= true
        while(queue.isNotEmpty()){

            val size = queue.size
            val list = LinkedList<Int>()
            for(i in 1 ..size) {
                val node = queue.removeFirst()
                node?.let {
                    if(isLeft) list.addLast(node.`val`)
                    else list.addFirst(node.`val`)
                    queue.addLast(node.left)
                    queue.addLast(node.right)
                }
            }
            isLeft=!isLeft
            if(list.isNotEmpty()) result.add(list)
        }
        return result
    }
}