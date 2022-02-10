package akotlin
import akotlin.Util.TreeNode

fun main() {
//    Util.transferTreeNode4Array(arrayOf(10,5,-3,3,2,null,11,3,-2,null,1,null,null,null,null)).let {
//    Util.transferTreeNode4Array(arrayOf(5,4,8,11,null,13,4,7,2,null,null,5,1,null,null)).let {
    Util.transferTreeNode4Array(arrayOf(1,
        null,2,
        null,null,null,3,
        null,null,null,null,null,null,null,4,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,5)).let {
        Util.print(it!!)
        Code437().pathSum(it,3).let {
            println(it)
        }
    }
}

class Code437 {
    // 怎么去重？
    // 分成 包含 本节点的 和不包含本节点的两种处理 就不有有重复的了
    fun pathSum(root: TreeNode?, targetSum: Int): Int {
        if(root == null ) return 0
        return findSum(root,targetSum)+pathSum(root.left,targetSum)+pathSum(root.right,targetSum)
    }

    // 包含root 节点的
    fun findSum(root: TreeNode?, targetSum: Int): Int {
       if(root == null) return 0
        var result = 0
        if(root.`val` == targetSum) result = 1

        result +=findSum(root.left,targetSum-root.`val`)
        result +=findSum(root.right,targetSum-root.`val`)
        return result

    }
}