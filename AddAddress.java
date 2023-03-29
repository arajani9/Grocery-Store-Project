/**
 * This is the code to get all the address info
 * from the user and store in the 
 * address table of the database.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddAddress extends JFrame {
	JTextField name= new JTextField("");  //To Get User Name.
	JTextField phonenum=new JTextField(""); //To Get user phone number.
	JTextField streetaddr = new JTextField(""); //To get user Street address.
	JTextField city = new JTextField(""); //To get user city.
	JTextField states = new JTextField(""); //To get user state.
	JTextField postalcode = new JTextField("");  //To get user zipcode.
	JButton add = new JButton("Add"); //Add button to add the address.
	JButton back = new JButton("Back");  //Back button to go back to main frame.
	MainFrame mainFrame = null;
	AddAddress frame = this;
	ResultSet rs;
	String sqlcode;
	SQL newaddr;
	int userid;
	int addrid;
	//Add Address function.
	public AddAddress(int uid,SQL sqlo,MainFrame mainFrame){
		this.mainFrame = mainFrame;
		userid = uid;
		System.out.println("userid = "+ userid);
		newaddr=sqlo;
		JPanel jpanel = new JPanel();
		jpanel.setLayout(new GridLayout(6,2));
		jpanel.add(new JLabel("Name:")); jpanel.add(name);
		jpanel.add(new JLabel("Phone Number (xxx-xxx-xxxx):")); jpanel.add(phonenum);
		jpanel.add(new JLabel("Street Address: ")); jpanel.add(streetaddr);
		jpanel.add(new JLabel("City: ")); jpanel.add(city);
		jpanel.add(new JLabel("states:")); jpanel.add(states);
		jpanel.add(new JLabel("Postal Code:")); jpanel.add(postalcode);
		this.add(jpanel,BorderLayout.CENTER);
		JPanel buttonpanel=new JPanel();
		buttonpanel.setLayout(new GridLayout(1,2,50,50));
		buttonpanel.add(add); buttonpanel.add(back);
		add.addActionListener(new addrlistener());
		back.addActionListener(new addrlistener());
		this.add(buttonpanel,BorderLayout.SOUTH);
	}
	public static void invoke (int uid, SQL sqlo,MainFrame  mainFrame){
		JFrame address = new AddAddress(uid,sqlo,mainFrame);
		address.setTitle("Add a new address");
		address.setVisible(true);
		address.setLocationRelativeTo(null);
		address.setSize(400,220);
		address.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	class addrlistener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			sqlcode="select max(addrid) from address";  //Getting the next avaible address id for the user. 
			rs=newaddr.QueryExchte(sqlcode);
			try {	
				rs.next();
				addrid= rs.getInt(1)+1;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
			String Name= name.getText();
			String Pnum= phonenum.getText();
			String Streetaddr= streetaddr.getText();
			String City = city.getText();
			String States = states.getText();
			String Postalcode = postalcode.getText();
			if(e.getSource()==back){
				mainFrame.setVisible(true);
				frame.dispose();
			}
			//Checks if any of the field is left empty by the user,
			else if(Name.trim().isEmpty()||Pnum.trim().isEmpty()||Streetaddr.trim().isEmpty()
					||City.trim().isEmpty()||States.trim().isEmpty()||Postalcode.trim().isEmpty())
				JOptionPane.showMessageDialog(null, "It is required to fill in every blank","Error",
						JOptionPane.ERROR_MESSAGE);
			else
			{ // Insert a new address of this user to the table address
				sqlcode="insert into address values ("+addrid+","+userid+", \'"+Name+"\', "
						+ "\'"+Pnum+"\', \'"+States+"\', \'"+City+"\', \'"+Streetaddr+"\', "
								+ "\'"+Postalcode+"\')";
				newaddr.WriteExcute(sqlcode);
				JOptionPane.showMessageDialog(null, "You have successfully added a new address",
						"Success", JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(AddAddress.class.getResource("success.png")));
				mainFrame.setVisible(true);
				mainFrame.setSearchAndBuyButtonEnable(true);
				frame.dispose();
			}   
		}
	}
}