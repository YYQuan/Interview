package akotlin

fun main() {


    val board = arrayOf(
        charArrayOf('5','3','.','.','7','.','.','.','.'),
        charArrayOf('6','.','.','1','9','5','.','.','.'),
        charArrayOf('.','9','8','.','.','.','.','6','.'),
        charArrayOf('8','.','.','.','6','.','.','.','3'),
        charArrayOf('4', '.', '.', '8', '.', '3', '.', '.', '1'),
        charArrayOf('7', '.', '.', '.', '2', '.', '.', '.', '6'),
        charArrayOf('.', '6', '.', '.', '.', '.', '2', '8', '.'),
        charArrayOf('.', '.', '.', '4', '1', '9', '.', '.', '5'),
        charArrayOf('.', '.', '.', '.', '8', '.', '.', '7', '9')
    )

//    val board = arrayOf(
//    charArrayOf('5', '3', '4', '6', '7', '8', '9', '1', '2'),
//    charArrayOf('6', '7', '2', '1', '9', '5', '3', '4', '8'),
//    charArrayOf('1', '9', '8', '3', '4', '2', '5', '6', '7'),
//    charArrayOf('8', '5', '9', '7', '6', '1', '4', '2', '3'),
//    charArrayOf('4', '2', '6', '8', '5', '3', '7', '9', '1'),
//    charArrayOf('7', '1', '3', '9', '2', '4', '8', '5', '6'),
//    charArrayOf('9', '6', '1', '5', '3', '7', '2', '8', '4'),
//    charArrayOf('2', '8', '7', '4', '1', '9', '6', '3', '5'),
//    charArrayOf('3', '4', '5', '2', '8', '6', '1', '7', '9'))



    val code =Code37()

    code.solveSudoku(board)
    board.forEach {
        println()
        it.forEach {c->
            print("$c ")
        }
    }
}
class Code37 {
    fun solveSudoku(board: Array<CharArray>): Unit {
        solveSudoku(board,0,-1)
    }

    fun solveSudoku(board: Array<CharArray>,row:Int,column: Int):Boolean {

        var count =0
        board.forEach B@{
            it.forEach {
                c ->
                if(c == '.') {
                    count++
                    return@B
                }
            }
        }
        if(count ==0) return true

        for(r in row ..8){
            for(c in 0 .. 8){
                if(r == row && c <=column) continue

                if(board[r][c]=='.'){
                    for( i in 1..9){
                        val legal = legal(board,r,c,'0'+i)
                        if(legal) {
                            board[r][c] = '0' + i
                            var result =solveSudoku(board, r, c)
                            if(result){
                                return true
                            }
                            board[r][c] = '.'
                        }
                    }
                    // 这里容易忘记 没有可用的数字后 直接回退
                    board[row][column] = '.'
                    return false
                }

            }
        }
        return false
    }


    fun legal(board: Array<CharArray>,row:Int,column:Int,target :Char):Boolean {
        return legalOnBoard(board,row,column,target)&&legalOnColumn(board,column,target)&&legalOnRow(board,row,target)
    }

    fun  legalOnColumn(board: Array<CharArray>,column:Int,target :Char):Boolean {
        for(i in 0..8){
            if(board[i][column] == target) return false
        }
        return true
    }
    fun  legalOnRow(board: Array<CharArray>,row:Int,target :Char):Boolean{
        for(i in 0..8){
            if(board[row][i] == target) return false
        }
        return true
    }
    fun  legalOnBoard(board: Array<CharArray>,row:Int,column:Int,target :Char):Boolean {
        var r = row/3
        var c = column/3

        for( i in r*3..r*3+2){
            for( j in c*3..c*3+2){
                    if(board[i][j] == target) return false
            }

        }
        return true
    }


}