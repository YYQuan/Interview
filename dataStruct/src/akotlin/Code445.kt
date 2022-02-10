package akotlin
import akotlin.Util.ListNode
import java.util.*

fun main() {
//    Code445().addTwoNumbers(Util.transferListNode4Array(intArrayOf(7,2,4,3)),
//        Util.transferListNode4Array(intArrayOf(5,6,4))).let {
   Code445().addTwoNumbers(Util.transferListNode4Array(intArrayOf(5)),
            Util.transferListNode4Array(intArrayOf(5))).let {
            Util.print(it!!)
        }

}
class Code445 {
    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {

        val virtualHead = ListNode(0)

        val stack = LinkedList<ListNode>()
        val stack2 = LinkedList<ListNode>()
        val stackResult = LinkedList<Int>()

        var node = l1
        while(node!=null){
            stack.addFirst(node)
            node = node.next
        }
        node = l2
        while(node!=null){
            stack2.addFirst(node)
            node = node.next
        }

//        stack.forEach {
//            print("${it.`val`}  ->")
//        }
//        println()
//        stack2.forEach {
//            print("${it.`val`}  ->")
//        }
//        println()
        var flag = false
        while(stack.isNotEmpty()||stack2.isNotEmpty()||flag){

            val node1 =if(stack.isNotEmpty()) stack.removeFirst() else null
            val node2 =if(stack2.isNotEmpty()) stack2.removeFirst() else null
//            println("${node1?.`val`}    ${node2?.`val`}   $flag ")
            var sum  =  (if(node1 == null) 0 else node1.`val`) + (if(node2 ==null) 0 else node2.`val` )+  (if(flag) 1 else 0)

            flag = sum>=10
//            println("sum $sum  $flag")
            sum %= 10

            stackResult.addFirst(sum)


        }

        var noteR:ListNode = virtualHead
        var size = stackResult.size
        while(size-->0){
            val tmp = stackResult.removeFirst()
            noteR.next = ListNode(tmp)
            noteR  = noteR.next!!
        }

        return virtualHead.next
    }
}