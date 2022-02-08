package akotlin

fun main() {
    Code202().isHappy(19).let {
        println(it)
    }
}
class Code202 {
    fun isHappy(n: Int): Boolean {
        val set = HashSet<Int>()

        var tmp = n
        while(true){
            println("tmp $tmp")
            if(tmp ==1) return true
            else {
                var sum = 0
                "$tmp".forEachIndexed { index, c ->
                    println("c  $c  ${c.toInt()}  ${(c.toInt() * c.toInt())}  ${ c.digitToInt()}")
//                    sum   += ((c.toInt()-49) * (c.toInt()-49))
                    sum   += (( "$c".toInt()) * ( "$c".toInt()))

                }
                if(set.contains(sum)){
                    return false
                }
                set.add(sum)

                tmp =sum
            }
        }

        return true
    }
}