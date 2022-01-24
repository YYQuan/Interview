package akotlin

fun main() {
    val ints1 = intArrayOf(4,5,6,0,0,0)
    val ints2 = intArrayOf(1,2,3)
    Code88().merge(ints1,3,ints2,3)
    ints1.forEach {
        print ("$it ")
    }
}
class Code88 {
    fun merge(nums1: IntArray, m: Int, nums2: IntArray, n: Int): Unit {

        var index1 = m-1
        var index2 = n-1
        for(i in nums1.size-1 downTo 0){
            if(index1<0){
                nums1[i] = nums2[index2--]
            }else if(index2<0){
                break
            }else{
                nums1[i] = Math.max(nums1[index1],nums2[index2])
                if(nums1[index1]>nums2[index2]){
                    index1--
                }else{
                    index2--
                }
            }
        }
    }
}