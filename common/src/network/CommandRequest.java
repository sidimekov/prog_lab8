package network;

import enums.ReadModes;
import enums.RequestTypes;

import java.io.Serial;
import java.util.Arrays;

public class CommandRequest extends Request {
    @Serial
    private static final long serialVersionUID = 5114584243644685314L;

    private String cmdName;
    private String[] args;
    private ReadModes readMode;

    public CommandRequest(String cmdName, String[] args) {
        super(RequestTypes.COMMAND);
        this.cmdName = cmdName;
        this.args = args;
        this.readMode = ReadModes.CONSOLE;
    }
    public CommandRequest(String cmdName, String[] args, ReadModes readMode) {
        super(RequestTypes.COMMAND);
        this.cmdName = cmdName;
        this.args = args;
        this.readMode = readMode;
    }

    public String getCommand() {
        return cmdName;
    }
    public String[] getArgs() {
        return args;
    }
    public ReadModes getReadMode() {
        return readMode;
    }
    public void setReadMode(ReadModes readMode) {
        this.readMode = readMode;
    }

    @Override
    public String toString() {
        return "CommandRequest{" +
                "cmdName='" + cmdName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", readMode=" + readMode +
                ", filePath=" + super.getFilePath() +
                '}';
    }
}
