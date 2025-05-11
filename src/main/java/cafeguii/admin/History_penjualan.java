package cafeguii.admin;

import cafeguii.koneksi.koneksi;
import cafeguii.login.Login_admin;
import com.mycompany.akubakul.Dashboard;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

class History {

    private int KodeHistory, Kodan;
    private String KodeKaryawan, Tanggal, Pesanan;
    private double Total;

    public History(int Kodan, int KodeHistory, double Total, String Pesanan, String KodeKaryawan, String Tanggal) {
        this.KodeHistory = KodeHistory;
        this.Pesanan = Pesanan;
        this.KodeKaryawan = KodeKaryawan;
        this.Tanggal = Tanggal;
        this.Total = Total;
        this.Kodan = Kodan;
    }

    public int getKodeHistory() {
        return KodeHistory;
    }

    public int getKodan() {
        return Kodan;
    }

    public String getPesanan() {
        return Pesanan;
    }

    public double getTotal() {
        return Total;
    }

    public String Tanggal() {
        return Tanggal;
    }

    public String KodeKaryawan() {
        return KodeKaryawan;
    }

}

public class History_penjualan extends javax.swing.JFrame {

    public History_penjualan() {
        initComponents();
        makeWindowedFullscreen();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        loadKolom();
        jTable1.setModel(model);
        conn = conn = koneksi.bukaKoneksi();
        daftarBuku = new ArrayList<>();
        loadBuku();
        tampilBuku();
        addButtonColumnToTable();
    }

    private DefaultTableModel model = new DefaultTableModel();
    private Connection conn;
    private ArrayList<History> daftarBuku;

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

    private void loadKolom() {
        model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) { // Kolom untuk tombol
                    return JButton.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 1; // hanya kolom check dan Jumlah yang bisa di-edit
            }
        };
        model.addColumn("Kode History");
        model.addColumn("Kode Pesanan");
        model.addColumn("Total");
        model.addColumn("Karyawan");
        model.addColumn("Tanggal");
        model.addColumn("Pesannnnn");
    }

    private void loadBuku() {
        if (conn != null) {
            daftarBuku = new ArrayList<>();
            String kueri = "SELECT " +
                    "ANY_VALUE(h.KodeHistory) AS KodeHistory, " +
                    "p.Purchase_id, " +
                    "GROUP_CONCAT(CONCAT(pr.NamaProduk, '\\nRp.', pr.HargaProduk, ' x ', p.Kuantitas, ' ') SEPARATOR '\\n\\n') AS Pesanan, "
                    +
                    "ANY_VALUE(dp.Total) AS Total, " +
                    "ANY_VALUE(dp.Tanggal) AS Tanggal, " +
                    "ANY_VALUE(k.UsernameKasir) AS UsernameKasir " +
                    "FROM pesanan p " +
                    "JOIN detailpenjualan dp ON p.Purchase_id = dp.CodePesanan " + // Atau ganti p.CodePesanan jika
                                                                                   // diperlukan
                    "JOIN historypenjualan h ON dp.CodePenjualan = h.CodePenjualan " +
                    "JOIN karyawan k ON k.KodeKaryawan = h.KodeKaryawan " +
                    "JOIN produk pr ON pr.CodeProduk = p.CodeProduk " +
                    "GROUP BY p.Purchase_id " +
                    "ORDER BY Tanggal DESC";
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int Kodan = rs.getInt("KodeHistory");
                    int KodeHistory = rs.getInt("Purchase_id");
                    String Pesanan = rs.getString("Pesanan");
                    double Total = rs.getDouble("Total");
                    String Tanggal = rs.getString("Tanggal");
                    String KodeKaryawan = rs.getString("UsernameKasir");
                    History buku = new History(Kodan, KodeHistory, Total, Pesanan, KodeKaryawan, Tanggal);
                    daftarBuku.add(buku);
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(History_pembelian.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void tampilBuku() {
        model.setRowCount(0);
        for (History b : daftarBuku) {
            model.addRow(new Object[] { b.getKodan(), b.getKodeHistory(), b.getTotal(), b.KodeKaryawan(), b.Tanggal(),
                    b.getPesanan() });
        }
    }

    private void addButtonColumnToTable() {
        TableColumn column = jTable1.getColumnModel().getColumn(1);
        column.setCellRenderer(new ButtonRenderer());
        column.setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
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
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                JOptionPane.showMessageDialog(button,
                        "Pesanan " + ": \n" + model.getValueAt(jTable1.getSelectedRow(), 5),
                        "Kode Pesanan : " + model.getValueAt(jTable1.getSelectedRow(), 1),
                        JOptionPane.INFORMATION_MESSAGE);
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btn_produk = new javax.swing.JButton();
        cb_history = new javax.swing.JComboBox<>();
        btn_logout = new javax.swing.JButton();
        Karyawan = new javax.swing.JButton();
        btn_bahan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 153, 153));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setText("Cari");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(102, 102, 102));
        jLabel4.setFont(new java.awt.Font("Segoe Print", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("History Penjualan");

        jTable1.setFont(new java.awt.Font("Segoe Print", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 242,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(397, 397, 397)
                                .addComponent(jLabel4)
                                .addContainerGap(799, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 296,
                                        Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 568,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));

        btn_produk.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_produk.setText("Produk");
        btn_produk.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_produk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_produkActionPerformed(evt);
            }
        });

        cb_history.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cb_history.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "History", "History Beli Bahan", "History Penjualan" }));
        cb_history.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_historyActionPerformed(evt);
            }
        });

        btn_logout.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_logout.setText("Logout");
        btn_logout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logoutActionPerformed(evt);
            }
        });

        Karyawan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Karyawan.setText("Karyawan");
        Karyawan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Karyawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KaryawanActionPerformed(evt);
            }
        });

        btn_bahan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_bahan.setText("Bahan");
        btn_bahan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_bahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bahanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(Karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, 268,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 7, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                false)
                                                        .addComponent(btn_logout,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cb_history, 0, 269, Short.MAX_VALUE)
                                                        .addComponent(btn_produk,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btn_bahan,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(218, 218, 218)
                                .addComponent(Karyawan)
                                .addGap(28, 28, 28)
                                .addComponent(btn_bahan)
                                .addGap(27, 27, 27)
                                .addComponent(btn_produk)
                                .addGap(27, 27, 27)
                                .addComponent(cb_history, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_logout)
                                .addGap(113, 113, 113))
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        String keyword = jTextField1.getText().toLowerCase();

        // Membuat RowSorter untuk model tabel
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable1.getModel());
        jTable1.setRowSorter(sorter);

        // Menerapkan filter berdasarkan kata kunci hanya pada kolom "Nama Produk"
        if (keyword.length() == 0) {
            // Jika kotak pencarian kosong, hapus semua filter
            sorter.setRowFilter(null);
        } else {
            // Jika ada kata kunci, terapkan filter hanya pada kolom "Nama Produk"
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 3)); // Indeks 2 adalah kolom "Nama Produk"
        }
    }// GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jTextField1ActionPerformed

    private void btn_produkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_produkActionPerformed
        ProdukManagement produk = new ProdukManagement();
        produk.setVisible(true);

        // Menutup Jframe dashboard saat ini
        this.dispose();
    }// GEN-LAST:event_btn_produkActionPerformed

    private void cb_historyActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cb_historyActionPerformed
        String selectedItem = (String) cb_history.getSelectedItem();

        if ("History Beli Bahan".equals(selectedItem)) {
            History_pembelian pembelian = new History_pembelian();
            pembelian.setVisible(true);
        } else if ("History Penjualan".equals(selectedItem)) {
            History_penjualan penjualan = new History_penjualan();
            penjualan.setVisible(true);
        } else if ("History".equals(selectedItem)) {
            History_penjualan penjualan = new History_penjualan();
            penjualan.setVisible(true);
        }

        this.dispose();
    }// GEN-LAST:event_cb_historyActionPerformed

    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_logoutActionPerformed
        this.dispose();

        Login_admin login = new Login_admin();
        login.setVisible(true);
    }// GEN-LAST:event_btn_logoutActionPerformed

    private void KaryawanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_KaryawanActionPerformed
        ManagemenAkun akun = new ManagemenAkun();
        akun.setVisible(true);
        this.dispose();
    }// GEN-LAST:event_KaryawanActionPerformed

    private void btn_bahanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_bahanActionPerformed
        BahanStock bahan = new BahanStock();
        bahan.setVisible(true);
        this.dispose();
    }// GEN-LAST:event_btn_bahanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
        // (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(History_penjualan.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(History_penjualan.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(History_penjualan.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(History_penjualan.class.getName()).log(java.util.logging.Level.SEVERE,
                    null, ex);
        }
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new History_penjualan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Karyawan;
    private javax.swing.JButton btn_bahan;
    private javax.swing.JButton btn_logout;
    private javax.swing.JButton btn_produk;
    private javax.swing.JComboBox<String> cb_history;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
