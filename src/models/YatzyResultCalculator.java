package models;

/**
 * Used to calculate the score of throws with 5 dice
 */
public class YatzyResultCalculator {

    private final Die[] dice;

    /**
     *
     * @param dice
     */
    public YatzyResultCalculator(Die[] dice) {
        //TODO: implement YatzyResultCalculator constructor.
        this.dice = dice;
    }

    /**
     * Calculates the score for Yatzy uppersection
     * @param eyes eye value to calculate score for. eyes should be between 1 and 6
     * @return the score for specified eye value
     */
    public int upperSectionScore(int eyes) {
        //TODO: Implement upperSectionScore method.
        int sum = 0;
        for (Die d : dice) {
            if (d.getEyes() == eyes){
                sum += eyes;
            }
        }
        return sum;
    }

    //Dette gør at man kan se bort fra 0 indeksering og bare bruge 1 - 6.
    private int[] countEyes(){
        int[] counts = new int[7];
        for (Die d : dice) {
            counts[d.getEyes()]++;
        }
        return counts;
    }

    public int onePairScore() {
        //TODO: implement onePairScore method.
        int[] c = countEyes();
        for (int index = 6; index >= 1 ; index--) {
            if (c[index] >= 2) return index * 2;
        }
        return 0;
    }


    public int twoPairScore() {
        //TODO: implement twoPairScore method.

        int[] c = countEyes();
        int pairs = 0;
        int score = 0;
        for (int index = 6; index >=1 ; index--) {
            if (c[index] >= 2){
                pairs++;
                score += index * 2;
                if (pairs == 2) return score;
            }

        }
        return 0;
    }

    public int threeOfAKindScore() {
        //TODO: implement threeOfAKindScore method.
        int[] c = countEyes();
        for (int index = 6; index >= 1 ; index--) {
            if (c[index] >= 3) return index * 3;

        }
        return 0;
    }

    public int fourOfAKindScore() {
        //TODO: implement fourOfAKindScore method.
        int[] c = countEyes();
        for (int index = 6; index >= 1 ; index--) {
            if (c[index] >= 4) return index * 4;

        }
        return 0;
    }

    public int smallStraightScore() {
        //TODO: implement smallStraightScore method.
        int[] c = countEyes();
        for (int index = 1; index <= 5; index++) {
            if (c[index] != 1) return 0;
        }
        return 15;
    }

    public int largeStraightScore() {
        //TODO: implement largeStraightScore method.
        int[] c = countEyes();
        for (int index = 2; index <= 6; index++) {
            if (c[index] != 1) return 0;
        }
        return 20;
    }

    public int fullHouseScore() {
        //TODO: implement fullHouseScore method.
        int[] c = countEyes();
        int three = 0, two = 0;
        for (int index = 1; index <= 6 ; index++) {
            if (c[index] == 3) three = index * 3;
            if (c[index] == 2) two = index * 2;
            }
        return (three > 0 && two > 0) ? three + two : 0;
    }



    public int chanceScore() {
        //TODO: implement chanceScore method.
        int sum = 0;
        for (Die d : dice) sum += d.getEyes();
            return sum;

    }

    public int yatzyScore() {
        //TODO: implement yatzyScore method.
        int first = dice[0].getEyes();
        for (Die d : dice) {
            if (d.getEyes() != first) return 0;

        }
        return 50;
    }
}
