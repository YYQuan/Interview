package akotlin
import akotlin.Util.ListNode

fun main() {
    val ints = intArrayOf(1,2,3,4)
    Util.transferListNode4Array(ints).let {
        val code = Code24()
        code.swapPairs(it).let {
            Util.print(it!!)
        }
    }
}
class Code24 {
    fun swapPairs(head: ListNode?): ListNode? {

        if(head == null)  return null
        if(head.next ==null) return head
        val  virtualHead = ListNode(0)
        virtualHead.next = head
        var pNode  = virtualHead
        var cNode = head
        var nNode = head.next

        while(Util.notNull(cNode,nNode){true} == true){
            var tmp = nNode!!.next

            pNode.next = nNode
            nNode.next = cNode
            cNode!!.next = tmp


            pNode = cNode
            cNode = tmp
            tmp?.let {
                nNode = tmp.next
            }
        }
        return virtualHead.next
    }
}