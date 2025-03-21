package BackEnd.User;

import Models.Score;
import java.util.List;

public class LeaderBoardController {
    
    public static List<Score> getTopScores() {
        return ScoreDAO.getTopScores();
    }
}
