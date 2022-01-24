package akotlin

import kotlin.system.exitProcess

fun main() {
    val chars = arrayOf( charArrayOf('A','B','C','E'),charArrayOf('S','F','C','S'),charArrayOf('A','D','E','E'))
    Code79().exist(chars,"ABCCED").let {
        println(it)
    }
}
class Code79 {
    fun exist(board: Array<CharArray>, word: String): Boolean {
        val isRead  = Array(board.size){BooleanArray(board.first().size){false} }
        board.forEachIndexed { index, chars ->
            chars.forEachIndexed { indexC, c ->
                if(c == word.first()){
                    isRead[index][indexC]=true
                    exist(board,isRead,index,indexC,1,word).let {
                        if(it) return true
                    }
                    isRead[index][indexC]=false

                }
            }
        }
        return false

    }
    fun exist(board: Array<CharArray>, isRead:Array<BooleanArray>,row:Int,col:Int,index:Int,word: String): Boolean {
        if(index == word.length) return true

        // 左上右下
        val comC= word[index]
        if(col!=0) {
            if(!isRead[row][col-1]) {
                val c = board[row][col - 1]
                if (c == comC) {
                    isRead[row][col-1] = true
                    exist(board, isRead, row, col - 1, index + 1, word).let {
                        if (it) {
                            return true
                        }
                    }
                    isRead[row][col-1] = false
                }
            }
        }
        if(row!=0) {
            if(!isRead[row-1][col]) {
                val c = board[row - 1][col]
                if(c == comC){
                    isRead[row-1][col] = true
                    exist(board,isRead,row-1,col,index+1,word).let {
                        if(it) return true
                    }
                    isRead[row-1][col] = false
                }
            }
        }
        if(col<board.first().size-1) {
            if(!isRead[row][col+1]) {
                val c = board[row][col + 1]
                if(c==comC){
                    isRead[row][col+1]= true
                    exist(board,isRead,row,col+1,index+1,word).let {
                        if(it) return true
                    }
                    isRead[row][col+1]= false
                }
            }
        }
        if(row<board.size-1) {
            if(!isRead[row+1][col]) {
                val c = board[row + 1][col]
                if(c == comC){
                    isRead[row+1][col] =true
                    exist(board,isRead,row+1,col,index+1,word).let {
                        if(it) return true
                    }
                    isRead[row+1][col] =false
                }
            }
        }


        return false

    }
}