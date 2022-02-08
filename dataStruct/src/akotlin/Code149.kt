package akotlin

fun main() {
//    val a1 = intArrayOf(1,1)
//    val a2 = intArrayOf(2,2)
//    val a3 = intArrayOf(3,3)
//    val array = arrayOf(a1,a2,a3)
//    Code149().maxPoints(array).let {
//        println(it)
//    }

//    val a1 = intArrayOf(1,1)
//    val a2 = intArrayOf(3,2)
//    val a3 = intArrayOf(5,3)
//    val a4 = intArrayOf(4,1)
//    val a5 = intArrayOf(2,3)
//    val a6 = intArrayOf(1,4)
//    val array = arrayOf(a1,a2,a3,a4,a5,a6)
//    Code149().maxPoints(array).let {
//        println(it)
//    }

    val a1 = intArrayOf(2,3)
    val a2 = intArrayOf(3,3)
    val a3 = intArrayOf(-5,3)
    val array = arrayOf(a1,a2,a3)
    Code149().maxPoints(array).let {
        println(it)
    }
}

class Code149 {
    fun maxPoints(points: Array<IntArray>): Int {

        if(points.size<=2) return points.size
        var result = 0
        val map = HashMap<Double,Int>()

        for(i in points.indices){
            var samePoint = 1
            for(j in i+1 until points.size){

                val x = points[i][0]
                val y = points[i][1]

                val x2 = points[j][0]
                val y2 = points[j][1]
                var double: Double = 0.0
                if(x==x2 &&y==y2){
                    samePoint++
                    continue
                }
                double = if(x == x2){
                    Double.MAX_VALUE
                }else if (y == y2) {
                    // double中 有-0.0 和0.0的区别
                    0.0
                }else {
                    ((y - y2)*1000.0 / (x - x2)*1000.0)
                }
                println(double)
                map[double] = map.getOrDefault(double,1)+samePoint
            }
            map.values.forEach {
                result = Math.max(it,result)
            }
            map.clear()
        }

        return result
    }
}