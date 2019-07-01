package com.scnu.fightofbit;

import javax.swing.*;

public class UI {
    private JPanel rootPanel;
    protected JButton btn_open;
    protected JButton btn_save;
    protected JButton btn_fight;
    protected JButton btn_show;
    protected JTextArea textArea_input;
    protected JTextArea textArea_result;
    protected JTextArea textArea_log;

    public UI(){
        JFrame frame=new JFrame("比特大战");
        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640,480);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new UI();
    }


}
