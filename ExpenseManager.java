import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class ExpenseManager {

    // Khai báo các thành phần giao diện
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField descriptionField, amountField;
    private JComboBox<String> categoryBox;
    private JLabel totalLabel;
    private double totalExpense = 0.0;

    public ExpenseManager() {
        // Tạo cửa sổ chính
        frame = new JFrame("Quản Lý Chi Tiêu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Tạo panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Tạo bảng hiển thị các khoản chi tiêu
        tableModel = new DefaultTableModel(new String[]{"Mô tả", "Loại", "Số tiền"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // Tạo panel nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));

        // Nhãn và trường nhập cho mô tả chi tiêu
        inputPanel.add(new JLabel("Mô tả:"));
        descriptionField = new JTextField();
        inputPanel.add(descriptionField);

        // Nhãn và danh sách chọn loại chi tiêu
        inputPanel.add(new JLabel("Loại:"));
        categoryBox = new JComboBox<>(new String[]{"Ăn uống", "Di chuyển", "Giải trí", "Khác"});
        inputPanel.add(categoryBox);

        // Nhãn và trường nhập số tiền
        inputPanel.add(new JLabel("Số tiền:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        // Nút thêm chi tiêu
        JButton addButton = new JButton("Thêm chi tiêu");
        addButton.addActionListener(new AddExpenseListener());
        inputPanel.add(addButton);

        // Nút xóa chi tiêu
        JButton deleteButton = new JButton("Xóa chi tiêu");
        deleteButton.addActionListener(new DeleteExpenseListener());
        inputPanel.add(deleteButton);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        // Panel hiển thị tổng số chi tiêu
        JPanel footerPanel = new JPanel();
        totalLabel = new JLabel("Tổng chi tiêu: 0 VND");
        footerPanel.add(totalLabel);
        mainPanel.add(footerPanel, BorderLayout.NORTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    // Lớp xử lý sự kiện thêm chi tiêu
    private class AddExpenseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String description = descriptionField.getText();
            String category = (String) categoryBox.getSelectedItem();
            String amountText = amountField.getText();

            try {
                double amount = Double.parseDouble(amountText);
                tableModel.addRow(new Object[]{description, category, formatCurrency(amount)});
                totalExpense += amount;
                updateTotalLabel();
                clearInputs();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Vui lòng nhập một số tiền hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Xóa các trường nhập liệu sau khi thêm thành công
        private void clearInputs() {
            descriptionField.setText("");
            amountField.setText("");
            categoryBox.setSelectedIndex(0);
        }
    }

    // Lớp xử lý sự kiện xóa chi tiêu
    private class DeleteExpenseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Lấy số tiền từ cột "Số tiền" (cột 2) và chuyển nó thành số thực
                String amountStr = tableModel.getValueAt(selectedRow, 2).toString().replace(",", "").replace(" VND", "");
                double amount = Double.parseDouble(amountStr);

                // Trừ số tiền từ tổng chi tiêu
                totalExpense -= amount;

                // Xóa dòng khỏi bảng
                tableModel.removeRow(selectedRow);

                // Cập nhật tổng chi tiêu
                updateTotalLabel();
            } else {
                JOptionPane.showMessageDialog(frame, "Vui lòng chọn một hàng để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Cập nhật tổng chi tiêu hiển thị
    private void updateTotalLabel() {
        totalLabel.setText(String.format("Tổng chi tiêu: %s", formatCurrency(totalExpense)));
    }

    // Định dạng số thành tiền tệ
    private String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("###,###,### VND");
        return formatter.format(amount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseManager::new);
    }
}
