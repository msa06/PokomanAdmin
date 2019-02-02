package in.pokoman.www.pokemonadmin.Model;

public class QuizStatus {
    private String live,showques,curques;

    public QuizStatus() {
    }

    public QuizStatus(String live, String showques, String curques) {
        this.live = live;
        this.showques = showques;
        this.curques = curques;
    }

    public String getLive() {
        return live;
    }

    public String getShowques() {
        return showques;
    }

    public String getCurques() {
        return curques;
    }
}
