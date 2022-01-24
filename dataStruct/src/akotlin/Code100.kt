package akotlin
import akotlin.Util.TreeNode

fun main() {
//    val ints = intArrayOf(1,null,2,3)
}
class Code100 {
    fun isSameTree(p: TreeNode?, q: TreeNode?): Boolean {
        if(p == null && q == null) return true
        if(p == null ) return false
        if(q == null ) return false
        if(q.`val` == p.`val`) {
            val r1 = isSameTree(p.left,q.left)
            if(!r1) return false
            val r2 =isSameTree(p.right,q.right)
            if(!r2) return false
        }else{
            return false
        }
        return true
    }


}