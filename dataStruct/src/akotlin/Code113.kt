package akotlin
import akotlin.Util.TreeNode
import java.util.*

fun main() {
    Util.transferTreeNode4Array(arrayOf(
        5,
        4,8,
        11,null,13,4,
        7,2,null,null,null,null,5,1
    )).let {
        Code113().pathSum(it,22).let {

            it.forEach {
                println()
                it.forEach {
                    print("$it ")
                }
            }
        }
    }
//    Util.transferTreeNode4Array(arrayOf(1,2,null)).let {
//        Code112().hasPathSum(it,0).let {
//            println(it)
//        }
//    }
}
class Code113 {
    fun pathSum(root: TreeNode?, targetSum: Int): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        if(root ==null) return result
         pathSum(root,targetSum,LinkedList(),result)
        return result
    }
    fun pathSum(root: TreeNode?, targetSum: Int,list:LinkedList<Int>,result: MutableList<List<Int>>) {
        if(root!=null &&root.left==null&&root.right==null&&root.`val` == targetSum) {

            result.add(mutableListOf<Int>().let {
                it.addAll(list)
                it.add(root.`val`)
                it
            })
            return
        }
        if(root == null) return

        list.addLast(root.`val`)
        pathSum(root.left,targetSum-root.`val`,list,result)
        pathSum(root.right,targetSum-root.`val`,list,result)
        list.removeLast()
    }
}