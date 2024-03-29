package analyse;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class UI extends JFrame {

    public String temp="";
    public LinkedList<String> input;
    public UI() {
        input=new LinkedList<>();
        initComponents();
    }

    /**
     * 这个方法用来初始化整个框架结构
     * 注意请不要修改这个代码，该方法的内容始终由Form生成
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        Start = new javax.swing.JButton();
        textURL = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        text_show = new javax.swing.JTextArea();
        Exit = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        about = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAutoRequestFocus(false);

        jLabel1.setText("输入表达式");

        Start.setText("开始");
        Start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Start(evt);
            }
        });

        textURL.setToolTipText("");
        textURL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textURLActionPerformed(evt);
            }
        });

        text_show.setEditable(false);
        text_show.setColumns(20);
        text_show.setRows(5);
        text_show.setAutoscrolls(false);
        text_show.setLineWrap(true);
        text_show.setWrapStyleWord(true);
        jScrollPane1.setViewportView(text_show);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
        );

        Exit.setText("退出");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Quit(evt);
            }
        });

        jButton1.setText("浏览");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                look(evt);
            }
        });

        jButton2.setText("生成随机");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creat(evt);
            }
        });

        jMenu1.setText("关于");

        about.setText("关于");
        about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutActionPerformed(evt);
            }
        });
        jMenu1.add(about);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("退出");

        jMenuItem1.setText("退出");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(textURL)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Start, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Exit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(32, 32, 32)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel1)
                                                        .addComponent(textURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jButton2)
                                                .addGap(7, 7, 7)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(19, 19, 19)
                                                .addComponent(jButton1)
                                                .addGap(30, 30, 30)
                                                .addComponent(Start)
                                                .addGap(28, 28, 28)
                                                .addComponent(Exit)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );

        pack();
    }

    private void Start(java.awt.event.ActionEvent evt) {
        text_show.setText("");
        temp=textURL.getText()+"\n";
        WordAnalyse wordAnalyse = new WordAnalyse(temp);
        wordAnalyse.analyse();
        StateAnalyze stateAnalyze = new StateAnalyze(wordAnalyse.tokes);
        stateAnalyze.start_analyse();
        text_show.append(stateAnalyze.output);
        text_show.append("\n");
        if (stateAnalyze.fail!=null) {
            text_show.append(stateAnalyze.fail);
            text_show.append("\n");

        }

    }

    private void Quit(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Quit
   
        System.exit(1);
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    
        System.exit(1);
    }

    private void aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutActionPerformed
      
        String str="编译原理实验一";
        JOptionPane.showMessageDialog(null,str,"信息",JOptionPane.PLAIN_MESSAGE);
    }

    private void look(java.awt.event.ActionEvent evt) {
       
        JFileChooser chooser=new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        int result =chooser.showOpenDialog(null);
        if(result==JFileChooser.APPROVE_OPTION)
        {
            String name=chooser.getSelectedFile().getPath();
            System.out.println(name);
            textURL.setText("");
            getFile(name);
            text_show.setText("");
            for (String a: input) {
                text_show.append(a+"->");
                WordAnalyse wordAnalyse = new WordAnalyse(a);
                wordAnalyse.analyse();
                StateAnalyze stateAnalyze = new StateAnalyze(wordAnalyse.tokes);
                stateAnalyze.start_analyse();
                text_show.append(stateAnalyze.output);
                text_show.append("\n");
                if (stateAnalyze.fail != null) {
                    text_show.append(stateAnalyze.fail);
                    text_show.append("\n");
                }
            }
        }
    }

    private void creat(java.awt.event.ActionEvent evt) {
    }

    private void textURLActionPerformed(java.awt.event.ActionEvent evt) {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* 创建并显示窗口 */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UI().setVisible(true);
            }
        });
    }

    // 界面设计用到的一些控件，部件
    private javax.swing.JButton Exit;
    private javax.swing.JButton Start;
    private javax.swing.JMenuItem about;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField textURL;
    private javax.swing.JTextArea text_show;

    public void getFile(String filepath)
    {
        String a="";
        if(input!=null) input.clear();
        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader(filepath));
            String temp=bufferedReader.readLine();
            while(temp!=null)
            {
                a=a+temp+"\n";
                input.addLast(temp+"\n");
                temp=bufferedReader.readLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
