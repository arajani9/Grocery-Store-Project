/**
 * This is the code
 * that will take input from the user
 * and will return the table product
 * that contain all the item in the particular
 * category.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;
//sql operation in button submit to be implemented  --done
public class SearchFrame extends JFrame{
	JButton submitButton = new JButton("Submit");
	JButton backButton = new JButton("Back");
	JTextField typeTF = new JTextField();
	SearchFrame frame = this;
	int userid; 
	SQL sql;
	MainFrame mainFrame;
	public SearchFrame(int id,SQL sqlo,MainFrame mainFrame)
	{
		userid = id;
		sql = sqlo;
		this.mainFrame = mainFrame;
		Panel titlePanel = new Panel();
		Panel searchContentPanel = new Panel();
		Panel buttonPanel = new Panel();
		// title panel 
		titlePanel.add(new JLabel("Search for the product you want!"));
		titlePanel.setSize(400,70);
		this.add(titlePanel,BorderLayout.NORTH);
		// search content panel 
		searchContentPanel.setLayout(new GridLayout(1,1,50,50));//changes
		searchContentPanel.setSize(2,2);
		searchContentPanel.add(new JLabel("Enter the item name:"));
		searchContentPanel.add(typeTF);
		this.add(searchContentPanel, BorderLayout.CENTER);
		// button panel 
	    buttonPanel.setLayout(new GridLayout(1,2,25,25));	
	    SearchListener listener = new SearchListener();
	    submitButton.addActionListener(listener);
	    backButton.addActionListener(listener);
		buttonPanel.add(submitButton);
		buttonPanel.add(backButton);
	    this.add(buttonPanel, BorderLayout.SOUTH);
	}
	private void setUpNewUI(int id, SQL sql,MainFrame mainFrame)
	    {
	        SearchFrame frame = new SearchFrame(id,sql,mainFrame);
	        frame.setTitle("Grocery");
	        frame.setLocationRelativeTo(null);
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        frame.setSize(400,150);
	        frame.setVisible(true);
	    }
	   static public void invoke(int id, SQL sql, MainFrame mainFrame)
	   {
		   SearchFrame frame = new SearchFrame(id,sql,mainFrame);
		   frame.setUpNewUI(id,sql,mainFrame);
	   }
	   public class SearchListener implements ActionListener{
		    @Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				if(event.getSource() ==  submitButton)
			    	 {
			    	 // to be implemented 
				    String type = typeTF.getText().trim();
				    String sqlCode = ""; 
				    //SQL query to select all the product of particular type, example: Type = milk.
				    sqlCode += "select * from product ";
					if(type.length()!=0)
					   sqlCode += "where ";
					sqlCode+= "type = "+"\'"+type.substring(0, 1).toLowerCase() + type.substring(1)+"\' ";
					sqlCode+= "AND quantity > 0;";   
					  System.out.println(sqlCode);
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
			          	JOptionPane.showMessageDialog(null, "No Result is found", "NO Result",JOptionPane.OK_OPTION);			      	
			          else 
			    	  try {
						SaveToCartFrame.invoke(userid, sql, rs, frame);
						frame.setVisible(false);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	  //open SaveToCartFrame with rs, sql, and userid 
			    	 }
			     else if (event.getSource() == backButton)
			     {
			    	 mainFrame.setVisible(true);
			    	 frame.dispose();
			      }
			}
	   }
	}