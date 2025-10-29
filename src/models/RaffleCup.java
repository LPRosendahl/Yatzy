package models;

public class RaffleCup {
    private final Die[] dice = new Die[5];

    public RaffleCup() {
        //TODO: Create an instance of RaffleCup.
        for (int index = 0; index < dice.length; index++) {
            dice[index] = new Die();
        }
    }

    public void throwDice() {
        //TODO: implement throwDice method.
        for (Die die : dice) {
            die.roll();
        }
    }

    public Die[] getDice() {

        return dice;
    }
}
