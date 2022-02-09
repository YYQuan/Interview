package akotlin
import akotlin.Util.TreeNode

fun main() {
    Util.transferTreeNode4Array(arrayOf(3,5,1,6,2,0,8,null,null,7,4,null,null,null,null)).let {
        Code236().lowestCommonAncestor(it,TreeNode(5),TreeNode(4)).let {
            println(it?.`val`)
        }
    }
}
class Code236 {
    // 思路 最近公共节点 ，那么就是一个在左 一个在右 ，不会在同一侧
    fun lowestCommonAncestor(root: TreeNode?, p: TreeNode?, q: TreeNode?): TreeNode? {
        if(root == null) return null
        if(p == null&&q ==null) return null
        if(p ==null) return q
        if(q ==null) return p

        val isRight  = containNode(root.right,p,q)
        val isLeft  = containNode(root.left,p,q)

        return if( isRight&&isLeft) root
        else if( (root.`val` == p.`val` || root.`val` ==q.`val`) && (isRight||isLeft)) return root
        else if( isRight ) lowestCommonAncestor(root.right,p,q)
        else if( isLeft ) lowestCommonAncestor(root.left,p,q)
        else {
            null
        }

    }

    fun  containNode(root:TreeNode?,p:TreeNode,q:TreeNode):Boolean{
        if(root == null) return false
        if(root.`val` == p.`val`) return true
        if(root.`val` == q.`val`) return true

        return containNode(root.left,p,q)||containNode(root.right,p,q)

    }
}