package akotlin

class Util {

    companion object{


        inline fun <R>  allNull(vararg args:Any?,  block :(()->R))= when{
            args.filterNotNull().size ==0 -> block()
            else -> null
        }
        inline fun <R>  hasNull(vararg args:Any?,  block :(()->R))= when{
            args.filterNotNull().size ==args.size -> null
            else -> block()
        }


        inline fun <R>  notAllNull(vararg args:Any?,  block :(()->R))= when{
            args.filterNotNull().isNotEmpty() -> block()
            else -> null

        }
        inline fun <R>  notNull(vararg args:Any?,  block :(()->R))= when{
            args.filterNotNull().size ==args.size -> block()
            else -> null

        }


        fun transferListNode4Array( array:IntArray):ListNode{
            if(array.isEmpty()) throw IllegalStateException();
            val virtualHead = ListNode(Int.MIN_VALUE)
            var node:ListNode? = virtualHead

            array.forEach {
                node?.next = ListNode(it)
                node = node?.next
            }
            return virtualHead.next!!
        }

        fun print(node :ListNode){
            var n:ListNode? = node
            val s = StringBuilder()
            while(Util.notNull(n){true}?:false){
                s.append("${n?.`val`} ->")
                n?.let {
                    n = n?.next
                }
            }
            println("listNode : $s")

        }
    }


    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }

}