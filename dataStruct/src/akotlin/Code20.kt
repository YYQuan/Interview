package akotlin

import com.sun.org.apache.bcel.internal.classfile.Code
import java.util.*

fun main() {
    val code =Code20()
    println(code.isValid("{"))
}
class Code20 {
    fun isValid(s: String): Boolean {
        if (s.isEmpty()) return true
        val stack = Stack<Char>()
        s.forEach {
            if(isLeftSign(it)){
                stack.push(it)
            }else if (isRightSign(it)){
                if(stack.isEmpty()) return false
                val isAdapte = isAdapter(stack.peek(),it)
//                println(" isAdapte $isAdapte  it $it ${stack.peek()} ")
                if(!isAdapte) return false
                else stack.pop()
            }
        }
        stack.forEach {
            print("$it ")
        }
        return stack.isEmpty()
    }

    fun isLeftSign(c:Char):Boolean{
        return when(c){
            '(' -> true
            '{' -> true
            '[' -> true
            else -> false
        }
    }
    fun isRightSign(c:Char):Boolean{
        return when(c){
            ')' -> true
            ']' -> true
            '}' -> true
            else -> false
        }
    }

    fun isAdapter(left:Char,right:Char):Boolean{
        return when(left){
            '(' -> {
                return right == ')'
            }
            '{' -> {
                return right == '}'
            }
            '[' -> {
                return right == ']'
            }
            else -> false
        }
    }

}