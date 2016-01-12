import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
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
	private JFormattedTextField formatTxtWalls;
	private JFormattedTextField formatTxtWindows;
	private JFormattedTextField formatTxtDoors;
	private JFormattedTextField formatTxtRoomSize;
	
	//Set prices=> no. of walls, windows, doors, price per metre
	static final int prices[] = {200,100,150, 35};

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
		frmWonderHowMuch = new JFrame();
		frmWonderHowMuch.setTitle("Wonder how much it might cost?");
		frmWonderHowMuch.setBounds(100, 100, 636, 570);
		frmWonderHowMuch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblInput = new JLabel("Enter details here");
		
		JPanel pnlInput = new JPanel();
		
		JButton btnDelete = new JButton("Delete");
		
		JButton btnUncheck = new JButton("Uncheck All");
		
		JButton btnCheck = new JButton("Check All");
		
		JLabel lblEstimatedCost = new JLabel("Estimated Cost: (NZD) $");
		
		//Add Room to list
		JButton btnAdd = new JButton("Add");
		btnAdd.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				String name = txtRoomName.getText();
				String type = (String) cBoxRoomType.getSelectedItem();
				DefaultTableModel tableModel = (DefaultTableModel) tableRoomDisplay.getModel();
				double total = 0;
				
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
						});
						break;
					}
				}
			}
		});
		
		JScrollPane scrollDisplay = new JScrollPane();
		scrollDisplay.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollDisplay.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel pnlDebug = new JPanel();
		
		txtLblEstimatedCost = new JTextField();
		txtLblEstimatedCost.setEnabled(false);
		txtLblEstimatedCost.setEditable(false);
		txtLblEstimatedCost.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(frmWonderHowMuch.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblInput)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(pnlInput, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(pnlDebug, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(19, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(124)
					.addComponent(btnAdd)
					.addContainerGap(449, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollDisplay, GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(15)
					.addComponent(lblEstimatedCost, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(txtLblEstimatedCost, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 99, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnUncheck)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnDelete)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCheck)))
					.addGap(43))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(lblInput)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(pnlDebug, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(pnlInput, GroupLayout.PREFERRED_SIZE, 191, Short.MAX_VALUE))
					.addGap(1)
					.addComponent(btnAdd)
					.addGap(4)
					.addComponent(scrollDisplay, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnDelete)
								.addComponent(btnCheck))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnUncheck))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblEstimatedCost)
							.addComponent(txtLblEstimatedCost, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(97))
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
			gl_pnlInput.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_pnlInput.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlInput.createParallelGroup(Alignment.TRAILING)
						.addComponent(tabbedPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
						.addGroup(gl_pnlInput.createSequentialGroup()
							.addComponent(lblRoomType)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(cBoxRoomType, 0, 180, Short.MAX_VALUE))
						.addGroup(gl_pnlInput.createSequentialGroup()
							.addComponent(lblRoomName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtRoomName, GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)))
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
		
		/*JFormattedTextField*/ formatTxtWalls = new JFormattedTextField( createFormatter( "####" ) );
 formatTxtWalls.setText("0");
 formatTxtWalls.setFocusLostBehavior(JFormattedTextField.COMMIT);
		lblWalls.setLabelFor(formatTxtWalls);
		formatTxtWalls.setHorizontalAlignment(SwingConstants.RIGHT);
		
		/*JFormattedTextField*/ formatTxtWindows = new JFormattedTextField( createFormatter( "####" ) );
 formatTxtWindows.setText("0");
 formatTxtWindows.setFocusLostBehavior(JFormattedTextField.COMMIT);
		lblWindows.setLabelFor(formatTxtWindows);
		formatTxtWindows.setHorizontalAlignment(SwingConstants.RIGHT);
		
		/*JFormattedTextField*/ formatTxtDoors = new JFormattedTextField( createFormatter( "####" ) );
 formatTxtDoors.setText("0");
 formatTxtDoors.setFocusLostBehavior(JFormattedTextField.COMMIT);
		lblDoors.setLabelFor(formatTxtDoors);
		formatTxtDoors.setHorizontalAlignment(SwingConstants.RIGHT);
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
						.addComponent(formatTxtDoors)
						.addComponent(formatTxtWalls, Alignment.TRAILING)
						.addComponent(formatTxtWindows, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE))
					.addContainerGap(38, Short.MAX_VALUE))
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
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
	
	protected MaskFormatter createFormatter( String s )
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
	}	
}

