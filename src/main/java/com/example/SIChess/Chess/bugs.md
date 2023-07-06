1. When dragging piece it shifts this piece one square up and left
   For example: piece on e4 -> d3
2. Sometimes it can move 2+ pieces in move, instead of 1.

--- This bugs are solved! :) ---

3. If mouse while dragging gets out of layeredPane there can be Null exception

4. Make all поля private - done

md format

make hashmap for exceptions in main

кінцевий автомат(state machine)

logs instead of prints

few public methods in classes, many private if there is a need

make separate method for every if-condition

Constructor cannot throw Exceptions


### Logic for a pawn to kill:

1. Pawn must be moving for 1 row forward
2. Pawn must be moving to neighbour file
3. There must be an opponent piece
4. There must be no check

Update for Square:
create method WhatPieceColorHasThisSquare()