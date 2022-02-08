package akotlin
import akotlin.Util.TreeNode
import java.util.*

fun main() {
    Code199().rightSideView(Util.transferTreeNode4Array(arrayOf(1,2,3,null,5,null,4))).let {
        println(it)
    }
}
class Code199 {
    fun rightSideView(root: TreeNode?): List<Int> {

        val result = mutableListOf<Int>()
        if(root == null)  return result
        val queue = LinkedList<TreeNode>()
        queue.add(root)
        while(queue.isNotEmpty()){

            var size = queue.size
//            println("size $size   ${queue[queue.lastIndex]}")
//            queue.forEach {
//                print("it.`val`  ${it.`val`}  ")
//            }
//            println()
            result.add(queue[queue.lastIndex].`val`)
            while(size>0){
                val node =queue.removeFirst()
                if(node!=null) {
                    node.left?.let {
                        queue.addLast(node.left)
                    }
                    node.right?.let {
                        queue.addLast(node.right)
                    }
                }
                size--

            }
        }


        return result
    }
}