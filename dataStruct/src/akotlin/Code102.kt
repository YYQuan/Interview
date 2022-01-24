package akotlin
import  akotlin.Util.TreeNode
import java.util.*

fun main() {
    Util.transferTreeNode4Array(arrayOf(3,9,20,null,null,15,7)).let {
        Code102().levelOrder(it).let {

            it.forEach {
                println()
                it.forEach {
                    print("$it ")
                }
            }
        }
    }
}
class Code102 {
    fun levelOrder(root: TreeNode?): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        val queue = LinkedList<TreeNode?>()
        queue.add(root)

        while(!queue.isEmpty()){
            val size = queue.size
            val list = mutableListOf<Int>()
            for(i in 1..size){

                val node = queue.removeFirst()
                if(node!=null) {
                    list.add(node.`val`)
                    queue.addLast(node.left)
                    queue.addLast(node.right)
                }

            }
            if(list.isNotEmpty())
                result.add(list)
        }
        return result

    }
}