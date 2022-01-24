package akotlin.sort

fun main() {
    var ints = intArrayOf(5,1,2,3)
    QuickSort.sort(ints)

    ints.forEach {
        print("$it ,")
    }
}
class QuickSort {
    companion object {
        fun sort(ints:IntArray){

            sort(ints,0,ints.size-1)
        }
        fun sort(ints:IntArray,start:Int,end:Int){

            if(start>= end ) return

            var mid = findMiddle(ints,start,end)
            sort(ints,start,mid-1)
            sort(ints,mid+1,end)
        }

        fun findMiddle(ints:IntArray,start:Int,end:Int):Int{
            var base = ints[start]
            var l = start +1
            var r = end

            while(l<r){

                while(l<r&&ints[l]<= base){
                    l++
                }

                while(start<r&&ints[r]>=base){
                    r--
                }

                if(l<r){
                    swap(ints,l,r)
                }

            }
            swap(ints,start,r)
            return r
        }

        fun swap(ints:IntArray, l:Int, r:Int){
            var tmp = ints[l]
            ints[l] = ints[r]
            ints[r] = tmp
        }
    }


}