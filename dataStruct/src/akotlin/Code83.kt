package akotlin
import akotlin.Util.ListNode

fun main() {
    val ints = intArrayOf(1,1,2)
    Code83().deleteDuplicates(Util.transferListNode4Array(ints)).let {
        it?.let {
            Util.print(it)
            println(it)
        }
    }
}
class Code83 {
    fun deleteDuplicates(head: ListNode?): ListNode? {

        if(head == null) return null
        val virtualHead = ListNode(Int.MIN_VALUE)
        virtualHead.next = head

        var node   =virtualHead
        var pNode = virtualHead
        var cNode = head
        var nNode = head.next

        while(cNode!=null){

            while(pNode.`val` ==cNode?.`val`){
                pNode = pNode.next!!
                cNode =cNode?.next
                nNode = nNode?.next
                if(cNode == null){
                    node.next =null
                    return virtualHead.next
                }
            }
            node.next = cNode
            node = node.next!!

            pNode = pNode.next!!
            cNode =cNode?.next
            nNode = nNode?.next
        }
        node.next =null
        return virtualHead.next
    }
}