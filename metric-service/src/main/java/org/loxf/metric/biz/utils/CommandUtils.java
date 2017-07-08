package org.loxf.metric.biz.utils;

import org.loxf.metric.base.exception.MetricException;
import org.loxf.metric.biz.utils.cmd.ICommand;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luohj on 2017/5/22.
 */
public class CommandUtils {
    private static Logger logger = LoggerFactory.getLogger(CommandUtils.class);
    static String commandPath = "org.loxf.metric.biz.utils.cmd.";
    static String [] supports= {"R", "SUM", "MAX", "MIN", "COUNT", "FORMAT", "MULTIPLY"};
    public static List<Command> getCommands(String commandStr, boolean isQuota){
        List<Command> commands = new ArrayList<>();
        if(StringUtils.isNotEmpty(commandStr)) {
            String[] command = commandStr.split(" ");
            if(command==null || command.length==0||command.length ==1){
                return commands;
            }
            for (int m = 1; m < command.length; m++) {
                String cmd = findCommand(command[m]);
                if (StringUtils.isNotEmpty(cmd) && cmd.length()>2) {
                    if(cmd.startsWith("-Q_")){
                        // 指标专用命令
                        if(isQuota){
                            cmd = cmd.substring(3, cmd.length() - 1);
                            String content = command[m].split("=").length > 1 ? command[m].split("=")[1] : "";
                            commands.add(new Command(cmd, content));
                        }
                    } else if(cmd.startsWith("-C_")){
                        // 图专用命令
                        if(!isQuota) {
                            cmd = cmd.substring(3, cmd.length() - 1);
                            String content = command[m].split("=").length > 1 ? command[m].split("=")[1] : "";
                            commands.add(new Command(cmd, content));
                        }
                    } else {
                        cmd = cmd.substring(1, cmd.length() - 1);
                        String content = command[m].split("=").length > 1 ? command[m].split("=")[1] : "";
                        commands.add(new Command(cmd, content));
                    }
                }
            }
        }
        return commands;
    }
    public static String findCommand(String str){
        List result = new ArrayList<String>();
        String regEx = "\\-([\\s\\S].*?)\\=";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(str);
        while (mat.find()){
            String tmp = mat.group();
            return tmp;
        }
        return "";
    }

    public static Object executeCommand(Command command, Object object){
        boolean isSupport = false;
        for(String support : supports){
            if(support.equalsIgnoreCase(command.getCommand())){
                isSupport = true;
                break;
            }
        }
        if (isSupport) {
            String className = commandPath + command.getCommand() + "Command";
            try {
                ICommand clazz = (ICommand) Class.forName(className).newInstance();
                return clazz.exe(command.getContent(), object);
            } catch (ClassNotFoundException e) {
                logger.error("当前运算，程序不支持：", e);
                throw new MetricException("当前运算，程序不支持：" + command.toString());
            } catch (IllegalAccessException e) {
                logger.error("当前运算，程序不支持：", e);
                throw new MetricException("当前运算，程序不支持：" + command.toString());
            } catch (InstantiationException e) {
                logger.error("当前运算，程序不支持：", e);
                throw new MetricException("当前运算，程序不支持：" + command.toString());
            }
        } else {
            throw new MetricException("当前运算不支持：" + command.toString());
        }
    }

    public static void main(String [] args){
        System.out.print(getCommands("LIST -R=0 -SUM=CIRCLE_TIME -Q_MAX=value", false));
    }
}
