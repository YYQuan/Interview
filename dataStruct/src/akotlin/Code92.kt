package akotlin
import akotlin.Util.ListNode

fun main() {

//    val ints = intArrayOf(1,2,3,4,5)
    val ints = intArrayOf(5)
    Code92().reverseBetween(Util.transferListNode4Array(ints),1,1).let {
        it?.let {
            Util.print(it)
        }
    }
}
class Code92 {
    fun reverseBetween(head: ListNode?, left: Int, right: Int): ListNode? {

        if(head == null) return null

        val virtualHead = ListNode(0)
        virtualHead.next =head

        var pNode :ListNode= virtualHead
        var lNode :ListNode?= virtualHead.next

        var rNode :ListNode= virtualHead
        var nNode :ListNode?= virtualHead.next

        for( i in 1 until left){
            if(pNode.next ==null) return head
            pNode = pNode.next!!
            lNode = lNode?.next

        }
        for( i in 1..right){
            if(rNode.next == null) return head
            rNode =rNode.next!!
            nNode =nNode?.next
        }

        var p = pNode
        var c = lNode
        var n = lNode?.next

        while(c!=nNode){
            c?.next = p

            p = c!!
            c = n
            n = n?.next

        }
        pNode.next = rNode
        lNode?.next = nNode

        return virtualHead.next
    }
}