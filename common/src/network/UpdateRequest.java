package network;

import enums.RequestTypes;

import java.io.Serial;
import java.util.Date;

public class UpdateRequest extends Request{
    @Serial
    private static final long serialVersionUID = 1935197592133045092L;
    private Date currentLastUpdate;
    public UpdateRequest() {
        super(RequestTypes.UPDATE);
    }
    public UpdateRequest(Date currentLastChange) {
        super(RequestTypes.UPDATE);
        this.currentLastUpdate = currentLastChange;
    }

    public void setCurrentLastUpdate(Date currentLastUpdate) {
        this.currentLastUpdate = currentLastUpdate;
    }

    public Date getCurrentLastUpdate() {
        return currentLastUpdate;
    }

    @Override
    public String toString() {
        return "UpdateRequest{" +
                "currentLastChange=" + currentLastUpdate +
                '}';
    }
}
