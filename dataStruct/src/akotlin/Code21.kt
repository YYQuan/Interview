package akotlin
import akotlin.Util.ListNode

fun main() {
    val ints1 = intArrayOf(1,2,4)
    val ints2 = intArrayOf(1,3,4)
    val node1 = Util.transferListNode4Array(ints1)
    val node2 = Util.transferListNode4Array(ints2)
    val code = Code21()
    code.mergeTwoLists(node1,node2)?.let {
        Util.print(it)
    }
}
class Code21 {
    fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
        val virtualHead = ListNode(Int.MIN_VALUE)
        var node = virtualHead
        var node1 = list1
        var node2 = list2
        while(node1!=null||node2!=null){
            Util.notNull(node1,node2){
                if(node1!!.`val` <node2!!.`val`){
                    node.next = node1
                    node = node.next!!
                    node1 = node1!!.next
                }else{
                    node.next =node2
                    node = node.next!!
                    node2 = node2!!.next
                }
            }?: run {
                Util.notNull(node1) {
                    node.next = node1
                    node = node.next!!
                    node1 = node1!!.next
                }?:run{
                    node.next = node2
                    node = node.next!!
                    node2 = node2!!.next
                }
            }
        }
        return virtualHead.next
    }
}