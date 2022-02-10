package akotlin
import akotlin.Util.TreeNode
import java.util.*

fun main() {
//    Util.transferTreeNode4Array(arrayOf(4,2,6,1,3,null,null)).let {
    Util.transferTreeNode4Array(arrayOf(1,0,48,null,null,12,49)).let {
        Code530().getMinimumDifference(it!!).let {
            println(it)
        }
    }
}
class Code530 {
    fun getMinimumDifference(root: TreeNode?): Int {

        if(root == null) return 0
        val list = mutableListOf<Int>()
        order(root,list)
        var min = Int.MAX_VALUE
        for(i in 1..list.lastIndex){
            min = Math.min(min, list[i]-list[i-1])
        }
        return min
    }

    fun order(root:TreeNode?,list:MutableList<Int>){
        if(root==null) return
        order(root.left,list)
        list.add(root.`val`)
        order(root.right,list)
    }
}