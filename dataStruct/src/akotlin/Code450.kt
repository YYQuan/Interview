package akotlin
import akotlin.Util.TreeNode
import java.util.*

fun main() {
//    Util.transferTreeNode4Array(arrayOf(5,3,6,2,4,null,7)).let {
//    Util.transferTreeNode4Array(arrayOf(5,3,6,2,4,null,7)).let {
    Util.transferTreeNode4Array(arrayOf(50,30,70,null,40,60,80)).let {
//    Util.transferTreeNode4Array(arrayOf(
//        2,
//        0,33,
//        null,1,25,40,
//        null,null,11,31,34,45,10,
//        18,29,32,null,36,43,46,4,null,12,24,26,30,null,null,35,
//        39,42,44,null,48,3,9,null,14,22,null,null,27,null,null,null,null,38,null,41,null,null,null,47,49,null,null,5,null,13,15,
//        21,23,null,28,37,null,null,null,null,null,null,null,null,8,null,null,null,17,19,null,null,null,null,null,null,null,7,null,16,null,null,20,6)).let {
        Util.print(it!!)
        Code450().deleteNode(it,50).let {
            Util.print(it!!)
        }
    }
}
class Code450 {
    // 核心 找到 待删除节点的右子树的最左节点
    fun deleteNode(root: TreeNode?, key: Int): TreeNode? {
        if(root == null) return  null
        val virtualTreeNode = TreeNode(key+1)
        virtualTreeNode.left = root
        val queue = LinkedList<TreeNode>()
        queue.add(virtualTreeNode)
        while(queue.isNotEmpty()){
            var size = queue.size
            while(size-->0){
                val node = queue.removeFirst()
                node.left?.let {
                    if(node.left!!.`val` == key){
                        val delNode = node.left
                        solution(node!!,delNode!!,true)
                        return virtualTreeNode.left
                    }
                    else queue.add(node.left!!)
                }
                node.right?.let {
                    if(node.right!!.`val` == key){
                        val delNode = node.right
                        solution(node!!,delNode!!,false)
                        return virtualTreeNode.left
                    }
                    else queue.add(node.right!!)
                }
            }
        }

        return virtualTreeNode.left

    }

    // 注意题目只要求保持为 二叉搜索树
    // 所以只需要把 被删除节点的 左子树移动到 右子树的最左边即可
    fun solution(parentNode :TreeNode,delNode:TreeNode,isLeft:Boolean= false){

        // 删除 节点没有 右子树
        if(delNode.right ==null){
            if(isLeft) parentNode.left = delNode.left
            else parentNode.right  = delNode.left
        }
        // 删除 节点有 右子树
        else {
            var subRightNode = delNode.right
            while(subRightNode!!.left!=null ){
                subRightNode = subRightNode.left
            }
            subRightNode.left = delNode.left
            if(isLeft) parentNode.left = delNode.right
            else parentNode.right = delNode.right
        }


    }


}