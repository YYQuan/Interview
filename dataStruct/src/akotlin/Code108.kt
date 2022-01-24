package akotlin
import akotlin.Util.TreeNode

fun main() {
    Code108().sortedArrayToBST(intArrayOf(-10,-3,0,5,9)).let {
        Util.print(it!!)
    }
}

class Code108 {
    fun sortedArrayToBST(nums: IntArray): TreeNode? {
        return sortedArrayToBST(nums,0,nums.size-1)
    }
    fun sortedArrayToBST(nums: IntArray,start:Int,end:Int): TreeNode? {
//        println("start $start  end :$end")
        if(start<0||start>end) return null
        val mid = (end -start+1)/2+start
        val node = TreeNode(nums[mid])
//        println("start $start  end :$end   mid :$mid  node ${node.`val`}")
        node.left = sortedArrayToBST(nums,start,mid-1)
        node.right = sortedArrayToBST(nums,mid+1,end)
        return node
    }
}