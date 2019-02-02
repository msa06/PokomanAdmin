package in.pokoman.www.pokemonadmin.Model;

public class Quiz {
    private String qid,qname,qprice,qtime,qvideourl;

    public Quiz() {
    }

    public Quiz(String qid, String qname, String qprice, String qtime, String qvideourl) {
        this.qid = qid;
        this.qname = qname;
        this.qprice = qprice;
        this.qtime = qtime;
        this.qvideourl = qvideourl;
    }

    public String getQid() {
        return qid;
    }

    public String getQname() {
        return qname;
    }

    public String getQprice() {
        return qprice;
    }

    public String getQtime() {
        return qtime;
    }

    public String getQvideourl() {
        return qvideourl;
    }
}
