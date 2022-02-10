package akotlin
import akotlin.Util.TreeNode

fun main() {
//    Util.transferTreeNode4Array(arrayOf(4,2,6,1,3,null,null)).let {
    Util.transferTreeNode4Array(arrayOf(1,0,48,null,null,12,49)).let {
        Code783().minDiffInBST(it).let {
            println(it)
        }
    }

}
class Code783 {
    fun minDiffInBST(root: TreeNode?): Int {

        if(root == null) return 0
        val  ints = order(root)

        var min = Int.MAX_VALUE

        for(i in 1..ints.lastIndex){

            min = Math.min(min, ints[i]-ints[i-1])
        }
        return min

    }
    fun order(root:TreeNode):ArrayList<Int>{
        val result  = ArrayList<Int>()
        order(root,result)
        return result
    }
    fun order(root:TreeNode?,result:MutableList<Int>){
        if(root == null)  return

        order(root.left,result)
        result.add(root.`val`)
        order(root.right,result)

    }

}