/**
 * This is the main frame of the Application.
 * It contains all the button to perform every task throughout
 * the application.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class MainFrame extends JFrame{
	JButton loginButton = new JButton("Login!");
	JButton registerButton = new JButton("Sign-up!");
	JButton searchProductButton = new JButton ("Search Product");
	JButton buyButton = new JButton ("Go to cart and Purchase");
	JButton addAddressButton = new JButton("Add Address");
	JButton QuitButton = new JButton("Logout!");
	JButton DeleteAccButton = new JButton("Delete Account");
	JLabel noticeString = new JLabel("Please login or signup if new user.");
	JLabel reminderString = new JLabel("");
	MainFrame mainFrame = this;
	SQL sql = null;
	int userid;
	public MainFrame() throws SQLException
	{
		// run initial the sql, build connection 
		sql = new SQL();
		userid = 0;
		 // button panel for frame
		 JPanel mainArc  = new JPanel();
	     mainArc.setLayout(new GridLayout(7,1,25,0));
	     mainArc.setSize(400, 400);
	    // listener for main frame, need sql passed to subframe 
	    MainButtonListener listener = new MainButtonListener(sql); //need to pass the sql class and userid 
	    loginButton.addActionListener(listener);
	    mainArc.add(loginButton);
	    registerButton.addActionListener(listener);
		mainArc.add(registerButton);
		DeleteAccButton.addActionListener(listener);
		DeleteAccButton.setEnabled(false);
		mainArc.add(DeleteAccButton);
		addAddressButton.addActionListener(listener);
        addAddressButton.setEnabled(false);
		mainArc.add(addAddressButton);
		searchProductButton.addActionListener(listener);
		searchProductButton.setEnabled(false);
		mainArc.add(searchProductButton);
		buyButton.addActionListener(listener);
		buyButton.setEnabled(false);
		mainArc.add(buyButton);
		QuitButton.addActionListener(listener);
	    mainArc.add(QuitButton); 
	    //setUserid();
	    this.add(mainArc,BorderLayout.WEST);
		//Right Side 
	    JPanel logoPanel = new JPanel();
	    logoPanel.setLayout(new GridLayout(0,2));
	    JLabel imageLabel = new JLabel(new ImageIcon(MainFrame.class.getResource("ecommlogo.png")));
	    logoPanel.add(imageLabel);
	    JPanel stringPanel =  new JPanel();
	    stringPanel.setLayout(new GridLayout(0,1));
	    stringPanel.add(noticeString);
	    JPanel stringPanel2 = new JPanel();
	    stringPanel2.add(reminderString);
	    JPanel rightPanel = new JPanel();
	    rightPanel.setLayout(new GridLayout(0,1));
	    rightPanel.add(logoPanel);
	    rightPanel.add(stringPanel);
	    rightPanel.add(stringPanel2);
	    this.add(rightPanel, BorderLayout.EAST);   
	}
	public void setUserid(int id) throws SQLException
	{
		userid = id;
		noticeString.setText("You have login with user id "+ id);
		System.out.println("Userid is set to "+ userid);
		setAddAddressButtonEnable(true);
		setDeleteAccButtonEnable(true);
		setLoginButtonEnable(false);
		setRegisterEnable(false);
		//check if there is address.
		String sqlCode = "select * from address where userid = "+id;
		java.sql.ResultSet result = sql.QueryExchte(sqlCode);
		if(result.next())
		{
			setSearchAndBuyButtonEnable(true);
		}
		else
		{
			reminderString.setText("Please add an address before your shopping");
		}
	}
	
	public void setAddAddressButtonEnable(boolean b)
	{
		addAddressButton.setEnabled(b);
	}
	public void setSearchAndBuyButtonEnable(boolean b)
	{
		reminderString.setText("Please enjoy your shopping!");
		searchProductButton.setEnabled(b);
		buyButton.setEnabled(b);
	}
	
	public void setLoginButtonEnable(boolean b){
		loginButton.setEnabled(b);
	}
	
	public void setRegisterEnable(boolean b){
		registerButton.setEnabled(b);
	}
	public void setDeleteAccButtonEnable(boolean b) {
		DeleteAccButton.setEnabled(b);
	}
	public static void main(String[] args) throws Exception{
	      MainFrame frame = new MainFrame();
	      frame.setTitle("Grocery Shopping");
	      frame.setLocationRelativeTo(null);
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.setSize(700,500);
	      frame.setVisible(true);
	}
    public class MainButtonListener implements ActionListener {
    		SQL sql = null;
			public MainButtonListener(SQL sqlpassed) {
				sql = sqlpassed;	
			}
			public void actionPerformed(ActionEvent event ) {
				if(event.getSource() == loginButton)
				{
					//mainFrame to be passed 
				  Login.invoke(sql,mainFrame);
				  mainFrame.setVisible(false);
				}
				else if(event.getSource() == registerButton)
				{
					//mainFrame to be passed 
					Register.invoke(sql,mainFrame);
					mainFrame.setVisible(false);
				}
				else if (event.getSource() == addAddressButton)
				{
					//mainFrame to be passed 
					System.out.println("userid = "+ userid+ "is passed to addAddressButton");
					AddAddress.invoke(userid,sql,mainFrame);
				
					mainFrame.setVisible(false);
				}
				else if (event.getSource() == searchProductButton)
				{
					//done 
					mainFrame.setVisible(false);
					SearchFrame.invoke(userid,sql,mainFrame);
				}
				else if(event.getSource() == buyButton)
				{//SQL query to get product name and info from save_To_shopping_cart table to show up
				//if user presses bubutton.
				 String sqlCode = "select P.name, S.addTime, S.quantity, P.pid, P.price from Save_To_Shopping_Cart S,"
				 		+ " Product P where S.pid = P.pid and S.userid = "+ userid + ";";
				 java.sql.ResultSet rs = sql.QueryExchte(sqlCode);
				 int rowCount = 0;
		          try {
		              rs.last();
		              rowCount = rs.getRow();
		              rs.first();
		          } catch (Exception e) {
		              // TODO: handle exception
		              e.printStackTrace();
		          }
		          if(rowCount == 0)			   
		          	JOptionPane.showMessageDialog(null, "No product is added into cart yet", "NO Result",JOptionPane.OK_OPTION);			      	
		          else 
					try {
						SetUpOrderFrame.invoke(userid, sql,rs,mainFrame);
						mainFrame.setVisible(false);
					} catch (SQLException e) {
					}
				}
				else if (event.getSource() == DeleteAccButton)
				{
					//done 
					mainFrame.setVisible(false);
					DeleteAccount.invoke(sql,mainFrame);
					
					}
				else if(event.getSource() == QuitButton)
				{
                    //done 
					System.exit(0);
				}
		}
    }
}