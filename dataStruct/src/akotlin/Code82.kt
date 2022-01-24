package akotlin
import akotlin.Util.ListNode

fun main() {

    val ints  = intArrayOf(1,2,3,3,4,4,5)
//    val ints  = intArrayOf(1,1)
    Code82().deleteDuplicates(Util.transferListNode4Array(ints)).let {
        it?.let {  Util.print(it!!)}
    }
}
class Code82 {
    // 核心思路  用 一个 node 来指定 next 只有 和左右都不相同的node才被保留
    fun deleteDuplicates(head: ListNode?): ListNode? {

        if(head == null) return null
        val virtualHead =ListNode(head.`val`-2)
        // 多加 一个头 要不 [1,1]这种情况下， 由于 和虚拟头部一样 就被判定成需要连接起来的了
        val virtualHead2 =ListNode(head.`val`-1)
        virtualHead.next = virtualHead2
        virtualHead2.next = head
        var  node= virtualHead
        var pNode = virtualHead
        var cNode:ListNode? = virtualHead2
        var nNode:ListNode? =head


        while(cNode!=null){
            if(nNode!=null){
                if(pNode.`val` != cNode.`val` &&cNode.`val`!=nNode.`val`){
                    node.next = cNode
                    node =node.next!!
                }
                nNode = nNode.next
            }else{
                if(pNode.`val` != cNode.`val` ){
                    node.next = cNode
                    node =node.next!!
                }
            }
            pNode = cNode
            cNode = cNode.next
        }
        node?.next =null

        return virtualHead2.next

    }
}