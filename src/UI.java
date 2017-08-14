import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;


public class UI {
	JTextArea textArea;
	JButton targetButton;
	JButton standardButton;
	JButton runButton;
	JTextField standardTextField;
	JTextField targetTextField;
	JFrame frame;
	String newFileName;
	String targetFileName;
    JTextField englishColumnTextField;
    JButton resetButton;

    public static void UIPrintOut(UI ui,String s) {
        ui.textArea.setText(ui.textArea.getText()+'\n'+s);
    }

	public UI() throws Exception{
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		frame=new JFrame("SimilarityEstimate_EnglishDataEdition_170803");
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent arg0){
				frame.dispose();
				System.exit(0);
			}
		});
		frame.setSize(430,700);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-frame.getSize().width)/2,
				(Toolkit.getDefaultToolkit().getScreenSize().height-frame.getSize().height)/2);

		Font fnt=new Font("微软雅黑",Font.PLAIN,20);

		//标准表载入模块UI
		JPanel standardArea=new JPanel();
		standardArea.setLayout(null);
		standardArea.setBounds(0,0,420,150);
		JLabel standardLabel=new JLabel("请输入标准表地址:");
		standardLabel.setFont(fnt);
		standardLabel.setBounds(5,5,420,30);
		standardTextField=new JTextField("");
		standardTextField.setFont(new Font("微软雅黑",Font.PLAIN,16));
		standardTextField.setBounds(5,40,400,40);
		standardButton=new JButton("浏览");
		standardButton.setFont(fnt);
		standardButton.setBounds(5,90,80,40);
		standardArea.add(standardLabel);
		standardArea.add(standardTextField);
		standardArea.add(standardButton);

		//目标表载入模块UI
		JPanel targetArea=new JPanel();
		targetArea.setLayout(null);
		targetArea.setBounds(0,140,420,150);
		JLabel targetLabel=new JLabel("请输入语言表地址:");
		targetLabel.setFont(fnt);
		targetLabel.setBounds(5,5,420,30);
		targetTextField=new JTextField("");
		targetTextField.setFont(new Font("微软雅黑",Font.PLAIN,16));
		targetTextField.setBounds(5,40,400,40);
		targetButton=new JButton("浏览");
		targetButton.setFont(fnt);
		targetButton.setBounds(5,90,80,40);
		targetArea.add(targetLabel);
		targetArea.add(targetTextField);
		targetArea.add(targetButton);

		//目标表输入英文列模块UI
        JLabel englishColumnLabel=new JLabel("输入目标表英文列名:");
        englishColumnLabel.setFont(fnt);
        englishColumnLabel.setBounds(155, 90,200,40);
        englishColumnTextField=new JTextField("B");
        englishColumnTextField.setFont(new Font("微软雅黑",Font.PLAIN,16));
        englishColumnTextField.setBounds(355,90,50,40);
        targetArea.add(englishColumnLabel);
        targetArea.add(englishColumnTextField);

		//运行程序按键
		runButton=new JButton("运行程序");
		runButton.setFont(fnt);
		runButton.setBounds(5,290,150,40);

		//重置信息窗口按键
        resetButton=new JButton("重置下方窗口");
        resetButton.setFont(fnt);
        resetButton.setBounds(235, 290, 170, 40);


		//进度展示窗口
		textArea=new JTextArea();
		textArea.setLayout(new FlowLayout(FlowLayout.LEFT));
		textArea.setBounds(0,335,420,325);
		textArea.setBorder(BorderFactory.createLoweredBevelBorder());
		textArea.setFont(new Font("微软雅黑",Font.PLAIN,18));
		textArea.setEditable(false);
		String defaultText="使用简介:"+"\n"
				+"1.请指定目标表英语列的位置，目标语言列\n" +
                "   务必放在英语列的后面一列"+"\n"
				+"2.标准表和目标表仅支持xlsx"+"\n"
//				+"3.将cilin.xlsx放在本工具同一目录下"+"\n"
//							+"开发by:湘江激流"+"\n"
				+"--------------------------------------------------"+"\n";
		textArea.append(defaultText);

		//组建添加到frame
		frame.add(standardArea);
		frame.add(targetArea);
		frame.add(runButton);
        frame.add(resetButton);
        frame.add(textArea);
		frame.setVisible(true);

		//添加监听器
		standardButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent args){
				if(args.getSource()==standardButton){
					JFileChooser standardFileChooser=new JFileChooser(".");
					if(standardFileChooser.showOpenDialog(null)==standardFileChooser.APPROVE_OPTION){
						String s=standardFileChooser.getSelectedFile().getAbsolutePath();
						standardTextField.setText(null);
						standardTextField.setText(s);
					}
				}
			}
		});
		targetButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent args){
				if(args.getSource()==targetButton){
					JFileChooser targetFileChooser=new JFileChooser(".");
					if(targetFileChooser.showOpenDialog(null)==targetFileChooser.APPROVE_OPTION){
						String s=targetFileChooser.getSelectedFile().getAbsolutePath();
						targetTextField.setText(null);
						targetTextField.setText(s);
					}
				}
			}
		});

		resetButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent args){
                if(args.getSource()==resetButton){
                    textArea.setText("");
                }
            }
        });

		runButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(final ActionEvent args){
				new Thread(new Runnable(){
					@Override
					public void run(){
						try{
							if(args.getSource()==runButton){
								//判断是否存在同名文件
								targetFileName=targetTextField.getText();
								newFileName=targetFileName.substring(0,targetFileName.lastIndexOf("."))
										+"_NEW.xlsx";
								if(new File(newFileName).exists()){
                                    System.out.println("same name file do exists.");
                                    new replaceFileDialog().replaceFileDialog.setVisible(true);
								}
								else{
									try {
										Main mainPage=new Main();
										mainPage.setStandardExcelPath(standardTextField.getText());
										mainPage.setTargetExcelPath(targetTextField.getText(),englishColumnTextField.getText());
										mainPage.startMainStream();
									} catch (Exception f) {
//										System.out.println("点击运行按钮，程序发生错误");
										f.printStackTrace();
										textArea.append("程序发生错误，请检查"+"\n");
									}
								}
							}
						}catch(Exception e){
							System.out.println("Error When runButton Pressed");
						}
					}
				}).start();
			}
		});

	}

	//在JTextArea输出程序进度或消息
	public void setText(String s){
		textArea.append(s);
		textArea.append("\n");
	}

	//新表同名提示窗，选择覆盖同名文件或取消此次程序运行
	private class replaceFileDialog{
		JDialog replaceFileDialog=new JDialog(frame,"存在同名文件");

		private replaceFileDialog(){
			replaceFileDialog.setSize(400,260);
			replaceFileDialog.setLayout(null);
			replaceFileDialog.setResizable(false);
			replaceFileDialog.setBounds((frame.getWidth()-replaceFileDialog.getWidth())/2
							+(Toolkit.getDefaultToolkit().getScreenSize().width-frame.getSize().width)/2,
					(frame.getHeight()-replaceFileDialog.getHeight())/2
							+(Toolkit.getDefaultToolkit().getScreenSize().height-frame.getSize().height)/2,
					replaceFileDialog.getWidth(),
					replaceFileDialog.getHeight());
			Container dialogContainer=replaceFileDialog.getContentPane();
			JLabel dialogText=new JLabel("新表同名文件已存在，是否覆盖？");
			dialogText.setBounds(30,30,340,60);
			dialogText.setFont(new Font("微软雅黑",Font.PLAIN,20));
			JLabel dialogText2=new JLabel("(原文件将被删除)");
			dialogText2.setBounds(30,65,340,60);
			dialogText2.setFont(new Font("微软雅黑",Font.PLAIN,16));
			JButton dialogButtonReplace=new JButton("覆盖");
			dialogButtonReplace.setBounds(25,140,150,50);
			dialogButtonReplace.setFont(new Font("微软雅黑",Font.PLAIN,18));
			JButton dialogButtonCancel=new JButton("取消");
			dialogButtonCancel.setBounds(225,140,150,50);
			dialogButtonCancel.setFont(new Font("微软雅黑",Font.PLAIN,18));
			dialogContainer.add(dialogText);
			dialogContainer.add(dialogText2);
			dialogContainer.add(dialogButtonReplace);
			dialogContainer.add(dialogButtonCancel);
			dialogContainer.setVisible(true);

			dialogButtonCancel.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					replaceFileDialog.dispose();
				}
			});

			dialogButtonReplace.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e){
					new Thread(new Runnable(){
						@Override
						public void run(){
							replaceFileDialog.dispose();
							new File(newFileName).delete();
							System.out.println("mainPage.startProgram()");
							try {
								Main mainPage=new Main();
								mainPage.setStandardExcelPath(standardTextField.getText());
								mainPage.setTargetExcelPath(targetTextField.getText(),"B");
								mainPage.startMainStream();
							} catch (Exception f) {
//										System.out.println("点击运行按钮，程序发生错误");
								f.printStackTrace();
								textArea.append("程序发生错误，请检查"+"\n");
							}
						}
					}).start();
				}
			});
		}
	}
}
