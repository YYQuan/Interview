package akotlin

fun main() {
    val array = arrayOf(intArrayOf(1,3,1),intArrayOf(1,5,1),intArrayOf(4,2,1))
    Code64().minPathSum(array).let {
        println(it)
    }
}
class Code64 {
    fun minPathSum(grid: Array<IntArray>): Int {
        val board = Array(grid.size){Array(grid.first().size){0} }
        for(i in 0 until grid.first().size){
            board[0][i] = grid.first()[i]+if(i == 0 )   0  else board[0][i-1]
        }
        for( i in 1 until grid.size){
            for (j in 0 until grid.first().size){

                if( j ==0 ){
                    board[i][j] = board[i-1][j]+grid[i][j]
                }else {
                    board[i][j] = Math.min(board[i][j-1],board[i-1][j])+grid[i][j]
                }
            }
        }
        return board[board.size-1][board.first().size-1]
    }
}