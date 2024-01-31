package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;
    public ChessGame() {
        this.teamTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece current = board.getPiece(startPosition);
        Collection<ChessMove> valid = current.pieceMoves(board,startPosition);
        if(valid == null){
            return valid;
        }
        else{
            //clone board
            ChessBoard clone = board;
            //try each potential move
            for(ChessMove posMove:valid){
                //look at end position of the chess move
                //save the piece at that position
                // call add and get piece from ChessBoard
                // put piece at start at endposition
                // put null piece at start position
                //isinCheck()
                // if true, not a valid move
                //if false then it is a valid move so add it
                //then reverse the move and put it back

            }


        }

       return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //loop through all the other player's moves
        Collection<ChessPosition> checkMoves = new ArrayList<>();
        ChessPiece myKing = null;
        ChessPosition kingPostion = null;
        for (int i = 1; i <=8; i ++){
            for(int j = 1; j <= 8; j++){
                ChessPiece currentPiece = board.getPiece(new ChessPosition(i,j));
                if(currentPiece != null){
                    TeamColor posColor = currentPiece.getTeamColor();
                    if(teamColor != posColor){
                        //call piece moves && add all possible moves to a collection
                        Collection<ChessMove> posmoves = currentPiece.pieceMoves(board,new ChessPosition(i,j));
                        for (ChessMove move: posmoves){
                            checkMoves.add(move.getEndPosition());
                        }
                    }
                    else{
                        ChessPiece.PieceType posKing = currentPiece.getPieceType();
                        if(posKing == ChessPiece.PieceType.KING){
                            myKing = currentPiece;
                            kingPostion = new ChessPosition(i,j);
                        }
                    }
                }
            }
        }
        //check to see if any moves have an end position where your king is
        if (kingPostion != null) {
            boolean isChecked = checkMoves.contains(kingPostion);
            if(isChecked){
                return true;
            }
            else{
                return false;
            }
        }



        return true;
    }

    public void findPieces(Collection<ChessPosition> posMoves, TeamColor teamColor){
        for (int i = 1; i <=8; i ++){
            for(int j = 1; j <= 8; j++){
                ChessPiece currentPiece = board.getPiece(new ChessPosition(i,j));
                if(currentPiece != null){
                    TeamColor posColor = currentPiece.getTeamColor();
                    if(teamColor == posColor){
                        //call piece moves && add all possible moves to a collection
                        Collection<ChessMove> posmoves = currentPiece.pieceMoves(board,new ChessPosition(i,j));
                        for(ChessMove move: posmoves){
                            Collection<ChessPosition> myPieces =
                        }
                        ChessPiece.PieceType posKing = currentPiece.getPieceType();
                        if(posKing == ChessPiece.PieceType.KING){
                            ChessPiece myKing = currentPiece;
                            ChessPosition kingPostion = new ChessPosition(i,j);
                        }
                    }
                }
            }
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean check = isInCheck(teamColor);
        Collection<ChessMove> valid = validMoves(board, );
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // for each piece on the board that's your color
        // valid moves
        // if null, return true;
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
