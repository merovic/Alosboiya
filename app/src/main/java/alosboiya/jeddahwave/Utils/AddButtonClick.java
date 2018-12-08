package alosboiya.jeddahwave.Utils;


public class AddButtonClick {

    String event;
    String event2;

    public AddButtonClick(String event) {
        this.event = event;
    }

    public AddButtonClick(String event, String event2) {
        this.event = event;
        this.event2 = event2;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEvent2() {
        return event2;
    }

    public void setEvent2(String event2) {
        this.event2 = event2;
    }
}
