package com.sum.xlog.core;


/**
 * 
 * 一个日志输出级别的枚举<p>
 * <b>各枚举值的含义如下:</b>
 * <ul>
 * <li>LogLevel.V 日志输出颜色为黑色的，输出大于或等于VERBOSE日志级别的信息 </li>
 * <li>LogLevel.D 日志输出颜色是蓝色的，输出大于或等于DEBUG日志级别的信息</li>
 * <li>LogLevel.I 日志输出为绿色，输出大于或等于INFO日志级别的信息 </li>
 * <li>LogLevel.W 输出为橙色, 输出大于或等于WARN日志级别的信息 </li>
 * <li>LogLevel.E 输出为红色, 仅仅输出ERROR日志级别的信息 </li>
 * </ul>
 */
public class LogLevel
{
    /**
     * 输出颜色为黑色的，输出大于或等于VERBOSE日志级别的信息
     **/
    public static byte V = 0;
    /**
     * 输出颜色是蓝色的，输出大于或等于DEBUG日志级别的信息
     **/
    public static byte D = 1;
    /**
     * 输出为绿色，输出大于或等于INFO日志级别的信息
     **/
    public static byte I = 2;
    /****
     * 输出为橙色, 输出大于或等于WARN日志级别的信息
     **/
    public static byte W = 3;
    /****
     * 输出为红色，仅输出ERROR日志级别的信息.
     */
    public static byte E = 4;
    /***
     * 只输出ASSERT级别的信息
     */
    public static byte WTF = 5;
    /**
     * 什么都不输出
     */
    public static byte OFF = 6;

    private LogLevel() {}
}
