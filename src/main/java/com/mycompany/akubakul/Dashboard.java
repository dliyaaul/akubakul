package com.mycompany.akubakul;

import cafeguii.admin.Bahan_stock;
import cafeguii.koneksi.koneksi;
import cafeguii.login.Login_admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

class Produk {

    public static String getKuantitasBahan() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private String NamaProduk;
    private double HargaProduk;
    private String BahanKuantitas;
    private ImageIcon Gambar;
    private int CodeProduk;

    public Produk(String NamaProduk, double HargaProduk, String imagePath, String BahanKuantitas, int CodeProduk) {
        this.NamaProduk = NamaProduk;
        this.HargaProduk = HargaProduk;
        this.Gambar = createImageIcon(imagePath);
        this.BahanKuantitas = BahanKuantitas;
        this.CodeProduk = CodeProduk;
    }

    public String getNamaProduk() {
        return NamaProduk;
    }

    public int getCodeProduk() {
        return CodeProduk;
    }

    public String getBahanKuantitas() {
        return BahanKuantitas;
    }

    public double getHargaProduk() {
        return HargaProduk;
    }

    public ImageIcon getGambar() {
        return Gambar;
    }

    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}

public final class Dashboard extends javax.swing.JFrame {

    private JButton btnLogin;
    private double total = 0.0;
    private int x = 0;
    private double tax = 0.0;
    private Produk[] items;
    private String currentPurchaseId;

    public Dashboard() {
        initComponents();
        makeWindowedFullscreen();
        loadKolom();
        LiveTimeDisplay();
        tableProduk.setModel(model);
        conn = koneksi.bukaKoneksi();
        daftarProduk = new ArrayList<>();
        loadProduk();
        loadIsiTbel();
        addSpinnerEditorToTable();
        addButtonColumnToTable();

        btn_bayar.setEnabled(false);
        totalButton.setEnabled(false);
        // Tambahkan listener untuk validasi checkbox
        hideColumn(tableProduk, 5);
        hideColumn(tableProduk, 6);
        showcolumn1(tableProduk, 0, 5);
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 0) { // Kolom "Check"
                    int row = e.getFirstRow();
                    boolean isChecked = (Boolean) model.getValueAt(row, 0);
                    int quantity = (Integer) model.getValueAt(row, 4);
                    if (isChecked && quantity == 0) {
                        JOptionPane.showMessageDialog(null, "Jumlah belum ditambahkan. Harap tambahkan jumlah terlebih dahulu.");
                        model.setValueAt(false, row, 0); // Batalkan centang
                    }
                }
            }
        });
    }

    private void makeWindowedFullscreen() {
        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Set the size of the frame to the screen size
        setSize(screenSize);
        // Set the frame to maximized state
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Ensure the frame is visible
        setVisible(true);
    }

    private void LiveTimeDisplay() {

        // Timer untuk memperbarui waktu setiap detik
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Format waktu
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentTime = sdf.format(new Date());
                // Set waktu ke JLabel
                jButtonnnnnnnnn.setText(currentTime);
            }
        });

        // Mulai timer
        timer.start();
    }

    private void hideColumn(JTable table, int columnIndex) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
    }

    private void showcolumn1(JTable table, int columnIndex, int preferredWidth) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(5); // atau ukuran min width yang diinginkan
        column.setMaxWidth(Integer.MAX_VALUE);
        column.setPreferredWidth(preferredWidth);
    }

    private DefaultTableModel model = new DefaultTableModel();
    private Connection conn;
    private ArrayList<Produk> daftarProduk;

    private void loadKolom() {
        model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                } else if (columnIndex == 4) {
                    return Integer.class; // Kolom "Jumlah" akan menggunakan JSpinner
                } else if (columnIndex == 3) { // Kolom untuk tombol
                    return JButton.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 0 || columnIndex == 4 || columnIndex == 3; // hanya kolom check dan Jumlah yang bisa di-edit
            }
        };
        model.addColumn("Check");
        model.addColumn("Nama");
        model.addColumn("Harga");
        model.addColumn("Bahan");
        model.addColumn("Jumlah");
        model.addColumn("...");
        model.addColumn("id");
    }

    private void loadProduk() {
        if (conn != null) {
            daftarProduk = new ArrayList<>();
            String kueri = "SELECT " +
                "ANY_VALUE(p.NamaProduk) AS NamaProduk, " +
                "ANY_VALUE(p.HargaProduk) AS HargaProduk, " +
                "ANY_VALUE(p.Gambar) AS Gambar, " +
                "GROUP_CONCAT(CONCAT(b.NamaBahan, ' (', COALESCE(bp.Kuantitas, 0), 'x)') SEPARATOR '\\n') AS BahanKuantitas, " +
                "p.CodeProduk " +
                "FROM produk p " +
                "LEFT JOIN `bahan&produk` bp ON p.CodeProduk = bp.CodeProduk " +
                "LEFT JOIN stokbahan b ON bp.KodeDetailBahan = b.KodeBahan " +
                "GROUP BY p.CodeProduk";
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String NamaProduk = rs.getString("NamaProduk");
                    double HargaProduk = rs.getDouble("HargaProduk");
                    String imagePath = rs.getString("Gambar");
                    String BahanKuantitas = rs.getString("BahanKuantitas");
                    int CodeProduk = rs.getInt("CodeProduk");
                    Produk produk = new Produk(NamaProduk, HargaProduk, imagePath, BahanKuantitas, CodeProduk);
                    daftarProduk.add(produk);
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(Bahan_stock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void loadIsiTbel() {
        model.setRowCount(0);
        for (Produk b : daftarProduk) {
            model.addRow(new Object[]{false, b.getNamaProduk(), "Rp. " + b.getHargaProduk(), "Selengkapnya", 0, b.getBahanKuantitas(), b.getCodeProduk()});
        }
    }

    // Metode untuk menambahkan JSpinner sebagai editor kolom "Jumlah"
    private void addSpinnerEditorToTable() {
        TableColumn column = tableProduk.getColumnModel().getColumn(4);
        column.setCellEditor(new SpinnerEditor());
    }

    private void addButtonColumnToTable() {
        TableColumn column = tableProduk.getColumnModel().getColumn(3);
        column.setCellRenderer(new ButtonRenderer());
        column.setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {

        private final JSpinner spinner;

        public SpinnerEditor() {
            spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            spinner.setValue(table.getValueAt(row, 4));
            return spinner;
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {

        private final JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                JOptionPane.showMessageDialog(button, "Bahan " + model.getValueAt(tableProduk.getSelectedRow(), 1) + ": \n" + model.getValueAt(tableProduk.getSelectedRow(), 5), "Detail Bahan", JOptionPane.INFORMATION_MESSAGE);
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    private String generatePurchaseId() {
        // Generate random purchase ID
        Random rand = new Random();
        int purchaseId = rand.nextInt(100000);
        return String.format("%06d", purchaseId);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTxtDate = new javax.swing.JPanel();
        btn_logout = new javax.swing.JButton();
        jButtonnnnnnnnn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();
        taxField = new javax.swing.JTextField();
        subTotalField = new javax.swing.JTextField();
        totalField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        btn_bayar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableProduk = new javax.swing.JTable();
        txt_cari = new javax.swing.JTextField();
        btn_cari = new javax.swing.JButton();
        Logout = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        cetakStruk = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        totalButton = new javax.swing.JButton();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(1, 2));
        setSize(new java.awt.Dimension(1220, 800));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setPreferredSize(new java.awt.Dimension(1360, 800));

        jPanel2.setBackground(new java.awt.Color(230, 230, 230));

        jLabel1.setFont(new java.awt.Font("Segoe Script", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("AKU BAKUL COFFESHOP");

        jTxtDate.setBackground(new java.awt.Color(153, 153, 153));

        btn_logout.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_logout.setText("Logout");
        btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jTxtDateLayout = new javax.swing.GroupLayout(jTxtDate);
        jTxtDate.setLayout(jTxtDateLayout);
        jTxtDateLayout.setHorizontalGroup(
            jTxtDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_logout, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );
        jTxtDateLayout.setVerticalGroup(
            jTxtDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btn_logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jButtonnnnnnnnn.setFont(new java.awt.Font("SF UI  Text 2", 0, 24)); // NOI18N
        jButtonnnnnnnnn.setText("00:00:00");
        jButtonnnnnnnnn.setToolTipText("");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(539, 539, 539)
                .addComponent(jLabel1)
                .addGap(346, 346, 346)
                .addComponent(jButtonnnnnnnnn, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTxtDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonnnnnnnnn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTxtDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(250, 250, 250));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 230, 230), 2));

        jLabel4.setFont(new java.awt.Font("SF UI  Text", 0, 14)); // NOI18N
        jLabel4.setText("Cari Nama :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(272, 272, 272))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));

        jTextArea.setColumns(20);
        jTextArea.setRows(5);
        jScrollPane1.setViewportView(jTextArea);

        taxField.setEditable(false);
        taxField.setText("0");
        taxField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                taxFieldActionPerformed(evt);
            }
        });

        subTotalField.setEditable(false);
        subTotalField.setText("0");
        subTotalField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subTotalFieldActionPerformed(evt);
            }
        });

        totalField.setEditable(false);
        totalField.setText("0");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Tax");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setText("Total");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("Sub Total");

        btn_bayar.setText("Bayar");
        btn_bayar.setToolTipText("");
        btn_bayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bayarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_bayar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(taxField)
                            .addComponent(totalField)
                            .addComponent(subTotalField))))
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(taxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(subTotalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(totalField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setForeground(new java.awt.Color(102, 102, 102));

        jLabel2.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("MENU");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(723, 723, 723)
                .addComponent(jLabel2)
                .addContainerGap(1766, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jScrollPane2.setToolTipText("");

        tableProduk.setFont(new java.awt.Font("Segoe Print", 1, 12)); // NOI18N
        tableProduk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Check", "id", "Nama", "Harga", "Bahan", "Jumlah"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableProduk.setAlignmentX(0.0F);
        tableProduk.setAlignmentY(0.0F);
        jScrollPane2.setViewportView(tableProduk);
        if (tableProduk.getColumnModel().getColumnCount() > 0) {
            tableProduk.getColumnModel().getColumn(0).setPreferredWidth(50);
            tableProduk.getColumnModel().getColumn(0).setMaxWidth(50);
            tableProduk.getColumnModel().getColumn(1).setPreferredWidth(30);
            tableProduk.getColumnModel().getColumn(1).setMaxWidth(30);
            tableProduk.getColumnModel().getColumn(3).setPreferredWidth(200);
            tableProduk.getColumnModel().getColumn(3).setMaxWidth(200);
            tableProduk.getColumnModel().getColumn(4).setPreferredWidth(150);
            tableProduk.getColumnModel().getColumn(4).setMaxWidth(150);
            tableProduk.getColumnModel().getColumn(5).setPreferredWidth(100);
            tableProduk.getColumnModel().getColumn(5).setMaxWidth(100);
        }

        txt_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_cariActionPerformed(evt);
            }
        });

        btn_cari.setText("Cari");
        btn_cari.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btn_cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cariActionPerformed(evt);
            }
        });

        Logout.setText("Logout");
        Logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btn_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_cari, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1205, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Logout))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(916, 916, 916))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_cari)
                            .addComponent(txt_cari)
                            .addComponent(Logout))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel5.setAlignmentX(0.0F);
        jPanel5.setAlignmentY(0.0F);

        cetakStruk.setText("Masukkan Pesanan");
        cetakStruk.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 3));
        cetakStruk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakStrukActionPerformed(evt);
            }
        });

        resetButton.setText("Reset");
        resetButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 3));
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        totalButton.setText(" Checkout");
        totalButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 3));
        totalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(totalButton, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cetakStruk, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1013, Short.MAX_VALUE)
                .addComponent(resetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(resetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(totalButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(cetakStruk, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1536, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 761, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void taxFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_taxFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_taxFieldActionPerformed

    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_logoutActionPerformed

    }//GEN-LAST:event_btn_logoutActionPerformed

//    private String getPurchaseId() {
//        String purchaseId = "";
//        Connection koneksi = null;
//        try {
//            koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root", "Opoae_123");
//            String sql = "SELECT Purchase_id FROM pesanan ORDER BY Purchase_id DESC LIMIT 1";
//            try (Statement stmt = koneksi.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
//                if (rs.next()) {
//                    purchaseId = rs.getString("Purchase_id");
//                }
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } finally {
//            // Tutup koneksi di blok finally untuk memastikan ditutupnya
//            if (koneksi != null) {
//                try {
//                    koneksi.close();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//        return purchaseId;
//    }
    public class StokNega {

        private int NamaProduk;
        private int HargaProduk;
        private int BahanKuantitas;
        private int Gambar;
        private int CodeProduk;

        public StokNega(int NamaProduk, int HargaProduk, int Gambar, int BahanKuantitas, int CodeProduk) {
            this.NamaProduk = NamaProduk;
            this.HargaProduk = HargaProduk;
            this.Gambar = Gambar;
            this.BahanKuantitas = BahanKuantitas;
            this.CodeProduk = CodeProduk;
        }

        public int getNamaProduk() {
            return NamaProduk;
        }

        public int getCodeProduk() {
            return CodeProduk;
        }

        public int getBahanKuantitas() {
            return BahanKuantitas;
        }

        public int getHargaProduk() {
            return HargaProduk;
        }

        public int getGambar() {
            return Gambar;
        }
    }
    private void btn_bayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_bayarActionPerformed
        double pajak = Double.parseDouble(taxField.getText());
        double subTotal = Double.parseDouble(subTotalField.getText());
        double totalHarga = Double.parseDouble(totalField.getText());
        String purchaseId = currentPurchaseId;
        int Karyawan = -99;

        String input = JOptionPane.showInputDialog(this, "Masukkan jumlah pembayaran:");
        double pembayaran = 0.0;
        try {
            pembayaran = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input tidak valid. Masukkan angka yang valid.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pembayaran < totalHarga) {
            JOptionPane.showMessageDialog(this, "Uang pembayaran kurang.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double kembalian = pembayaran - totalHarga;
        JOptionPane.showMessageDialog(this, "Kembalian Anda: " + kembalian);

        boolean isStockSufficient = true;

        try (Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root", "Opoae_123")) {
            String checkSql = "SELECT COUNT(*) FROM pesanan WHERE Purchase_id = ?";
            try (PreparedStatement checkStmt = koneksi.prepareStatement(checkSql)) {
                checkStmt.setString(1, purchaseId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(this, "Purchase ID tidak ditemukan di tabel pesanan.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Keluar dari metode jika purchaseId tidak ditemukan
                }
            }

            ArrayList<StokNega> Stoklist = new ArrayList<>();
            String DcStok = "SELECT subquery.CodeProduk, subquery.produkBahanKuantitas, subquery.KodeBahan, subquery.Stok, (subquery.Stok - subquery.produkBahanKuantitas) AS Hasil FROM ( SELECT p.CodeProduk, (ps.Kuantitas * pb.Kuantitas) AS produkBahanKuantitas, b.KodeBahan, b.Stok FROM pesanan ps JOIN produk p ON p.CodeProduk = ps.CodeProduk JOIN `bahan&produk` pb ON p.CodeProduk = pb.CodeProduk JOIN stokbahan b ON b.KodeBahan = pb.KodeDetailBahan WHERE ps.Purchase_id = ? ) AS subquery;";
            try (PreparedStatement ppp = koneksi.prepareStatement(DcStok)) {
                ppp.setString(1, purchaseId);
                ResultSet rs = ppp.executeQuery();
                while (rs.next()) {
                    int NamaProduk = rs.getInt("CodeProduk");
                    int produkBahanKuantitas = rs.getInt("produkBahanKuantitas");
                    int Gambar = rs.getInt("KodeBahan");
                    int BahanKuantitas = rs.getInt("Hasil");
                    int CodeProduk = rs.getInt("Stok");
                    StokNega stoknega = new StokNega(NamaProduk, produkBahanKuantitas, Gambar, BahanKuantitas, CodeProduk);
                    Stoklist.add(stoknega);
                    if (BahanKuantitas < 0) {
                        isStockSufficient = false;
                        JOptionPane.showMessageDialog(this, "Stok anda kurang", "Error", JOptionPane.ERROR_MESSAGE);
                        resetFields();
                        return;
                    } else {
                        String updateSql = "UPDATE stokbahan SET Stok = ? WHERE KodeBahan = ?";
                        try (PreparedStatement updateStmt = koneksi.prepareStatement(updateSql)) {
                            updateStmt.setInt(1, BahanKuantitas);
                            updateStmt.setInt(2, Gambar);
                            updateStmt.executeUpdate();
                            
                        }
                    }
                }
            }

            if (isStockSufficient) {
                String KaryawanQuery = "SELECT * FROM karyawangetter";
                try (PreparedStatement ps = koneksi.prepareStatement(KaryawanQuery)) {
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int ID = rs.getInt("ID"); // Assuming "ID" is the column name in your database
                        // Here you can use the retrieved ID or assign it to your variable
                        Karyawan = ID;
                    } else {
                        // Handle case when no rows are returned
                        JOptionPane.showMessageDialog(this, "No data found in karyawangetter table.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Failed to execute query: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                String sql = "INSERT INTO detailpenjualan (CodePesanan, Pajak, Total, Bayar, Kembali, SubTotal) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = koneksi.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, purchaseId);
                    stmt.setDouble(2, pajak);
                    stmt.setDouble(3, totalHarga);
                    stmt.setDouble(4, pembayaran);
                    stmt.setDouble(5, kembalian);
                    stmt.setDouble(6, subTotal);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Data berhasil dimasukkan ke detailpenjualan.");

                    // iddetailpenjualan yang dihasilkan
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        long idDetailPenjualan = generatedKeys.getLong(1);

                        // HISTORY PENJUALAN
                        String historySql = "INSERT INTO historypenjualan (Codepenjualan, KodeKaryawan) VALUES (?, ?)";
                        try (PreparedStatement historyStmt = koneksi.prepareStatement(historySql)) {
                            historyStmt.setLong(1, idDetailPenjualan);
                            historyStmt.setInt(2, Karyawan);
                            historyStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memasukkan data ke detailpenjualan.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        resetFields();
    }//GEN-LAST:event_btn_bayarActionPerformed

    private void txt_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_cariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_cariActionPerformed

    private void btn_cariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cariActionPerformed
        // Mendapatkan kata kunci pencarian
        String keyword = txt_cari.getText().toLowerCase();

        // Membuat RowSorter untuk model tabel
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableProduk.getModel());
        tableProduk.setRowSorter(sorter);

        // Menerapkan filter berdasarkan kata kunci hanya pada kolom "Nama Produk"
        if (keyword.length() == 0) {
            // Jika kotak pencarian kosong, hapus semua filter
            sorter.setRowFilter(null);
        } else {
            // Jika ada kata kunci, terapkan filter hanya pada kolom "Nama Produk"
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 1)); // Indeks 2 adalah kolom "Nama Produk"
        }
    }//GEN-LAST:event_btn_cariActionPerformed

    private void subTotalFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subTotalFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subTotalFieldActionPerformed

    private void totalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalButtonActionPerformed
        double subtotal = 0.0;
        int totalQuantity = 0;
        double taxRate = 0.1; // Assuming 10% tax rate
        double tax = 0.0;
        double total = 0.0;
        currentPurchaseId = generatePurchaseId();
        StringBuilder itemIds = new StringBuilder();

        try (Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root", "Opoae_123")) {
            for (int i = 0; i < tableProduk.getRowCount(); i++) {
                boolean isChecked = (Boolean) tableProduk.getValueAt(i, 0);
                if (isChecked) {
                    int id = (Integer) tableProduk.getValueAt(i, 6);
                    int quantity = (Integer) tableProduk.getValueAt(i, 4);
                    totalQuantity += quantity;
                    String priceStr = (String) tableProduk.getValueAt(i, 2); // Harga berada di kolom 2
                    priceStr = priceStr.replace("Rp. ", "").replace(",", ""); // Menghilangkan "Rp. " dan koma jika ada
                    double price = Double.parseDouble(priceStr); //
                    subtotal += price * quantity;
                    itemIds.append(id);

                    String sql = "INSERT INTO pesanan (CodeProduk, Kuantitas, Purchase_id) VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = koneksi.prepareStatement(sql)) {
                        stmt.setInt(1, id);
                        stmt.setInt(2, quantity);
                        stmt.setString(3, currentPurchaseId);
                        stmt.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            tax = subtotal * taxRate;
            total = subtotal + tax;
            // Mengunci tabel produk
            jTextArea.setEditable(false); // Menonaktifkan pengeditan
            tableProduk.setEnabled(false);
            cetakStruk.setEnabled(false);
            resetButton.setEnabled(false);
            btn_bayar.setEnabled(true);
            // Menampilkan hasil perhitungan di JTextField
            subTotalField.setText(String.valueOf(subtotal));
            taxField.setText(String.valueOf(tax));
            totalField.setText(String.valueOf(total));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_totalButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // Mengatur ulang semua checkbox ke false
        for (int row = 0; row < tableProduk.getRowCount(); row++) {
            tableProduk.setValueAt(false, row, 0);
        }

        // Mengatur ulang nilai spinner menjadi 0
        for (int row = 0; row < tableProduk.getRowCount(); row++) {
            tableProduk.setValueAt(0, row, 4); // Kolom "Jumlah" berada di indeks 5

            // Memastikan JSpinner diatur ke nilai defaultnya (0)
            JSpinner spinner = (JSpinner) tableProduk.getCellEditor(row, 4).getTableCellEditorComponent(tableProduk, null, true, row, 4);
            spinner.setValue(0);
        }

        totalButton.setEnabled(false);
        jTextArea.setText("");
    }//GEN-LAST:event_resetButtonActionPerformed

    private void cetakStrukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakStrukActionPerformed
        boolean anyChecked = false;
        for (int i = 0; i < tableProduk.getRowCount(); i++) {
            if ((Boolean) tableProduk.getValueAt(i, 0)) {
                anyChecked = true;
                break;
            }
        }

        if (!anyChecked) {
            JOptionPane.showMessageDialog(null, "Tidak ada produk yang dipilih untuk dicetak struk.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        StringBuilder struk = new StringBuilder();
        struk.append("========================================================================================\n");
        struk.append("                                   AB CoffeeShop\n");
        struk.append("========================================================================================\n");
        struk.append("Time:\n");
        struk.append("Purchase Id: ").append(generatePurchaseId()).append("\n");
        struk.append("========================================================================================\n");
        struk.append("                                 Thank you for your visit!\n");
        struk.append("                                 Follow us on Instagram @AB_Coffee\n");
        struk.append("----------------------------------------------------------------------------------------\n");
        struk.append("Item Name:\t\tPrice\tQty\n");
        struk.append("========================================================================================\n");

        // Mengambil data dari tabel dan menambahkannya ke struk
        for (int i = 0; i < tableProduk.getRowCount(); i++) {
            boolean isChecked = (Boolean) tableProduk.getValueAt(i, 0);
            if (isChecked) {
                String itemName = (String) tableProduk.getValueAt(i, 1); // Nama produk berada di kolom 2

                // Mengambil harga sebagai double
                String priceStr = (String) tableProduk.getValueAt(i, 2); // Harga berada di kolom 2
                priceStr = priceStr.replace("Rp. ", "").replace(",", ""); // Menghilangkan "Rp. " dan koma jika ada
                double price = Double.parseDouble(priceStr); //

                // Mengambil nilai dari kolom "Jumlah" sebagai integer
                int quantity = (Integer) tableProduk.getValueAt(i, 4);
                struk.append(itemName)
                        .append("\t\t").append("Rp. " + price)
                        .append("\t ").append(quantity)
                        .append("x")
                        .append("\n");
            }
        }

        // Set struk ke dalam JTextArea atau lakukan operasi cetak yang sesuai
        totalButton.setEnabled(true);
        jTextArea.setEditable(false); // Menonaktifkan pengeditan
        jTextArea.setText(struk.toString());
    }//GEN-LAST:event_cetakStrukActionPerformed

    private void LogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutActionPerformed
        Login_admin login = new Login_admin();
        login.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_LogoutActionPerformed

    private void resetFields() {
        // Mengosongkan nilai subtotal, pajak, total, dan totalField
        subTotalField.setText("");
        taxField.setText("");
        totalField.setText("");
        jTextArea.setText("");
        btn_bayar.setEnabled(false);
        totalButton.setEnabled(false);
        cetakStruk.setEnabled(true);
        resetButton.setEnabled(true);

        // Mengaktifkan kembali tabel produk
        tableProduk.setEnabled(true);

        // Mereset model tabel produk untuk mengosongkan semua item yang dipilih
        for (int row = 0; row < tableProduk.getRowCount(); row++) {
            tableProduk.setValueAt(false, row, 0);
        }

        // Mengatur ulang nilai spinner menjadi 0
        for (int row = 0; row < tableProduk.getRowCount(); row++) {
            tableProduk.setValueAt(0, row, 4); // Kolom "Jumlah" berada di indeks 5

            // Memastikan JSpinner diatur ke nilai defaultnya (0)
            JSpinner spinner = (JSpinner) tableProduk.getCellEditor(row, 4).getTableCellEditorComponent(tableProduk, null, true, row, 4);
            spinner.setValue(0);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Logout;
    private javax.swing.JButton btn_bayar;
    private javax.swing.JButton btn_cari;
    private javax.swing.JButton btn_logout;
    private javax.swing.JButton cetakStruk;
    private javax.swing.JButton jButtonnnnnnnnn;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea;
    private javax.swing.JPanel jTxtDate;
    private javax.swing.JButton resetButton;
    private javax.swing.JTextField subTotalField;
    private javax.swing.JTable tableProduk;
    private javax.swing.JTextField taxField;
    private javax.swing.JButton totalButton;
    private javax.swing.JTextField totalField;
    private javax.swing.JTextField txt_cari;
    // End of variables declaration//GEN-END:variables
}
