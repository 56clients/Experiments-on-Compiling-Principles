package com.scnu.fightofbit;

import com.scnu.fightofbit.utils.SyntaxTree;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Main extends UI {

    private List<SyntaxTree> mList = new ArrayList<>();
//    private ExecutorService pool= Executors.newFixedThreadPool(5);

    public Main() {
        super();

        /**
         * 添加策略按钮的响应事件
         * 打开文件对话框选中多个策略文件
         */
        btn_open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser("./");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setMultiSelectionEnabled(true);
                int result = chooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] files = chooser.getSelectedFiles();
                    for (File f : files) {
                        SyntaxTree t = new SyntaxTree(f.getAbsolutePath());
                        if (t.Parse()) {
                            textArea_log.append("策略" + t.getFileName() + "@" + t.getStategyName() + "添加成功\n");
                            mList.add(t);
                        } else
                            textArea_log.append(t.getErrorMsg());
                    }

                }
            }
        });

        /**
         * 保存策略按钮的响应事件
         * 把策略输入框的内容存入文件中,并对策略进行分析,排查是否有错
         */
        btn_save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (textArea_input.getText().equals(""))
                    return;

                try {
                    File file = new File(new File("").getAbsolutePath(), "策略" + System.currentTimeMillis() + ".txt");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                    textArea_input.append("\0");
                    for (int i = 0; i < textArea_input.getLineCount(); i++) {
                        try {//getText(int offset,int length)
                            writer.write(textArea_input.getText(textArea_input.getLineStartOffset(i),
                                    textArea_input.getLineEndOffset(i) - textArea_input.getLineStartOffset(i) - 1));
                            writer.newLine();
                        } catch (BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    }
                    writer.flush();
                    writer.close();

                    textArea_input.setText("");


                    SyntaxTree t = new SyntaxTree(file.getAbsolutePath());
                    if (t.Parse()) {
                        JOptionPane.showMessageDialog(null, "保存成功,文件位置为:" + file.getAbsolutePath());
                    } else {
                        if (file.exists())
                            file.delete();
                        textArea_log.append(t.getErrorMsg());
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


        /**
         * 开始战斗按钮的响应事件
         * 将之前添加进来的策略进行两两对战,并将总分进行排序,并打印对战日志到文件
         */
        btn_fight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //对战需要两个策略以上
                if (mList.size() < 2) {
                    JOptionPane.showMessageDialog(null, "请添加2个以上的策略才能开始战斗");
                    return;
                }

                String N = JOptionPane.showInputDialog(null, "请输入要战斗的回合数：");
                Pattern pattern = Pattern.compile("[0-9]*");
                if (!pattern.matcher(N).matches()){
                    JOptionPane.showMessageDialog(null,"请输入合适的数字！");
                    return;
                }

                //初始化entry矩阵
                boolean[][] entry = new boolean[mList.size()][mList.size()];
                int[][] score = new int[mList.size()][mList.size()];
                for (int i = 0; i < mList.size(); i++) {
                    for (int j = 0; j < mList.size(); j++) {
                        entry[i][j] = false;
                        score[i][j] = 0;
                    }

                }
                for (int i = 0; i < mList.size(); i++) {
                    for (int j = 0; j < mList.size(); j++) {
                        //已经对战过的策略对不再进行对战
                        if (entry[i][j] == true)
                            continue;
                        if (i != j) {
                            String scoreString = fight(mList.get(i), mList.get(j),Integer.valueOf(N).intValue());
                            //scoreString 结构为"num num"
                            String[] fightScore = scoreString.split(" ");

                            score[i][j] = Integer.valueOf(fightScore[0]);
                            score[j][i] = Integer.valueOf(fightScore[1]);

                            entry[i][j] = true;
                            entry[j][i] = true;
                        }
                    }
                }

                int[] total = new int[mList.size()];
                for (int i = 0; i < mList.size(); i++) {
                    total[i] = 0;
                    for (int j = 0; j < mList.size(); j++) {
                        total[i] += score[i][j];
                    }
                }

                analysis(total);
                mList.clear();
            }
        });

        /**
         *
         */
        btn_show.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mList.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "策略池为空");
                    return;
                }
                Object[] objects = new Object[mList.size()];
                for (int i = 0; i < mList.size(); i++) {
                    objects[i] = mList.get(i).getFileName() + "@" + mList.get(i).getStategyName();
                }
                Object select = JOptionPane.showInputDialog(null, "请选择要显示的策略", "显示语法树", JOptionPane.PLAIN_MESSAGE, null, objects, objects[0]);
                if (select == null)
                    return;
                for (int i = 0; i < objects.length; i++) {
                    if (select.equals(objects[i])) {
                        //choosed one
                        File save = new File("SyntaxTree for " + objects[i].toString() + ".txt");
                        try {
                            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(save));
                            bufferedWriter.write(mList.get(i).Print());
                            bufferedWriter.flush();
                            bufferedWriter.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(null, "语法树已生成，请打开查看\n" + save.getAbsolutePath());
                    }
                }
            }
        });


    }

    /**
     * 分析对战结果
     *
     * @param total 每个策略对应的总分
     */
    private void analysis(int[] total) {
        Map<SyntaxTree, Integer> totalMap = new HashMap<>();

        for (int i = 0; i < mList.size(); i++) {
            totalMap.put(mList.get(i), total[i]);
        }

        List<Map.Entry<SyntaxTree, Integer>> sortedList = new LinkedList<Map.Entry<SyntaxTree, Integer>>();
        sortedList.addAll(totalMap.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<SyntaxTree, Integer>>() {
                        @Override
                        public int compare(Map.Entry<SyntaxTree, Integer> o1, Map.Entry<SyntaxTree, Integer> o2) {
                            //逆序
                            return o2.getValue() - o1.getValue();
                        }
        });

        textArea_result.append("对战结果如下:\n");
        for (int i = 0; i < mList.size(); i++) {
            textArea_result.append("第" + (i + 1) + "名 策略" + sortedList.get(i).getKey().getFileName() + "@" +
                    sortedList.get(i).getKey().getStategyName() + ",总分" + sortedList.get(i).getValue() + "\n");
        }
        textArea_result.append("\n");
    }

    /**
     * 策略对战,并打印对战日志
     *
     * @param t1
     * @param t2
     * @return
     */
    public String fight(SyntaxTree t1, SyntaxTree t2, int N) {
        int score1 = 0, score2 = 0;
        try {
            File parent = new File(new File("").getAbsolutePath() + "/log/");
            if (!parent.exists())
                parent.mkdirs();
            File file = new File(parent,
                    t1.getFileName() + "@" + t1.getStategyName() + " vs " +
                            t2.getFileName() + "@" + t2.getStategyName() + ".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < N; i++) {
                int temp1 = t1.MakeChoice();
                int temp2 = t2.MakeChoice();

                t1.SaveEnemyChoice(temp2);
                t2.SaveEnemyChoice(temp1);

                if (temp1 == 0 && temp2 == 0) {
                    score1 += 1;
                    score2 += 1;
                }
                if (temp1 == 0 && temp2 == 1) {
                    score1 += 5;
                }
                if (temp1 == 1 && temp2 == 0) {
                    score2 += 5;
                }
                if (temp1 == 1 && temp2 == 1) {
                    score1 += 3;
                    score2 += 3;
                }
                writer.write("第" + (i + 1) + "回合,对战情况---->" + temp1 + ":" + temp2 + "    " +
                        t1.getFileName() + "@" + t1.getStategyName() + "累计得分:" + score1 + "    " +
                        t2.getFileName() + "@" + t2.getStategyName() + "累计得分:" + score2);
                writer.newLine();
            }
            writer.flush();
            writer.close();
            textArea_result.append("在策略" + t1.getFileName() + "@" + t1.getStategyName() +
                    "和策略" + t2.getFileName() + "@" + t2.getStategyName() + "的对决中:\n");
            if (score1 > score2) {
                textArea_result.append("策略" + t1.getFileName() + "@" + t1.getStategyName() + "获胜,比分" + score1 + ":" + score2 + "\n\n");
            } else if (score1 < score2) {
                textArea_result.append("策略" + t2.getFileName() + "@" + t2.getStategyName() + "获胜,比分" + score1 + ":" + score2 + "\n\n");
            } else {
                textArea_result.append("平局,比分" + score1 + ":" + score2 + "\n\n");
            }

            textArea_log.append("对战信息已记录,详情请打开" + file.getAbsolutePath() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return score1 + " " + score2;
    }

    public static void main(String[] args) {
        new Main();

    }

}
