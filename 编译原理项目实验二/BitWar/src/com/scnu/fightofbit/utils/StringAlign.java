package com.scnu.fightofbit.utils;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

public class StringAlign extends Format {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int JUST_LEFT = 'l'; //左对齐常量
    public static final int JUST_RIGHT = 'r'; //右对齐常量
    public static final int JUST_CENTER = 'c'; //居中常量
    private int just; //当前的对齐
    private int maxChars;

    public StringAlign(int maxChars, int just) {
        switch (just) {
            case JUST_CENTER:
            case JUST_LEFT:
            case JUST_RIGHT:
                this.just = just;
                break;
            default:
                throw new IllegalArgumentException("invalid justification arg.");
        }
        if (maxChars < 0) {
            throw new IllegalArgumentException("maxChars has to be an integer");
        }
        this.maxChars = maxChars;
    }

    public StringBuffer format(Object obj, StringBuffer where, FieldPosition ignore) {
        String s = (String) obj;
        String wanted = s.substring(0, Math.min(s.length(), maxChars));
        //如果没有空间用于对其，则返回最大行宽的内容
        if (wanted.length() > maxChars) {
            where.append(wanted);
        } else switch (just) {
            case JUST_RIGHT:
                pad(where, maxChars - wanted.length());
                where.append(wanted);
                break;
            case JUST_LEFT:
                where.append(wanted);
                pad(where, maxChars - wanted.length());
                break;
            case JUST_CENTER:
                int startPos = where.length();
                pad(where, (maxChars - wanted.length()) / 2);
                where.append(wanted);
                pad(where, (maxChars - wanted.length()) / 2);
                //调整化整误差
                pad(where, maxChars - (where.length() - startPos));
                break;
        }
        return where;
    }

    protected final void pad(StringBuffer to, int howMany) {
        for (int i = 0; i < howMany; i++) {
            to.append(" ");
        }
    }

    String format(String s) {
        return format(s, new StringBuffer(), null).toString();
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
        // TODO Auto-generated method stub
        return source;
    }
}
