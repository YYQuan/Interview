package akotlin
import akotlin.Util.TreeNode

fun main() {
//    Util.transferTreeNode4Array(arrayOf(6,2,8,0,4,7,9,null,null,3,5,null,null,null,null)).let {
//        Code235().lowestCommonAncestor(it,TreeNode(2),TreeNode(8)).let {
    Util.transferTreeNode4Array(arrayOf(6,2,8,0,4,7,9,null,null,3,5,null,null,null,null)).let {
        Code235().lowestCommonAncestor(it,TreeNode(2),TreeNode(4)).let {
            println(it?.`val`)
        }
    }

}
class Code235 {
    // 根据二叉搜索树的性质,找到 p,q之间的值的节点就是最近公共祖先了
    fun lowestCommonAncestor(root: TreeNode?, p: TreeNode?, q: TreeNode?): TreeNode? {
//        println("${root?.`val`} ${p?.`val`}  ${q?.`val`}")
        if(root == null)  return null
        if(p == null && q ==null) return null
        if(p == null) return q
        if(q == null) return p

        val min =if( p.`val` > q.`val`) q else p
        val max =if( p.`val` > q.`val`) p else q
        return if(root.`val` == max.`val`) max
        else if(root.`val` == min.`val`) min
        else if(root.`val`<max.`val` && root.`val`>min.`val`) root
        else if( root.`val` >max.`val`) lowestCommonAncestor(root.left,p,q)
        else lowestCommonAncestor(root.right,p,q)
    }
}