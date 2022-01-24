package akotlin
import akotlin.Util.TreeNode
import java.util.*

fun main() {
    Util.transferTreeNode4Array(arrayOf(3,9,20,null,null,15,7)).let {
        Code107().levelOrderBottom(it).let {
            it.forEach {
                println()
                it.forEach {
                    print("$it ")
                }
            }
        }
    }
}
class Code107 {
    fun levelOrderBottom(root: TreeNode?): List<List<Int>> {
        val queue = LinkedList<List<TreeNode>>()

        val queueRow = LinkedList<TreeNode?>()
        val result = mutableListOf<List<Int>>()

        queueRow.add(root)
        while(queueRow.isNotEmpty()){

            val size = queueRow.size
            val nodeList = mutableListOf<TreeNode>()
            for(i in 1..size){
                val node = queueRow.removeFirst()
                node?.let {
                    nodeList.add(node)
                    queueRow.addLast(node.left)
                    queueRow.addLast(node.right)
                }

            }
            if(nodeList.isNotEmpty())
                queue.add(nodeList)
        }

        val size = queue.size
        for(i in 1..size){
            val list = queue.removeLast()
            val listInt = mutableListOf<Int>()
            list.forEach {
                listInt.add(it.`val`)
            }
            result.add(listInt)
        }
        return result



    }
}