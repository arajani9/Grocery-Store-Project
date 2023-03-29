/**
 *This code is for the address frame
 *Like a table to show the address in the system
 *where user can get delivery 
 *for his/her order. 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;

public class addressFrame extends JPanel 
{
	int userid;
	int orderNumber;
	int addrid[] = null;
	SQL sql;
	Object[][] data = null;
	JTable table = null;
	JButton ConfirmButton = new JButton("Confirm");
	MainFrame mainFrame = null;
	JFrame frame = null;
	java.sql.ResultSet rs = null;
	
	public addressFrame(int id, SQL sql,int orderNumber,MainFrame mainFrame) throws SQLException
	{
		userid = id;
		this.orderNumber = orderNumber;
		this.sql = sql;
		this.mainFrame= mainFrame;
		//Below code is used to search all the address stored in database associated to the particular userid.
		String sqlCode = "select addrid, name, states, city, streetaddr, postCode from address where userid = " + userid;
		rs = sql.QueryExchte(sqlCode);
        table = new JTable(new AddressModule());
        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(5).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        table.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 1){
                    int columnIndex = table.columnAtPoint(e.getPoint()); 
                    int rowIndex = table.rowAtPoint(e.getPoint());
                    if(columnIndex == 5) {
                        if(table.getValueAt(rowIndex,columnIndex) == null){ 
                              table.setValueAt(false, rowIndex, columnIndex);
                          }
                        if(((Boolean)table.getValueAt(rowIndex,columnIndex)).booleanValue()){ 
                               table.setValueAt(false, rowIndex ,5); 
                          }
                        else {
                              table.setValueAt(true, rowIndex, 5);
                          }
                     }
                }
            }
        });
        JScrollPane scrollPane= new JScrollPane(table);
        add(scrollPane);
     }
	private int getSize(java.sql.ResultSet resultSet)
    {
    	int rowCount = 0;
        try {
            resultSet.last();
            rowCount = resultSet.getRow();
            resultSet.first();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return rowCount;
    }
	 public class AddressModule extends AbstractTableModel
	{
	   String[] columnName = {"Name","States","City","Street Address","Post Code", "select"};
	   Object[][] data = null;
	   Object[] longValue = {"Steven Murphy", "Florida", "Tampa", 
			   "123 This road main st, Blvd", "33021", new Boolean(false)};
	   public AddressModule() throws SQLException
		{
		super();
		data = (Object[][]) initData();
		}
	   @Override
		public int getColumnCount() {
		  return columnName.length;	
		}
		public boolean isCellEditable(int row, int column)  
        {  
			if (column == 5)  
            {  
                return true;  
            }  
            else  
            {  
                return false;  
            }  
        } 
		@Override
		public String getColumnName(int col) {
           return columnName[col];
       }
		public void setValueAt(Object o, int rowIndex, int columnIndex)
		{
			data[rowIndex][columnIndex] = o;
			table.repaint();
		}
		@Override
		public int getRowCount() {
			return data.length;
		}
		public Object getValueAt(int rowIndex, int columnIndex) {
			return data[rowIndex][columnIndex];
		}
		public Class getColumnClass(int c) {
	            return getValueAt(0, c).getClass();
	        }
		private Object[] initData() throws SQLException
		 {
			 int count = 0;
			 Object[][] data = null;
			 if(rs != null)
			 {
				 data = new Object[getSize(rs)][6];
				 addrid = new int[getSize(rs)];
				if(data.length!=0 && rs.first())
					do
				 {//Storing address info in the table.
						addrid[count] = rs.getInt(1);  //Address Id
					    data[count][0] = rs.getString(2);      //name
					    data[count][1] = rs.getString(3);	//state
					    data[count][2] = rs.getString(4);   //city
					    data[count][3] = rs.getString(5);	//street address 
					    data[count][4] = rs.getString(6);   //ZipCode
					    data[count][5] = new Boolean(false);//checkbox select the address for delivery.
					    count++;
				 }while(rs.next());
			 }
			 return data;
		 }
		 }
	public  void createUI(int id, SQL sqlo) throws SQLException
	{
		
		JPanel titlePanel = new JPanel(new GridLayout(1,0));
		titlePanel.add(new JLabel("Address list"));
		JPanel buttonPanel = new JPanel(new GridLayout(1,2,25,25));
	    Listener listener = new Listener();
		ConfirmButton.addActionListener(listener);
		buttonPanel.add(ConfirmButton);
		JFrame frame = new JFrame("Address Page");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame = frame;
        frame.add(titlePanel,BorderLayout.NORTH);
        frame.add(this,BorderLayout.CENTER);
        frame.add(buttonPanel,BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
	}
    static public void invoke(int id, SQL sql,int orderNumber,MainFrame mainFrame) throws SQLException
	{
		addressFrame frame = new addressFrame(id,sql,orderNumber,mainFrame);
		frame.createUI(id, sql);
	}
    private class Listener implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent event) {
    	if(event.getSource() ==  ConfirmButton){
         int flag = 0;
         int selected = -1;
    	 // add jusdgement if there is two selected; 
    	 for (int i =0;i <getSize(rs);i++)
    		 if((boolean) table.getValueAt(i, 5))
    		 {
    			 flag ++;
    			 selected = i;
    		 }
    	 if(flag != 1)
    		 JOptionPane.showMessageDialog(null, "You must select one and only one address to confirm the delivery", 
    				 "Error",JOptionPane.OK_OPTION);
    	 else
    	 {
    		 /*insert into Deliver_To
        		values(addrid, orderNumber, TimeDelivered);*/
    		 Date d = new Date();  
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             String sqlCode = "insert into Deliver_To values("+addrid[selected]+","
             +orderNumber+", \'"+sdf.format(d)+"\');";
        	 sql.WriteExcute(sqlCode);
        	 JOptionPane.showMessageDialog(null, "Your ordered is confirmed. Thank you for your purchase!", 
        			 "Success", JOptionPane.INFORMATION_MESSAGE,new ImageIcon(addressFrame.class.getResource("success.png")));
        	 JOptionPane.showMessageDialog(null, "Your ordered number is "+orderNumber+", please"
        	 		+ "keep it for future refrences.", "Order number", JOptionPane.INFORMATION_MESSAGE);
        	 mainFrame.setVisible(true);
        	 frame.dispose();
        	 }
    	}
     }
}}