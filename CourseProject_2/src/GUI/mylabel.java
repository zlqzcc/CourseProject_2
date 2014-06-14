package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import core.sakai.objects.SakaiSite;

public class mylabel extends JLabel{
	public SakaiSite site;
	
	mylabel(String name, SakaiSite si ){
		super(name);
		int width=0;
		site=si;
		char[] na=name.toCharArray();
		for(char ch:na){	
			if(Character.isLetter(ch)){
				width=width+15;
				
			}
			else {
				
				width=width+40;
			}
		}
		setFont(new Font(this.getFont().getFontName(),this.getFont().getStyle(),28));
		
		setSize(width,36);
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				setOpaque(false);
				setForeground(Color.black);
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setOpaque(true);
				setBackground(Color.GRAY);
				setForeground(Color.white);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
			
				loginpanel.mainpanel.re.removeall();
				
				loginpanel.mainpanel.re.add( new homeworkpanel(site));
				
				loginpanel.mainpanel.re.repaint();
				
				
			
				
				// TODO Auto-generated method stub
				
			}
			
		}
		);
   
	}
	

}
