package akotlin
import akotlin.Util.ListNode

fun main() {

    Util.transferListNode4Array(intArrayOf(4,2,1,3)).let {
        Util.print(it!!)

        Code147().insertionSortList(it).let {
            Util.print(it!!)
        }
//        Util.print(it!!)
    }
}
class Code147 {
    fun insertionSortList(head: ListNode?): ListNode? {
        if(head == null) return head
        val virtualHead = ListNode(Int.MIN_VALUE)
        virtualHead.next = head

        var  p1Node = virtualHead
        var c1Node = head
        var n1Node = head.next

        while(c1Node!=null){
            if(c1Node.`val`<p1Node.`val`){
//               注意 virtualHead的next 不一定是head了
                var p2Node = virtualHead
                var c2Node = virtualHead.next
                var n2Node = c2Node!!.next
//
                while(c1Node.`val`>=c2Node!!.`val`){
                    c2Node = c2Node.next
                    p2Node =p2Node.next!!
                    n2Node = n2Node?.next
                }
//                println("p2Node ${p2Node.`val`}  c2Node ${c2Node.`val`}   c1 ${c1Node.`val`}")
                p2Node.next = c1Node
                c1Node.next = c2Node

                p1Node.next =n1Node
                c1Node =n1Node
                n1Node = n1Node?.next



            }else{
                p1Node = p1Node.next!!
                c1Node = c1Node.next
                n1Node = n1Node?.next
            }
//            Util.print(virtualHead)

        }

        return virtualHead.next
    }
}