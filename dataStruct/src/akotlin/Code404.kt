package akotlin
import akotlin.Util.TreeNode
import java.util.*

fun main() {

    Util.transferTreeNode4Array(arrayOf(3,9,20,null,null,15,7)).let {
        Util.print(it!!)
        Code404().sumOfLeftLeaves(it).let {
            println(it)
        }
    }
}
class Code404 {
    fun sumOfLeftLeaves(root: TreeNode?): Int {
        if(root == null) return 0
        if(root.left ==null &&root.right ==null) return 0
        val queue =LinkedList<TreeNode>()
        queue.add(root)
        val list = mutableListOf<TreeNode>()
        while(queue.isNotEmpty()){

            val size = queue.size
            var count = size
            while(count-->0){
                val node = queue.removeFirst()



                node.left?.let {
                    queue.add(node.left!!)
                    // 判断自己的左节点是不是叶子节点
                    if(node.left!!.left == null &&node.left!!.right==null) list.add(node.left!!)
                }
                node.right?.let {
                    queue.add(node.right!!)
                }

            }

        }

        var sum  = 0
        list.forEach {
            sum+=it.`val`
        }
        return sum

    }
}