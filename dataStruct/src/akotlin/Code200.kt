package akotlin

import kotlin.math.exp

fun main() {
    Code200().numIslands(arrayOf(
        charArrayOf('1','1','1','1','0'),
        charArrayOf('1','1','0','1','0'),
        charArrayOf('1','1','0','0','0'),
        charArrayOf('0','0','0','0','0')
    )).let {
        println(it)
    }
}
class Code200 {

    fun numIslands(grid: Array<CharArray>): Int {
        if(grid.isEmpty()) return 0

        val isRead = Array(grid.size){BooleanArray(grid[0].size)}
        var result =0
        for(i in grid.indices){
            for(j in grid[i].indices){

                if(isRead[i][j]) continue
                if(grid[i][j] != '1') continue

                expand(grid,isRead,i,j)
//                println("----------------")
                result++
            }
        }
        return result

    }

    private fun expand(grid: Array<CharArray> ,isRead: Array<BooleanArray>,row :Int, coloum:Int) {
//        println(" expand $row  $coloum")
        if(isRead[row][coloum]) return
        isRead[row][coloum] = true
        //左上右下
        if(row>0){
            if(grid[row-1][coloum] =='1'&&!isRead[row-1][coloum]){
//                println(" expand   左  ${row-1}  $coloum")


                expand(grid,isRead,row-1,coloum)
            }
        }
        if(row<grid.size-1){
            if(grid[row+1][coloum] =='1'&&!isRead[row+1][coloum]){
//                println(" expand   右  ${row+1}  $coloum")
                expand(grid,isRead,row+1,coloum)
            }
        }
        if(coloum>0){
            if(grid[row][coloum-1] =='1'&&!isRead[row][coloum-1]){
//                println(" expand   上  ${row}  ${coloum-1}")
                expand(grid,isRead,row,coloum-1)
            }
        }
        if(coloum<grid[0].size-1){
            if(grid[row][coloum+1] =='1'&&!isRead[row][coloum+1]){
//                println(" expand   下  ${row}  ${coloum+1}")
                expand(grid,isRead,row,coloum+1)
            }
        }
    }

}