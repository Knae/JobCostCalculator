import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.io.*;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.FlowLayout;

import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.GridLayout;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.BevelBorder;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;


public class calcMainWindow {

	private JFrame frmWonderHowMuch;
	private JTextField txtRoomName;
	private JTable tableRoomDisplay;
	private JTextField txtTypeDebug;
	private JTextField txtNameDebug;
	private JTextField txtTabDebug;
	private JTextField txtLblEstimatedCost;
	
	//Declared objects for use
	private JComboBox cBoxRoomType;
	private JTabbedPane tabbedPane;
	private JFormattedTextField formatTxtWindows;
	private JFormattedTextField formatTxtWalls;
	private JFormattedTextField formatTxtDoors;
	private JFormattedTextField formatTxtRoomSize;
	private DefaultTableModel tableModel;
	
	//Set prices=> no. of walls, windows, doors, price per metre
	private int prices[] = { 200, 100, 150, 35};

	//Constants
	static Charset usedSet = Charset.forName("UTF-16");
	
	//FileChooser for file manipulation
	final JFileChooser fcSave = new JFileChooser( System.getProperty( "user.home" ) + "/Desktop" );
	final JFileChooser fcOpen = new JFileChooser( System.getProperty( "user.home" ) + "/Desktop" );
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					calcMainWindow window = new calcMainWindow();
					window.frmWonderHowMuch.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public calcMainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//===================================================================
		//Get prices from data file
		//===================================================================
		URL dataFile = calcMainWindow.class.getResource( "Data" );
		File readData = new File( dataFile.getFile() );
		try (
				BufferedReader reader = new BufferedReader( 
											new FileReader( readData )													
										)
				)
		{
		    String line = null;
		    String pricesTemp[] = {"","","",""};
		    while ((line = reader.readLine()) != null) 
		    {
				System.out.println(line);
		        if( !( line.startsWith( "//" ) ) ) {
					pricesTemp = line.split(";");
	        		if ( pricesTemp.length == 4 ) 
	        		{
						for (int i = 0; i < 4; i++)
						{
							prices[ i ] = Integer.parseInt( pricesTemp[ i ] );
							System.out.print( prices[ i ] + ";" );
						}
						System.out.println( "" );
					}
	        		else
	        		{
	        			System.out.println( "Not enough inputs in file " );
	        		}
				}
		    }
		    reader.close();
		} catch (IOException x) 
			{
				//Assuming file doesn't exist, create one
				try(
					BufferedWriter writer = new BufferedWriter( 
												new FileWriter( readData )
												) 
				)
				{
					writer.write( "//walls,windows,doors,pricePerMetre" );
					writer.newLine();
					for (int i = 0; i < 4; i++)
					{
						writer.write( prices[ i ] + ";" );						
					}
					writer.close();
				} catch (IOException e1) {
					System.err.format("IOException: %s%n", x);
				};
				System.out.println( "new data file created" );
			}
		//======================================================================
		frmWonderHowMuch = new JFrame();
		frmWonderHowMuch.setTitle("Wonder how much it might cost?");
		frmWonderHowMuch.setBounds(100, 100, 650, 571);
		frmWonderHowMuch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblInput = new JLabel("Enter details here");
		
		JPanel pnlInput = new JPanel();
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {
			//Delete selected rooms from calculator
			@Override
			public void mousePressed(MouseEvent arg0)
			{	 
				Boolean deleteCheck = false;
				//int debugWatch = tableRoomDisplay.getRowCount();
			initialSearch:
				for( int i = 0; i < tableRoomDisplay.getRowCount(); i++)
				{
					if( ((boolean) (tableRoomDisplay.getValueAt( i, 4))) == true )
					{
						tableModel.removeRow( i );
						deleteCheck = true;
						if( i == tableRoomDisplay.getRowCount() )
							deleteCheck = false;
						break initialSearch;
					}
					else
						deleteCheck = false;
				}
				
				//repeat search, each time a checked box is found
				while( deleteCheck )
				{
				repeatSearch:
					for( int i = 0; i < tableRoomDisplay.getRowCount(); i++)
					{
						if( ((boolean) (tableRoomDisplay.getValueAt( i, 4))) == true )
						{
							tableModel.removeRow( i );
							deleteCheck = true;
							break repeatSearch;
						}
						else
							deleteCheck = false;
					}
				}
				updateEstimate();
			}
		});
		
		JButton btnUncheck = new JButton("Uncheck All");
		btnUncheck.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) 
			{
				for( int i = 0; i < tableRoomDisplay.getRowCount(); i++)
				{
					tableModel.setValueAt( false, i, 4);
				}
			}
		});
		
		JButton btnCheck = new JButton("Check All");
		btnCheck.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				for( int i = 0; i < tableRoomDisplay.getRowCount(); i++)
				{
					tableModel.setValueAt( true, i, 4);
				}
			}
		});
		
		JLabel lblEstimatedCost = new JLabel("Estimated Cost: (NZD)");
		
		//Add Room to list
		JButton btnAdd = new JButton("Add");
		btnAdd.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				String name = txtRoomName.getText();
				String type = (String) cBoxRoomType.getSelectedItem();
				/*tableModel = (DefaultTableModel) tableRoomDisplay.getModel();*/
				double total = 0;
				
				try {
					switch( (tabbedPane.getSelectedIndex() ) ){
						case 0:
						{
							int walls = Integer.parseInt( formatTxtWalls.getText() );
							int windows = Integer.parseInt( formatTxtWindows.getText() );
							int doors = Integer.parseInt( formatTxtDoors.getText() );
							
							total = ( walls * prices[0] ) +
									( windows * prices[1] ) +
									(doors * prices[2] );
							
							tableModel.addRow ( new Object[] {
									name,
									type,
									"Walls: "+walls+",Windows: "+windows+",Doors: "+doors,
									total,
									false,
							});
							break;
						}
						case 1:
						{
							int roomSize = Integer.parseInt( formatTxtRoomSize.getText() );

							total = roomSize * prices[3];
							tableModel.addRow ( new Object[] {
									name,
									type,
									"Size is: "+roomSize+" sq. metres",
									total,
									false,
							});
							break;
						}
					}
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null,
						    "You must only use numbers. Alphabets have no value to me",
						    "Inane error",
						    JOptionPane.ERROR_MESSAGE);
					//e.printStackTrace();
				}
				//update Total estimate
				updateEstimate();
			}
		}
		);
		
		JScrollPane scrollDisplay = new JScrollPane();
		scrollDisplay.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollDisplay.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel pnlDebug = new JPanel();
		
		txtLblEstimatedCost = new JTextField();
		txtLblEstimatedCost.setText("$0.00");
		txtLblEstimatedCost.setEditable(false);
		txtLblEstimatedCost.setColumns(10);
		
		//Save and open files
		//====================================================
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener( new SaveFile() );
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener( new LoadFile() );
		//====================================================
		
		GroupLayout groupLayout = new GroupLayout(frmWonderHowMuch.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnSave)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnLoad)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDelete))
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(btnUncheck)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnCheck))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(pnlInput, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(pnlDebug, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE))))
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(124)
								.addComponent(btnAdd))
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(20)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addComponent(scrollDisplay, GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblEstimatedCost, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(txtLblEstimatedCost, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE))))))
					.addGap(47))
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblInput)
					.addContainerGap(547, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblInput)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(pnlInput, GroupLayout.PREFERRED_SIZE, 214, Short.MAX_VALUE)
							.addGap(1))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(pnlDebug, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAdd)
						.addComponent(btnCheck)
						.addComponent(btnUncheck))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollDisplay, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDelete)
						.addComponent(btnLoad)
						.addComponent(btnSave)
						.addComponent(lblEstimatedCost)
						.addComponent(txtLblEstimatedCost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(136))
		);
		
		
		tableRoomDisplay = new JTable();
		scrollDisplay.setViewportView(tableRoomDisplay);
		tableRoomDisplay.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		tableRoomDisplay.setRowSelectionAllowed(false);
		tableRoomDisplay.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Type", "Surfaces/Measurements", "Total", "Delete?"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, Double.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		//==========================================================
		tableModel = (DefaultTableModel) tableRoomDisplay.getModel();
		//===========================================================
		
		tableRoomDisplay.getColumnModel().getColumn(0).setResizable(false);
		tableRoomDisplay.getColumnModel().getColumn(0).setPreferredWidth(90);
		tableRoomDisplay.getColumnModel().getColumn(0).setMinWidth(45);
		tableRoomDisplay.getColumnModel().getColumn(0).setMaxWidth(90);
		tableRoomDisplay.getColumnModel().getColumn(1).setResizable(false);
		tableRoomDisplay.getColumnModel().getColumn(1).setPreferredWidth(90);
		tableRoomDisplay.getColumnModel().getColumn(1).setMinWidth(20);
		tableRoomDisplay.getColumnModel().getColumn(1).setMaxWidth(90);
		tableRoomDisplay.getColumnModel().getColumn(2).setResizable(false);
		tableRoomDisplay.getColumnModel().getColumn(2).setPreferredWidth(190);
		tableRoomDisplay.getColumnModel().getColumn(2).setMinWidth(190);
		tableRoomDisplay.getColumnModel().getColumn(3).setPreferredWidth(100);
		tableRoomDisplay.getColumnModel().getColumn(3).setMinWidth(50);
		tableRoomDisplay.getColumnModel().getColumn(3).setMaxWidth(150);
		tableRoomDisplay.getColumnModel().getColumn(4).setResizable(false);
		tableRoomDisplay.getColumnModel().getColumn(4).setPreferredWidth(50);
		tableRoomDisplay.getColumnModel().getColumn(4).setMinWidth(50);
		tableRoomDisplay.getColumnModel().getColumn(4).setMaxWidth(50);
		
		JLabel lblRoomType = new JLabel("Room Type");
		
		/*JTabbedPane*/ tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		/*JComboBox*/ cBoxRoomType = new JComboBox();

		cBoxRoomType.setModel(new DefaultComboBoxModel(new String[] {"", "Bathroom", "Bedroom", "Garage", "Kitchen", "Living Room", "Store Room", "Toilet"}));
		
		JLabel lblRoomName = new JLabel("Name for it:");
		
		txtRoomName = new JTextField();
		txtRoomName.setColumns(10);
		GroupLayout gl_pnlInput = new GroupLayout(pnlInput);
		gl_pnlInput.setHorizontalGroup(
			gl_pnlInput.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_pnlInput.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlInput.createParallelGroup(Alignment.TRAILING)
						.addComponent(tabbedPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_pnlInput.createSequentialGroup()
							.addGroup(gl_pnlInput.createParallelGroup(Alignment.LEADING)
								.addComponent(lblRoomType)
								.addComponent(lblRoomName))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_pnlInput.createParallelGroup(Alignment.LEADING)
								.addComponent(txtRoomName, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
								.addComponent(cBoxRoomType, 0, 180, Short.MAX_VALUE))))
					.addContainerGap())
		);
		gl_pnlInput.setVerticalGroup(
			gl_pnlInput.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInput.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlInput.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRoomType)
						.addComponent(cBoxRoomType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlInput.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRoomName)
						.addComponent(txtRoomName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 129, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JPanel tabInputSurfaces = new JPanel();
		tabbedPane.addTab("No. of Surfaces", null, tabInputSurfaces, null);
		tabbedPane.setEnabledAt(0, true);
		
		JLabel lblWalls = new JLabel("Walls:");
		
		JLabel lblWindows = new JLabel("Windows:");
		
		JLabel lblDoors = new JLabel("Doors/Doorways:");
		
		/*JFormattedTextField*/ formatTxtWindows = new JFormattedTextField( /*createFormatter( "####" )*/ );
		formatTxtWindows.setText("0");
		formatTxtWindows.setFocusLostBehavior(JFormattedTextField.COMMIT);
		lblWindows.setLabelFor(formatTxtWindows);
		formatTxtWindows.setHorizontalAlignment(SwingConstants.RIGHT);
		
		/*JFormattedTextField*/ formatTxtDoors = new JFormattedTextField( /*createFormatter( "####" )*/ );
		formatTxtDoors.setText("0");
		formatTxtDoors.setFocusLostBehavior(JFormattedTextField.COMMIT);
		lblDoors.setLabelFor(formatTxtDoors);
		formatTxtDoors.setHorizontalAlignment(SwingConstants.RIGHT);
		
		/*JFormattedTextField*/ formatTxtWalls = new JFormattedTextField();
		formatTxtWalls.setHorizontalAlignment(SwingConstants.RIGHT);
		formatTxtWalls.setFocusLostBehavior(JFormattedTextField.PERSIST);
		formatTxtWalls.setText("0");
		GroupLayout gl_tabInputSurfaces = new GroupLayout(tabInputSurfaces);
		gl_tabInputSurfaces.setHorizontalGroup(
			gl_tabInputSurfaces.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabInputSurfaces.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabInputSurfaces.createParallelGroup(Alignment.LEADING)
						.addComponent(lblWalls)
						.addComponent(lblWindows)
						.addComponent(lblDoors))
					.addGap(47)
					.addGroup(gl_tabInputSurfaces.createParallelGroup(Alignment.LEADING, false)
						.addComponent(formatTxtWalls)
						.addComponent(formatTxtDoors)
						.addComponent(formatTxtWindows, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
					.addContainerGap(59, Short.MAX_VALUE))
		);
		gl_tabInputSurfaces.setVerticalGroup(
			gl_tabInputSurfaces.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabInputSurfaces.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabInputSurfaces.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWalls)
						.addComponent(formatTxtWalls, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_tabInputSurfaces.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblWindows)
						.addComponent(formatTxtWindows, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_tabInputSurfaces.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDoors)
						.addComponent(formatTxtDoors, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		tabInputSurfaces.setLayout(gl_tabInputSurfaces);
		
		JPanel tabInputMeasurements = new JPanel();
		tabbedPane.addTab("Measurements", null, tabInputMeasurements, null);
		tabbedPane.setEnabledAt(1, true);
		
		JLabel lblRoomSize = new JLabel("Estimated Room Size in sq metres");
		
		/*JFormattedTextField*/ formatTxtRoomSize = new JFormattedTextField();
 formatTxtRoomSize.setText("0");
		formatTxtRoomSize.setFocusLostBehavior(JFormattedTextField.COMMIT);
		formatTxtRoomSize.setHorizontalAlignment(SwingConstants.RIGHT);
		GroupLayout gl_tabInputMeasurements = new GroupLayout(tabInputMeasurements);
		gl_tabInputMeasurements.setHorizontalGroup(
			gl_tabInputMeasurements.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabInputMeasurements.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabInputMeasurements.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(formatTxtRoomSize, Alignment.LEADING)
						.addComponent(lblRoomSize, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		gl_tabInputMeasurements.setVerticalGroup(
			gl_tabInputMeasurements.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabInputMeasurements.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblRoomSize)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(formatTxtRoomSize, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(36, Short.MAX_VALUE))
		);
		tabInputMeasurements.setLayout(gl_tabInputMeasurements);
		pnlInput.setLayout(gl_pnlInput);
		frmWonderHowMuch.getContentPane().setLayout(groupLayout);
		
		//=================================================
		//Debug
		JLabel lblTypeDebug = new JLabel("Selected Type");
		
		JLabel lblNameDebug = new JLabel("Given Name");
		
		JLabel lblTabDebug = new JLabel("Selected Tab");
		
		txtTypeDebug = new JTextField();
		txtTypeDebug.setEditable(false);
		txtTypeDebug.setColumns(10);
		cBoxRoomType.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) 
			{
				txtTypeDebug.setText((String)( cBoxRoomType.getSelectedItem() ));
			}
		});
		
		txtNameDebug = new JTextField();
		txtNameDebug.setEditable(false);
		txtNameDebug.setColumns(10);
		txtRoomName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtNameDebug.setText( (String) (txtRoomName.getText()) ); 
			}
		});
		
		txtTabDebug = new JTextField();
		txtTabDebug.setEditable(false);
		txtTabDebug.setColumns(10);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0)
			{
				txtTabDebug.setText( String.valueOf( (tabbedPane.getSelectedIndex() )));
			}
		});
		GroupLayout gl_pnlDebug = new GroupLayout(pnlDebug);
		gl_pnlDebug.setHorizontalGroup(
			gl_pnlDebug.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlDebug.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlDebug.createParallelGroup(Alignment.LEADING)
						.addComponent(lblTypeDebug)
						.addComponent(lblNameDebug)
						.addComponent(lblTabDebug))
					.addGap(56)
					.addGroup(gl_pnlDebug.createParallelGroup(Alignment.LEADING, false)
						.addComponent(txtTabDebug)
						.addComponent(txtTypeDebug, Alignment.TRAILING)
						.addComponent(txtNameDebug, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
					.addContainerGap(34, Short.MAX_VALUE))
		);
		gl_pnlDebug.setVerticalGroup(
			gl_pnlDebug.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlDebug.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlDebug.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTypeDebug)
						.addComponent(txtTypeDebug, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlDebug.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNameDebug)
						.addComponent(txtNameDebug, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlDebug.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTabDebug)
						.addComponent(txtTabDebug, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(126, Short.MAX_VALUE))
		);
		pnlDebug.setLayout(gl_pnlDebug);
		//==================================================
	}
	
/*	protected MaskFormatter createFormatter( String s )
	{
		MaskFormatter formatter = null;
		try 
		{
			formatter = new MaskFormatter( s );
		} catch( java.text.ParseException exc ) 
			{
				System.err.println("Bad Input: " + exc.getMessage() );
				System.exit( -1 );
			}
		return formatter;
	}*/
	
	private void updateEstimate()
	{
	//update Total estimate
	//int totalRooms = tableRoomDisplay.getRowCount();
		double currentTotalEstimate = 0;
		DecimalFormat displayFormat = new DecimalFormat("$#.00");
		displayFormat.setGroupingUsed(true);
		displayFormat.setGroupingSize(3);
		
		for( int i = 0; i < tableRoomDisplay.getRowCount(); i++ )
		{
			//double debugValue = (double) tableRoomDisplay.getValueAt( i , 3 );
			//get the total of each room, removing commas and symbols
			currentTotalEstimate += ( (double) ( tableRoomDisplay.getValueAt( i , 3 ) ) );
		}
		String costText = displayFormat.format( currentTotalEstimate );
	
		txtLblEstimatedCost.setText( costText );
	}
	
	class SaveFile implements ActionListener {
		public void actionPerformed( ActionEvent e ) {
			int returnValueFileChooser = fcSave.showSaveDialog( null );
			
			if( returnValueFileChooser == JFileChooser.APPROVE_OPTION )
			{
				File saveTarget = fcSave.getSelectedFile();
				
				try( BufferedWriter writer = new BufferedWriter( new FileWriter( saveTarget )) ) 
				{
					for( int i = 0; i < tableRoomDisplay.getRowCount(); i++)
					{	
						/*name,
						type,
						description
						total,
						delete?,*/
						RoomObject tempRoom = new RoomObject( 
								(String) tableRoomDisplay.getValueAt( i, 0),
								(String) tableRoomDisplay.getValueAt( i, 1),
								(String) tableRoomDisplay.getValueAt( i, 2),
								(double) tableRoomDisplay.getValueAt( i, 3)
								);
						writer.write( 
								tempRoom.getName() + ";" +
								tempRoom.getType() + ";" +
								tempRoom.getDesc() + ";" +
								tempRoom.getTotal() + ";"
								);
						writer.newLine();

					}
				} catch (IOException x) {
					System.err.format("IOException: %s%n", x);
				} 
				
				System.out.println("Saved to: " + saveTarget.getAbsolutePath());
			}
		}
	}
	
	class LoadFile implements ActionListener {
		public void actionPerformed( ActionEvent e ) {
			int returnValueFileChooser = fcOpen.showOpenDialog( null );
			
			if( returnValueFileChooser == JFileChooser.APPROVE_OPTION )
			{
				File openTarget = fcOpen.getSelectedFile();
				try ( BufferedReader reader = new BufferedReader( new FileReader( openTarget ))	)
				{
				    String line = null;
				    while ((line = reader.readLine()) != null) 
				    {
				        if( line != "" )
				        {
				        	String tempSpace[] = { "", "", "", ""};
				        	tempSpace = line.split( ";" );
				        	
				        	tableModel.addRow ( new Object[] {
									tempSpace[0],
									tempSpace[1],
									tempSpace[2],
									Double.valueOf( tempSpace[3] ),
									false,
							});
				        }
				    }
				    reader.close();
				} catch (IOException x) 
					{
					    System.err.format("IOException: %s%n", x);
					}
				System.out.println("Open From : " + openTarget.getAbsolutePath());
			}
		}
	}
}


