import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener {
    public String modeCheck;
    JButton seePrice = new JButton("Price List");
    JButton seeOrders = new JButton("Orders List");
    JButton logOut = new JButton("Log Out");
    JButton addNew = new JButton("Add New");
    JButton markToggle = new JButton("Mark In Work/Finished");
    JScrollPane mainScrollPane = new JScrollPane();
    JTable mainTable = new JTable();
    MainWindow frm;

    MainWindow(String mode) {
        frm = this;
        modeCheck = mode;
        this.setTitle("Buildings Administration Utility");
        this.setSize(1024, 768);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(null);
        this.setVisible(true);
        this.add(Button(seeOrders, 35, 35, 200));

        if (modeCheck.equals("general")){
            this.add(Button(seePrice, 35, 95, 200));
            this.add(Button(addNew, 274, 650, 120));

            seePrice.addActionListener(this);
            addNew.addActionListener(this);
        }
        else{
            this.add(Button(markToggle, 274,650, 240));
            markToggle.addActionListener(this);
        }

        this.add(Button(logOut, 824, 650, 150));
        this.add(scrollPane(mainScrollPane, 274,35,700,600));

        seeOrders.addActionListener(this);
        logOut.addActionListener(this);
    }

    JButton Button(JButton b, int x, int y, int w){
        b.setBounds(x, y, w, 40);
        b.setFont(new Font("Inter", Font.PLAIN,20));
        b.setForeground(new Color(85, 85, 85));
        b.setBackground(new Color(217, 217, 217));

        return b;
    }

    public JScrollPane scrollPane(JScrollPane sp, int x, int y, int w, int h){
        sp.setBounds(x, y, w, h);
        return sp;
    }

    public JTable infoTable(String[][] info, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNames);
        this.mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        model.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE){
                int rowChanged = e.getLastRow();
                System.out.println("changed");
                CommandControl.convertToRequest(rowChanged, this);
            }
        });
        for (String[] strings : info) {
            model.addRow(strings);
        }
        this.mainTable.setModel(model);
        return this.mainTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        CommandControl.CmdReviewMain(cmd, this);
    }
}
