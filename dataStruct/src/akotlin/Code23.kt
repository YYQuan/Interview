package akotlin
import akotlin.Util.ListNode

fun main() {
    val ints1 = intArrayOf(1,4,5)
    val ints2 = intArrayOf(1,3,5)
    val ints3 = intArrayOf(2,6)

    val lists = arrayOf(
        Util.transferListNode4Array(ints1),
        Util.transferListNode4Array(ints2),
        Util.transferListNode4Array(ints3))
    val code = Code23()
    code.mergeKLists(lists)?.let {
        Util.print(it)
    }


}
class Code23 {
    fun mergeKLists(lists: Array<ListNode?>): ListNode? {

        if(lists.isEmpty()) return null
        if(lists.size ==1)  return lists[0]

        var list1 = lists[0]

        var index  = 1
        while(index< lists.size){
            val list2 = lists[index]
            list1 = merge2ListNode(list1,list2)
            index++
        }
        return list1
    }

    fun merge2ListNode(list1:ListNode?,list2:ListNode?):ListNode?{

        val virtualHead  = ListNode(0)

        var node = virtualHead
        var node1 = list1
        var node2 = list2

        while(node1!=null||node2!=null){
            Util.notNull(node1,node2){
                if(node1!!.`val`<node2!!.`val`){
                    node.next = node1
                    node = node.next!!
                    node1 = node1!!.next
                }else{
                    node.next = node2
                    node = node.next!!
                    node2 = node2!!.next
                }
            }?: run {
                Util.notNull(node1){
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