package org.loxf.metric.biz.Operation;

import org.apache.commons.lang.StringUtils;
import org.loxf.metric.base.exception.MetricException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luohj on 2017/6/22.
 */
public class Arithmetic {
    private static Logger logger = LoggerFactory.getLogger(Arithmetic.class);
    public static void main(String args[]){
        System.out.println(arithmetic("(126.0/0*598.3199999999999)/1059.74"));
        //System.out.println(isNum("-2.212+13"));
    }
    public static boolean isNum(String expression){
        try {
            BigDecimal t = new BigDecimal(expression);
        } catch (NumberFormatException e){
            logger.debug("当前表达式：" + expression);
            return false;
        }
        return true;
    }
    public static boolean isExpression(String expression){
        if(StringUtils.isEmpty(expression)){
            return false;
        }
        if(isNum(expression)){
            return false;
        }
        return true;
    }
    public static String arithmetic(String exp){
        try {
            return parseExp(exp, 0).replaceAll("[\\[\\]]", "");
        } catch (MetricException e){
            logger.error("运算错误：" + exp, e);
            throw new MetricException("运算错误：" + exp, e);
        }
    }
    /**
     * 解析计算四则运算表达式，例：2+((3+4)*2-22)/2*3
     * @param expression
     * @return
     */
    public static String parseExp(String expression, int count){
        if(count>500){
            throw new MetricException("当前运算层次过深[>500]，可能堆栈溢出。");
        }
        //先判断是否数字
        if(isNum(expression)){
            return expression;
        }
        expression=expression.replaceAll("\\s+", "").replaceAll("^\\((.+)\\)$", "$1");
        String checkExp="\\d";
        String minExp="^((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\+\\-\\*\\/]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))$";
        //最小表达式计算
        if(expression.matches(minExp)){
            String result=calculate(expression);

            return Double.parseDouble(result)>=0?result:"["+result+"]";
        }
        //计算不带括号的四则运算
        String noParentheses="^[^\\(\\)]+$";
        String priorOperatorExp="(((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\*\\/]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\])))";
        String operatorExp="(((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\+\\-]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\])))";
        if(expression.matches(noParentheses)){
            Pattern patt=Pattern.compile(priorOperatorExp);
            Matcher mat=patt.matcher(expression);
            if(mat.find()){
                String tempMinExp=mat.group();
                expression=expression.replaceFirst(priorOperatorExp, parseExp(tempMinExp, ++count));
            }else{
                patt=Pattern.compile(operatorExp);
                mat=patt.matcher(expression);

                if(mat.find()){
                    String tempMinExp=mat.group();
                    expression=expression.replaceFirst(operatorExp, parseExp(tempMinExp, ++count));
                }
            }
            return parseExp(expression, ++count);
        }
        //计算带括号的四则运算
        String minParentheses="\\([^\\(\\)]+\\)";
        Pattern patt=Pattern.compile(minParentheses);
        Matcher mat=patt.matcher(expression);
        if(mat.find()){
            String tempMinExp=mat.group();
            expression=expression.replaceFirst(minParentheses, parseExp(tempMinExp, ++count));
        }
        return parseExp(expression, ++count);
    }
    /**
     * 计算最小单位四则运算表达式（两个数字）
     * @param exp
     * @return
     */
    public static String calculate(String exp){
        exp=exp.replaceAll("[\\[\\]]", "");
        String number[]=exp.replaceFirst("(\\d)[\\+\\-\\*\\/]", "$1,").split(",");
        BigDecimal number1=new BigDecimal(number[0]);
        BigDecimal number2=new BigDecimal(number[1]);
        BigDecimal result=null;

        String operator=exp.replaceFirst("^.*\\d([\\+\\-\\*\\/]).+$", "$1");
        if("+".equals(operator)){
            result=number1.add(number2).setScale(4, BigDecimal.ROUND_HALF_UP);
        }else if("-".equals(operator)){
            result=number1.subtract(number2).setScale(4, BigDecimal.ROUND_HALF_UP);
        }else if("*".equals(operator)){
            result=number1.multiply(number2).setScale(4, BigDecimal.ROUND_HALF_UP);
        }else if("/".equals(operator)){
            if(number2.compareTo(BigDecimal.ZERO)==0){
                // 被除数等于0
                return "0";
            }
            result=number1.divide(number2, 4, BigDecimal.ROUND_HALF_UP);
        }

        return result!=null?result.toPlainString():"0";
    }
}