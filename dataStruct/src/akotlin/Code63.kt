package akotlin

fun main() {
    val intss = arrayOf(intArrayOf(0,0,0),intArrayOf(0,1,0),intArrayOf(0,0,0))
    Code63().uniquePathsWithObstacles(intss).let {
        println(it)
    }

}
class Code63 {

    fun uniquePathsWithObstacles(obstacleGrid: Array<IntArray>): Int {
        var board = Array(obstacleGrid.size){Array(obstacleGrid.first().size){0} }

        for(i in 0 until obstacleGrid.first().size){
            board[0][i] = if(obstacleGrid[0][i] == 1)  0  else {  if (i>0) board[0][i-1] else 1 }
        }

        for(i in 1 until obstacleGrid.size){
            for (j in obstacleGrid.first().indices){

                if(obstacleGrid[i][j]==1) board[i][j]=0
                else if(j == 0 ) board[i][j] = board[i-1][j]
                else board[i][j] = board[i-1][j]+board[i][j-1]
            }
        }

        return board[obstacleGrid.size-1][obstacleGrid.first().size-1]
    }
}