package akotlin
import akotlin.Util.TreeNode
import com.sun.org.apache.bcel.internal.classfile.Code

fun main() {
//    Code230().kthSmallest(Util.transferTreeNode4Array(arrayOf(3,1,4,null,2,null,null)),1).let {
    Code230().kthSmallest(Util.transferTreeNode4Array(arrayOf(5,3,6,2,4,null,null,1,null,null,null,null,null,null,null)),3).let {
        println(it)
    }
}
class Code230 {
    //
    fun kthSmallest(root: TreeNode?, k: Int): Int {
        if(root == null) return 0
        val leftCount =getCount(root.left)
        if(leftCount+1 == k) return root.`val`


        if(leftCount>=k){
            return kthSmallest(root.left,k)
        }else{
            return kthSmallest(root.right,k-leftCount-1)
        }


    }

    fun getCount(root:TreeNode?):Int{
        if(root== null) return 0
        return getCount(root.left)+getCount(root.right)+1
    }
}