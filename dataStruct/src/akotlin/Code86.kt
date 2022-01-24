package akotlin
import akotlin.Util.ListNode

fun main() {
//    val ints = intArrayOf(1,4,3,2,5,2)
    val ints = intArrayOf(1,4,3,2,5,2)
    Code86().partition(Util.transferListNode4Array(ints),3).let {
        it?.let {
            Util.print(it)
        }
    }
}
class Code86 {
    //思路  构造两个新的链表
    // 遍历 和构造新链表互不影响
    fun partition(head: ListNode?, x: Int): ListNode? {
        if(head== null) return null

        val virtualHead = ListNode(Int.MIN_VALUE)
        val virtualHeadL = ListNode(Int.MIN_VALUE)
        val virtualHeadH = ListNode(Int.MIN_VALUE)
        virtualHead.next = head
        var lNode  = virtualHeadL
        var hNode  = virtualHeadH

        var cNode = virtualHead.next
        var nNode = virtualHead.next!!.next


        while(cNode!=null){
            if(cNode.`val` >= x){
                hNode.next = cNode
                hNode = cNode
            }else{
                lNode.next = cNode
                lNode = cNode
            }
            cNode.next =null
            cNode = nNode
            nNode = nNode?.next


        }
        hNode.next = null

        lNode.next = if(virtualHeadH.next != null) virtualHeadH.next  else null

        return virtualHeadL.next

    }
}