import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CommandControl {
    public static void CmdReviewMain(String cmd, MainWindow frm){
        switch (cmd) {
            case "Price List" -> new Sender("FtchStrg;", frm);
            case "Orders List" -> new Sender("FtchPrc;", frm);
            case "Add New" -> addNewRow(frm, frm.mainTable);
            case "Mark In Work/Finished" -> changeRow(frm);
            case "Log Out" -> {
                frm.dispose();
                frm.modeCheck = null;
                new LoginWindow();
            }
            default -> new Sender(cmd + ";Incorrect Input", frm);
        }
    }

    public static void CmdReviewLogin(String cmd, String login, String pass, String mode, MainWindow frm){
        switch (cmd) {
            case "Log in" -> new Sender("Auth;" + login + ";" + pass + ";" + mode + ";", frm);
            case "Sign up" -> new Sender("Reg;" + login + ";" + pass + ";" + mode + ";", frm);
            default -> new Sender(cmd + ";Incorrect Input", frm);
        }
    }
    public static void SrvWrdReview(String cmd, MainWindow frm){
        String option = cmd.split(";")[0];

        String[] response = cmd.substring(option.length()+1).split(";");

        switch (option) {
            case "ChkSuc" -> {
                LoginWindow.reg.dispose();
                String mode = response[0];
                new MainWindow(mode);
            }
            case "UsrAdd" -> {
                LoginWindow.reg.dispose();
                JOptionPane.showMessageDialog(null,
                        "User successfully added",
                        "Signup success", JOptionPane.WARNING_MESSAGE);
                String mode = response[0];
                new MainWindow(mode);
            }
            case "UsrExst" -> {
                LoginWindow.reg.dispose();
                JOptionPane.showMessageDialog(null,
                        "User already exists!",
                        "Login Warning", JOptionPane.WARNING_MESSAGE);
                String mode = response[0];
                new MainWindow(mode);
            }
            case "NoUsr" -> JOptionPane.showMessageDialog(null,
                    "No such user exists!",
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            case "PrcInf" -> fillTable(new String[]{"ID", "ITEM", "QUANTITY", "TOTAL PRICE", "STATUS"}, response, frm);
            case "StrgInf" -> fillTable(new String[]{"ID", "NAME", "PRICE"}, response, frm);
        }
    }
    private static void fillTable(String[] columnNames, String[] response, MainWindow frm){
        String[][] info;
        int totalRows = (response.length) / columnNames.length;
        info = new String[totalRows][columnNames.length + 1];
        for(int j = 0; j < totalRows; j++)
            System.arraycopy(response, j * columnNames.length, info[j], 0, columnNames.length);

        frm.mainTable = frm.infoTable(info, columnNames);
        frm.mainScrollPane.setViewportView(frm.mainTable);
    }

    public static void addNewRow(MainWindow frm, JTable table){
            String[] rows = new String[table.getRowCount()];
            String controlValue = String.valueOf((Integer.parseInt(frm.mainTable.getValueAt(frm.mainTable.getRowCount()-1, 0).toString()) + 1));
            rows[0] = controlValue;
            DefaultTableModel model = (DefaultTableModel)table.getModel();
            model.addRow(rows);
            frm.mainTable.setModel(model);
    }

    public static void changeRow(MainWindow frm){
        if (frm.mainTable.getSelectedRow() != -1) {
            DefaultTableModel model = (DefaultTableModel) frm.mainTable.getModel();
            if (model.getValueAt(frm.mainTable.getSelectedRow(), frm.mainTable.getColumnCount()-1).equals("in work")){
                model.setValueAt("finished", frm.mainTable.getSelectedRow(), frm.mainTable.getColumnCount()-1);
            }else if (model.getValueAt(frm.mainTable.getSelectedRow(), frm.mainTable.getColumnCount()-1).equals("finished")){
                model.setValueAt("in work", frm.mainTable.getSelectedRow(), frm.mainTable.getColumnCount()-1);
            }
            convertToRequest(frm.mainTable.getSelectedRow(), frm);
            frm.mainTable.setModel(model);
        }else {
            JOptionPane.showMessageDialog(frm,
                    "No row selected. Try again!",
                    "Selection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void convertToRequest(int rowChanged, MainWindow frm){
        DefaultTableModel model = (DefaultTableModel) frm.mainTable.getModel();
        StringBuilder requestBuilder = null;
        int controlValue = model.getColumnCount();//Integer.parseInt(model.getValueAt(rowChanged, 0).toString());

        if (controlValue == 3) {
            requestBuilder = new StringBuilder("UpdStrg;");
            for (int i = 0 ; i < frm.mainTable.getColumnCount(); i++){
                requestBuilder.append(model.getValueAt(rowChanged, i)).append(";");
            }
            requestBuilder.append(frm.modeCheck).append(";");
        } else if (controlValue == 5) {
            requestBuilder = new StringBuilder("UpdPrc;");
            for (int i = 0 ; i < frm.mainTable.getColumnCount(); i++){
                requestBuilder.append(model.getValueAt(rowChanged, i)).append(";");
            }
        }
        assert false;
        String request = requestBuilder.toString();

        if (!request.contains("null")){
            new Sender(request, frm);
        }
    }
}
