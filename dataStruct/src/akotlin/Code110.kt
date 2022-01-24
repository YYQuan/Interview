package akotlin
import akotlin.Util.TreeNode
import java.util.*


fun main() {


    Util.transferTreeNode4Array(arrayOf(3,9,20,null,null,15,7)).let {
        Util.print(it!!)
        Code110().isBalanced(it).let {
            println(it)
        }
    }
}
class Code110 {
    fun isBalanced(root: TreeNode?): Boolean {

        if(root == null) return true


        val queue = LinkedList<TreeNode?>()
        queue.add(root)
        while(queue.isNotEmpty()){
            val size = queue.size
            for(i in 1..size){
                val node= queue.removeFirst()

                node?.let {
                    if( Math.abs(isBalanced(node.left,0)-isBalanced(node.right,0))>1){
//                        println("node.left ${node.left?.`val`}  node.right :${node.right?.`val`}  ${ isBalanced(node.left,0)} ${ isBalanced(node.right,0)}  ")
                        return false
                    }

                    queue.add(node.left)
                    queue.add(node.right)
                }

            }
        }

        return true
    }
    fun isBalanced(root: TreeNode?,depth:Int): Int {
        if(root ==null) return depth;
        val d1 = isBalanced(root?.left,depth+1)
        val d2 = isBalanced(root?.right,depth+1)
        return Math.max(d1,d2)
    }

}