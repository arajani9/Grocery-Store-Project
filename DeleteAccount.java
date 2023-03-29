/**
 * This code is for the user 
 * to delete his/her account
 * if user no longer to be the member.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
//import java.sql.SQLException;
public class DeleteAccount extends JFrame{
	JTextField name = new JTextField();
	JTextField Userid = new JTextField();
	JButton submit = new JButton("submit");
	JButton back = new JButton("Back");
	MainFrame mainFrame = null;
	DeleteAccount frame = this;
	SQL deleteuser;
	ResultSet rs;
	int userid;
	String sqlcode;
	public DeleteAccount(SQL sqlo,MainFrame mainFrame){
		this.mainFrame = mainFrame;
		deleteuser=sqlo;
		JPanel jpanel = new JPanel();
		jpanel.setLayout(new GridLayout(2,2));
		jpanel.add(new JLabel("User ID:"));jpanel.add(Userid);
		jpanel.add(new JLabel("name:"));jpanel.add(name);
		this.add(jpanel,BorderLayout.CENTER);
		JPanel buttonpanel=new JPanel();
		buttonpanel.setLayout(new GridLayout(1,2,50,50));
		buttonpanel.add(submit); buttonpanel.add(back);
		submit.addActionListener(new deleteAcclistener());
		back.addActionListener(new deleteAcclistener());
		this.add(buttonpanel,BorderLayout.SOUTH);
	}
	public static void invoke (SQL sqlo,MainFrame mainFrame){
		JFrame deleteaccount = new DeleteAccount(sqlo,mainFrame);
		deleteaccount.setVisible(true);
		deleteaccount.setSize(600, 200);
		deleteaccount.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		deleteaccount.setLocationRelativeTo(null);
		deleteaccount.setTitle("Delete account page!");
	}
	class deleteAcclistener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String userid = Userid.getText();
			String Name = name.getText();
			String nameFormat = Name.substring(0,1).toUpperCase()+Name.substring(1);
			if(e.getSource()==back){
				mainFrame.setVisible(true);
				frame.dispose();
			}
			else if(Name.trim().isEmpty()||userid.trim().isEmpty())
				JOptionPane.showMessageDialog(null, "It is required to fill in every blank","Error",
						JOptionPane.ERROR_MESSAGE);
			else{
				//SQL query to delete the bank details
				sqlcode = "DELETE FROM bankcard b USING creditcard cc ";
				sqlcode+="WHERE cc.cardnumber = b.cardnumber ";
				sqlcode+="AND cc.userid = "+Integer.parseInt(userid)+";";
				deleteuser.WriteExcute(sqlcode);
				//SQL query to delete users all details from the database
				sqlcode = "DELETE FROM users WHERE ";
				sqlcode+="userid = "+Integer.parseInt(userid)+" AND name = \'"+nameFormat+"\';";
				deleteuser.WriteExcute(sqlcode);
				JOptionPane.showMessageDialog(null, "You have successfully deleted, your account linked with userid = "+
				userid+", Hope to see you back soon!","Account Deleted Successfully",
				JOptionPane.INFORMATION_MESSAGE,new ImageIcon(Register.class.getResource("success.png")));
				mainFrame.dispose();
				frame.dispose();
				System.exit(0);
			}
		}
	}
}
