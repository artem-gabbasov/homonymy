package intf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;

import javax.swing.GroupLayout;
import javax.swing.SpringLayout;

@SuppressWarnings("serial")
public class RecognitionApplet extends Applet {

	TextArea source, dest;
	Button exec;
	
	final static int gapSize = 5;
	
	public void init()
	{
		source = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		dest = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		
		exec = new Button("Лемматизировать");
		exec.setMaximumSize(new Dimension(100, 30));
		exec.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				execute();
			}
		});
		
		GroupLayout layout = new GroupLayout(this);
		
		layout.setHorizontalGroup(
				layout.createParallelGroup()
				.addGroup(
					layout.createSequentialGroup()
					.addComponent(source)
					.addGap(gapSize)
					.addComponent(dest)
				)
				.addComponent(exec)
				);
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGroup(
					layout.createParallelGroup()
					.addComponent(source)
					.addComponent(dest)
				)
				.addGap(gapSize)
				.addComponent(exec)
				);
		
		setLayout(layout);
	}
	
	private void execute()
	{
		try {
			dest.setText(Recognition.recognize(source.getText()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
