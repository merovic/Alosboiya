package alosboiya.jeddahwave.Models;



public class SuggestionItems {
    private int sugimg;
    private String sugdis , suglocatoin ;

    public SuggestionItems(int sugimg, String sugdis, String suglocatoin) {
        this.sugimg = sugimg;
        this.sugdis = sugdis;
        this.suglocatoin = suglocatoin;
    }

    public int getSugimg() {
        return sugimg;
    }

    public void setSugimg(int sugimg) {
        this.sugimg = sugimg;
    }

    public String getSugdis() {
        return sugdis;
    }

    public void setSugdis(String sugdis) {
        this.sugdis = sugdis;
    }

    public String getSuglocatoin() {
        return suglocatoin;
    }

    public void setSuglocatoin(String suglocatoin) {
        this.suglocatoin = suglocatoin;
    }
}
