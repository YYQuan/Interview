package akotlin
import akotlin.Util.*

fun main() {
    val ints = intArrayOf(1,2,3,4,5)
    val node = Util.transferListNode4Array(ints)
    Util.print(node)
    val code =Code19()
    code.removeNthFromEnd(node,2)
    Util.print(node)
}
class Code19 {
    fun removeNthFromEnd(head: ListNode?, n: Int): ListNode? {
        val virtualHead = ListNode(0)
        virtualHead.next = head

        var nodeL:ListNode? = head
        var nodeR:ListNode? = head
        var nodeP = virtualHead

        var i = n-1
        while(i >0){
            i--
            nodeR?.let {
                nodeR  = it.next
            }?: throw IllegalStateException()
        }

        while(true){
            if(nodeR!!.next !=null){
                nodeL = nodeL!!.next
                nodeR = nodeR!!.next
                nodeP = nodeP.next!!
            }else break
        }

        nodeP.next = nodeL!!.next

        return virtualHead.next

    }
}