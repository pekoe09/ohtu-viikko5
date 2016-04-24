package ohtu;

import java.util.HashMap;

public class TennisGame {
    
    private int player1Score = 0;
    private int player2Score = 0;
    private String player1Name;
    private String player2Name;
    private HashMap<Integer, String> scoreName;

    public TennisGame(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        initializeScoreNames();
    }
    
    private void initializeScoreNames() {
        scoreName = new HashMap<Integer, String>();
        scoreName.put(0, "Love");
        scoreName.put(1, "Fifteen");
        scoreName.put(2, "Thirty");
        scoreName.put(3, "Forty");
        scoreName.put(4, "Win");        
    }

    public void wonPoint(String playerName) {
        if (playerName.equals(player1Name))
            player1Score += 1;
        else
            player2Score += 1;
    }

    public String getScore() {
        String score = "";
        
        if (player1Score==player2Score)
        {
            score = getEvenScore();
        }
        
        else if (player1Score>=4 || player2Score>=4)
        {
            score = getEndGameScore();
        }
        else
        {
            score = getRegularScore();
        }
        return score;
    }
    
    private String getEvenScore() {
        if(player1Score < 4)
            return scoreName.get(player1Score) + "-All";
        else
            return "Deuce";  
    }
    
    private String getEndGameScore() {
        int minusResult = player1Score-player2Score;
        if (minusResult==1) 
            return "Advantage player1";
        else if (minusResult ==-1) 
            return "Advantage player2";
        else if (minusResult>=2) 
            return "Win for player1";
        else 
            return "Win for player2";
    }
    
    private String getRegularScore() {
        return scoreName.get(player1Score) + "-"+ scoreName.get(player2Score);
    }
}