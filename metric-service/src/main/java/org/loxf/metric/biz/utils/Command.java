package org.loxf.metric.biz.utils;

/**
 * Created by luohj on 2017/5/22.
 */
public class Command {
    public Command( String command, String content){
        this.command = command;
        this.content = content;
    }

    public String toString(){
        return "-" + this.command + "=" + this.content;
    }
    private String command;
    private String content ;

    public String getCommand() {
        return command.toUpperCase();
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
