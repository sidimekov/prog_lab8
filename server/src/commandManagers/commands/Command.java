package commandManagers.commands;

import enums.ReadModes;
import network.Response;

import java.io.Serial;
import java.io.Serializable;

public abstract class Command implements Serializable {
    @Serial
    private static final long serialVersionUID = -3435517048440493480L;
    protected String DESC;
    protected String USAGE;

    public abstract Response execute(ReadModes readMode, String[] args);

    public String getDesc() {
        return DESC;
    };

    public String getUsage() {
        return USAGE;
    };


}
