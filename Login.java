/**
 * This code is for login the user into his account
 * User has to give his/her 
 * name and userid to login.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame{
	JTextField userid = new JTextField ("");
	JTextField Name = new JTextField("");
	JButton login = new JButton("Log in");
	JButton back = new JButton("Back");
	SQL loginsql;
	ResultSet rs;
	String sqlcode;
	String uid;
	String name;
	MainFrame mainFrame = null;
	Login frame = this;
	public Login(SQL sqlo,MainFrame mainFrame){
		loginsql=sqlo;
		this.mainFrame = mainFrame;
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(2,2));
		panel1.add(new JLabel("Enter User Id: ")); 
		panel1.add (userid);
		panel1.add(new JLabel("Enter Your name: ")); 
		panel1.add (Name);
		this.add(panel1, BorderLayout.CENTER);
		JPanel buttonpanel=new JPanel();
		buttonpanel.setLayout(new GridLayout(1,2,50,50));
		buttonpanel.add(login); buttonpanel.add(back);
		login.addActionListener(new loginListener());
		back.addActionListener(new loginListener());
		this.add(buttonpanel,BorderLayout.SOUTH);
	}
	public static void invoke(SQL sqlo,MainFrame mainFrame){
		JFrame login = new Login(sqlo,mainFrame);
		login.setTitle("User log in");
		login.setLocationRelativeTo(null);
		login.setSize(400,150);
		login.setVisible(true);
		login.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	class loginListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==login){
				uid = userid.getText();
				name = Name.getText();
				//Checks if any data asked by user is left empty and prompt message accordingly.
				if(uid.trim().isEmpty() & name.trim().isEmpty())
					JOptionPane.showMessageDialog(null,"User id and Name cannot be left blank ","Error",
							JOptionPane.ERROR_MESSAGE);
					else if(uid.trim().isEmpty())
					JOptionPane.showMessageDialog(null, "You must input your user id","Error", 
							JOptionPane.ERROR_MESSAGE);
				else if(name.trim().isEmpty())
					JOptionPane.showMessageDialog(null, "You must input your Name","Error", JOptionPane.ERROR_MESSAGE);
				else if(!isbuyer(uid))
					JOptionPane.showMessageDialog(null, "You have not registered. You must register before login", 
							"Error",JOptionPane.ERROR_MESSAGE);
				else
					try {
						if(name.equalsIgnoreCase(getresult())){
							int userid= Integer.parseInt(uid); 
							JOptionPane.showMessageDialog(null, "You have logged in successfully",
									"Log in successfully",
									JOptionPane.INFORMATION_MESSAGE,
									new ImageIcon(Login.class.getResource("success.png")));
							mainFrame.setUserid(userid);
							mainFrame.setAddAddressButtonEnable(true);
							mainFrame.setVisible(true);
							frame.dispose();
						}
						else
							JOptionPane.showMessageDialog(null, "The user id or Name is not correct", 
									"Log in failed",JOptionPane.ERROR_MESSAGE);
					} catch (NumberFormatException | HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
						e1.printStackTrace();
						}
			}
			else if(e.getSource()==back){
				mainFrame.setVisible(true);
				frame.dispose();
			}
		}
	}
	public String getresult() throws SQLException{
		//SQL query to get name of user by providing user id.
		sqlcode="select name from users where userid = "+uid;
		rs=loginsql.QueryExchte(sqlcode);
		rs.next();
		String pnamedb = rs.getString(1);
		System.out.println("for userid "+uid+" the name should be "+ pnamedb);
		return pnamedb;
	}
	
	public boolean isbuyer(String uid){
		//SQL query to get userid of buyer by providing uid.
		sqlcode="select userid from buyer where userid = "+uid;
		rs=loginsql.QueryExchte(sqlcode);
		int rowCount = 0;
         try {
             rs.last();
             rowCount = rs.getRow();
             rs.first();
         } catch (Exception e) {
             // TODO: handle exception
             e.printStackTrace();
         }
         if(rowCount == 0){			   
         	return false;
         }
         return true;
	}
}
