package akotlin
import akotlin.Util.TreeNode

fun main() {
    val t = TreeNode(1)
    val tl1 = TreeNode(2)
    val tr1 = TreeNode(2)
    val tl2 = TreeNode(3)
    val tr2 = TreeNode(3)
    val tl3 = TreeNode(4)
    val tr3 = TreeNode(4)
    t.left = tl1
    t.right = tr1

    tl1.left=tl2
    tl1.right=tl3

    tr1.left=tr3
    tr1.right=tr2
    Code101().isSymmetric(t).let {
        println(it)
    }
}
class Code101 {
    fun isSymmetric(root: TreeNode?): Boolean {
        if(root == null) return true
        val int1 = lOrder(root.left)
        val int2 = rOrder(root.right)
//        int1.forEach {
//            print("$it ")
//        }
//        println("----")
//        int2.forEach {
//            print("$it ")
//        }
//        println()
        if(int1.size != int2.size) return false
        int1.forEachIndexed { index, i ->
            if(i != int2[index])return false
        }
        return true
    }

    fun lOrder(root:TreeNode?):MutableList<Int?>{
        val result = mutableListOf<Int?>()
        lOrder(root,result)
        return result
    }
    fun lOrder(root:TreeNode?,result:MutableList<Int?>){
        if(root==null){
            result.add(null)
            return
        }
        result.add(root.`val`)
        lOrder(root.left,result)
        lOrder(root.right,result)

    }
    fun rOrder(root:TreeNode?):MutableList<Int?>{
        val result = mutableListOf<Int?>()
         rOrder(root,result)
        return result
    }
    fun rOrder(root:TreeNode?,result:MutableList<Int?>){
        if(root==null){
            result.add(null)
            return
        }
        result.add(root.`val`)

        rOrder(root.right,result)
        rOrder(root.left,result)

    }

}