package akotlin
import akotlin.Util.TreeNode

fun main() {
    val t1 = TreeNode(2)
    val t2 = TreeNode(2)
    val t3 = TreeNode(2)
    t1.left = t2
    t1.right = t3
    Code98().isValidBST(t1).let {
        println(it)
    }
}
class Code98 {
    fun isValidBST(root: TreeNode?): Boolean {
        val ints = check(root)
        ints.forEachIndexed { index, i ->
            if(index>0){
                if(ints[index-1]>= i) return false
            }
        }
        return true
    }
    fun  check(root:TreeNode?):List<Int>{
        val result = mutableListOf<Int>()
         check(root,result)
        return  result
    }
    fun  check(root:TreeNode?,result:MutableList<Int>){
        if(root == null ) return


        check(root.left,result)
        result.add(root.`val`)
        check(root.right,result)
    }
}