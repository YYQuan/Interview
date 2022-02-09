package akotlin
import akotlin.Util.TreeNode

fun main() {
//    Util.transferTreeNode4Array(arrayOf(3, 2, 3, null, 3, null, 1)).let {
//    Util.transferTreeNode4Array(arrayOf(3, 4, 5, 1, 3, null, 1)).let {
    Util.transferTreeNode4Array(arrayOf(4,1,null,2,null,null,null,3,null,null,null,null,null,null,null)).let {


        Code337().rob(it).let {
            println(it)
        }

    }
}
class Code337 {
    // 常规递归有大量重复计算
    // 优化思路是自下而上 减少重复计算
    fun rob(root: TreeNode?): Int {
        robD(root).let {
            return Math.max(it[0],it[1])
        }
    }
    // int[2] {sel root , unsel root }
    fun robD(root: TreeNode?): IntArray {

       if(root == null) return intArrayOf(0,0)
        if(root.left == null&&root.right==null) return intArrayOf(root.`val`,0)
        val l = robD(root.left)
        val r = robD(root.right)

//        println("root.val ${root.`val`} l ${l[0]}  ${l[1]} r ${r[0]}  ${r[1]}")
//        val result = Math.max(l[0]+r[0],root.`val`+l[1]+r[1])

        return intArrayOf(root.`val`+l[1]+r[1],
            Math.max(l[0],l[1])+Math.max(r[0],r[1]) //root.left/right 可选可不选  哪个大的往上报哪个
        )
    }
}