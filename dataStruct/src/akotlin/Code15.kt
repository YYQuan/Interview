package akotlin

fun main() {
//    var ints = intArrayOf(-1,0,1,2,-1,-4)
//    var ints = intArrayOf(0,0,0)
//    var ints = intArrayOf(-4,-2,1,-5,-4,-4,4,-2,0,4,0,-2,3,1,-5,0)
    var ints = intArrayOf(0,0,0,0,0,0,0)
    val code = Code15()
    code.threeSum(ints).forEach {
        println()
        it.forEach {c -> print("$c ," ) }
    }
}

class Code15 {

    // 思路 ： 问题转化为 在一个数组里找两个数的和是target
    // 难点是去重, 去重 通过   判断当前只是不是target的一半 和 只处理 最左 或者最右
    fun threeSum(nums: IntArray): List<List<Int>> {
        Util.notNull(nums){}?: return  ArrayList()
        nums.sort()

        var result = ArrayList<List<Int>>()
        var l = 0
        while(l<=nums.size-3&&nums[l]<=0){
//            println( "threeSum :  l $l ${nums[l]}")
            val target = - nums[l]
            twoSUm(nums,l+1,target).forEach {  a ->
                result.add(arrayListOf<Int>().let { b ->
                    b.addAll(a)
                    b.add(nums[l])
                    b
                })
            }
            // 找到 和上次不一样 元素
            do {
                l++
            }while(l<=nums.size-3&&nums[l] == nums[l-1])
        }
        return result
    }
    
    fun twoSUm(nums:IntArray,start:Int,target:Int):List<List<Int>>{
        var result = arrayListOf<List<Int>>()
        var l = start
        var r = nums.size-1
//        println("-->  l  :$l r : $r")
        while (l <r){
//            println(" target $target l  :$l ${nums[l]} r : $r  ${nums[r]}")
            // 相同的值 只取最右边那个
            nums[l+1]?.let {intParam ->
                // 如果是 目标值的一半的话 就要考虑 两个相同值
                if(nums[l+1] == nums[l]&&nums[l+1]+nums[l+1] == target) {
                    result.add(arrayListOf<Int>().let {
                        it.add(intParam)
                        it.add(intParam)
                        it
                    })
                }
                while ( l<r && nums[l + 1] == nums[l]) {
                    l++
                }
            }
            //          经过过滤 有可能l  已经大于等于 r了 ,判断在这 避免 nums[l] nums[r] 的值相同 引起重复
            if(l>=r) break;

            // 相同的值 只取最左边那个
            nums[r-1]?.let {intParam ->
                if(nums[r-1] == nums[r] &&nums[r]+nums[r] ==target ){
                    result.add(arrayListOf<Int>().let {
                        it.add(intParam)
                        it.add(intParam)
                        it
                    })

                }
                while (r>l && nums[r-1]==nums[r]){
                    r--
                }
            }
//            println("2 target $target l  :$l ${nums[l]} r : $r  ${nums[r]}")
//          经过过滤 有可能l  已经大于等于 r了
            if(l>=r) break;

            val sum = nums[l]+nums[r]
            if(sum == target){
                result.add(arrayListOf<Int>().let {
                    it.add(nums[l])
                    it.add(nums[r])
                    it
                })
                r--
                l++
            }else if(sum >target){
                r--
            }else{
                l++
            }

        }

        return result
    }

}

