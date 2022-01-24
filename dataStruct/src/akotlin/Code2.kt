package akotlin
import akotlin.Util.ListNode
fun main(args: Array<String>) {

    var l1 = intArrayOf(9,9,9,9,9,9,9)
    var l2 = intArrayOf(9,9,9,9)
    val code = Code2()
    var node1 = Util.transferListNode4Array(l1)
    var node2 = Util.transferListNode4Array(l2)
    val result = code.addTwoNumbers_2(null,null)
    Util.print(node1!!)
    Util.print(node2!!)
    result?.let {
        Util.print(result)
    }?: println("result is null")
    println("over")
}


class Code2 {


    fun addTwoNumbers(l1: ListNode?, l2: ListNode?): ListNode? {
        Util.allNull(l1,l2){return null}
        Util.allNull(l1){return l2}
        Util.allNull(l2){return l1}
        var count = 0
        println("count :${count++}")
        val virtualHead = ListNode(Int.MIN_VALUE);
        var node :ListNode? = virtualHead;
        var  flag = false ;// 是否进位了

        var  pNode = l1
        var  qNode  = l2

        println("count :${count++}  $pNode $qNode   ${Util.allNull(pNode,qNode){}}")

        while((Util.notAllNull(pNode,qNode){ true}?:flag) ){
//            println("count :${count++}  $pNode $qNode  ")
            var pValue = pNode?.`val` ?: 0
            var qValue = qNode?.`val` ?: 0
            var sum = pValue +qValue + (if(flag) 1 else 0)
            flag = sum /10 >0
            node?.next = ListNode(sum%10)
            pNode?.let {
                pNode = pNode?.next
            }
            qNode?.let {
                qNode = qNode?.next
            }
            node?.let {
                node = node?.next
            }
            println("count :${count++}  ${pNode?.`val`} ${qNode?.`val`}  ")
        }
        return virtualHead.next
    }


    fun addTwoNumbers_2(l1: ListNode?, l2: ListNode?): ListNode?{
        Util.allNull(l1,l2){return null}
        Util.hasNull(l1){return l2}
        Util.hasNull(l2){return l1}

        val virtualHead = ListNode(Int.MIN_VALUE)
        var node :ListNode? = virtualHead

        var pNode = l1;
        var qNode = l2;

        var map = mutableMapOf<Int,Int>()
        var count = 0;
        while(Util.notNull(pNode){true} == true){
            pNode?.let {
                map[count++] = pNode?.`val`?:0
                pNode = pNode?.next
            }
        }
        count = 0;
        while(Util.notNull(qNode){true} == true){
            qNode?.let {
                val sum = (( qNode!!.`val`?:0) +map.getOrDefault(count,0))
                map[count] = sum%10
                if(sum/10 >0){
                    // 这步 可能会导致 map[count+1]>10
                    map[count+1] = map.getOrDefault(count+1,0) +1
                }
                qNode = qNode!!.next
                count++
            }
        }

        count = 0 ;
        while( true){

            map[count]?.let {
                if(map[count]!! >=10){
                    map[count] = map[count]!!%10
                    map[count+1] = map.getOrDefault(count+1,0)+1
                }
                node!!.next = ListNode(map[count]!!)

                node = node!!.next
                count++
            }?:break

        }
        return virtualHead.next
    }
}
