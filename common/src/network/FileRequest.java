package network;

import enums.RequestTypes;

import java.io.Serial;

public class FileRequest extends Request{
    @Serial
    private static final long serialVersionUID = 7832110644811828752L;
    public FileRequest() {
        super(RequestTypes.FILE);
    }

}
