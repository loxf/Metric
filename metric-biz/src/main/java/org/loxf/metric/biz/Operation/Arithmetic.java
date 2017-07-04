package org.loxf.metric.biz.Operation;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luohj on 2017/6/22.
 */
public class Arithmetic {
    public static void main(String args[]){
        System.out.println(arithmetic("2.2+((3+4)*2-22)/2*3.2"));
    }
    public static String arithmetic(String exp){
        return parseExp(exp).replaceAll("[\\[\\]]", "");
    }
    /**
     * 解析计算四则运算表达式，例：2+((3+4)*2-22)/2*3
     * @param expression
     * @return
     */
    public static String parseExp(String expression){
        //String numberReg="^((?!0)\\d+(\\.\\d+(?<!0))?)|(0\\.\\d+(?<!0))$";
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
                expression=expression.replaceFirst(priorOperatorExp, parseExp(tempMinExp));
            }else{
                patt=Pattern.compile(operatorExp);
                mat=patt.matcher(expression);

                if(mat.find()){
                    String tempMinExp=mat.group();
                    expression=expression.replaceFirst(operatorExp, parseExp(tempMinExp));
                }
            }
            return parseExp(expression);
        }
        //计算带括号的四则运算
        String minParentheses="\\([^\\(\\)]+\\)";
        Pattern patt=Pattern.compile(minParentheses);
        Matcher mat=patt.matcher(expression);
        if(mat.find()){
            String tempMinExp=mat.group();
            expression=expression.replaceFirst(minParentheses, parseExp(tempMinExp));
        }
        return parseExp(expression);
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
            result=number1.add(number2);
        }else if("-".equals(operator)){
            result=number1.subtract(number2);
        }else if("*".equals(operator)){
            result=number1.multiply(number2);
        }else if("/".equals(operator)){
            if(number2.compareTo(BigDecimal.ZERO)==0){
                // 被除数等于0
                return "0";
            }
            result=number1.divide(number2, 4, BigDecimal.ROUND_HALF_UP);
        }

        return result!=null?result.toString():"0";
    }
}