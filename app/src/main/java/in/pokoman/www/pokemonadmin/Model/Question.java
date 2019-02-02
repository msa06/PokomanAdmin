package in.pokoman.www.pokemonadmin.Model;

public class Question {
    private String ques,ans,op1,op2,op3,op4;

    public Question() {
    }

    public Question(String ques, String ans, String op1, String op2, String op3, String op4) {
        this.ques = ques;
        this.ans = ans;
        this.op1 = op1;
        this.op2 = op2;
        this.op3 = op3;
        this.op4 = op4;
    }

    public String getQues() {
        return ques;
    }

    public String getAns() {
        return ans;
    }

    public String getOp1() {
        return op1;
    }

    public String getOp2() {
        return op2;
    }

    public String getOp3() {
        return op3;
    }

    public String getOp4() {
        return op4;
    }
}
