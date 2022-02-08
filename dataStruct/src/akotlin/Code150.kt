package akotlin

import java.util.*

fun main() {
//    Code150().evalRPN(arrayOf("2","1","+","3","*")).let {
    Code150().evalRPN(arrayOf("4","13","5","/","+")).let {
        println(it)
    }
}
class Code150 {
    fun evalRPN(tokens: Array<String>): Int {

        val queue = LinkedList<String>()
        tokens.forEach {
            queue.addLast(it)
        }

        val list = LinkedList<Int>()
        while(queue.isNotEmpty()){

            val s = queue.removeFirst()
            if(isSign(s)){
                val op2 = list.removeLast()
                val op1 = list.removeLast()

                when(s){
                     "+" ->  queue.addFirst(""+ (op1+op2)  )
                    "-" ->    queue.addFirst(""+ (op1-op2)  )
                    "*" ->     queue.addFirst(""+ (op1*op2)  )
                    "/" -> queue.addFirst(""+ (op1/op2)  )
                    else -> throw IllegalStateException()
                }
            }else{
                list.add(s.toInt())
            }

        }
        return list[0]
    }

    fun isSign(s:String):Boolean{
        return s=="+"||s=="-"||s=="*"||s=="/"
    }

}