package com.nedogeek.holdem.dealer;

import com.nedogeek.holdem.gamingStuff.Desk;
import com.nedogeek.holdem.gamingStuff.Player;

/**
 * User: Konstantin Demishev
 * Date: 05.10.12
 * Time: 22:02
 */
public class Dealer implements Runnable {
    private final Desk desk;

    private final MoveManager moveManager;
    private final NewGameSetter newGameSetter;
    private final PlayersManager playersManager;
    private final GameCycleManager gameCycleManager;
    private final EndGameManager endGameManager;

    Dealer(Desk desk) {
        this.desk = desk;
        playersManager = new PlayersManager(this);
        moveManager = new MoveManager(this, playersManager);
        newGameSetter = new NewGameSetter(desk, playersManager, moveManager);
        gameCycleManager = new GameCycleManager(this, playersManager);
        endGameManager = new EndGameManager(desk, playersManager);
    }

    Dealer(Desk deskMock, MoveManager moveManagerMock, NewGameSetter newGameSetterMock,
           PlayersManager playersManagerMock, GameCycleManager gameCycleManagerMock,
           EndGameManager endGameManagerMock) {
        desk = deskMock;
        moveManager = moveManagerMock;
        newGameSetter = newGameSetterMock;
        playersManager = playersManagerMock;
        gameCycleManager = gameCycleManagerMock;
        endGameManager = endGameManagerMock;
    }

    public void run() {
        //TODO not codded yet
    }

    void tick() {
        switch (desk.getGameStatus()) {
            case READY:
                gameCycleManager.prepareNewGameCycle();
                break;
            case STARTED:
                makeGameAction();
                break;
            case CYCLE_ENDED:
                gameCycleManager.endGameCycle();
                break;
        }
    }

    private void makeGameAction() {
        switch (desk.getGameRound()) {
            case INITIAL:
                newGameSetter.setNewGame();
                break;
            case FINAL:
                endGameManager.endGame();
                break;
            default:
                if (playersManager.hasAvailableMovers()) {
                    moveManager.makeMove(playersManager.getMover());
                } else {
                    desk.setNextGameRound();
                }
                break;
        }
    }

    public void setGameStarted() {

    }

    public boolean riseNeeded(Player player) {
        return false;
    }

    public void addToPot(int betValue) {

    }

    public int getCallValue() {
        return 0;
    }

    public void setCallValue(int playerBet) {

    }
}
