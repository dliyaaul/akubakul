package cafeguii.admin;

import cafeguii.koneksi.koneksi;
import cafeguii.login.Login_admin;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
class Buku {

    private int KodePembelian, Kuantitas;
    private String Karyawan, NamaBahan, Tanggal;
    private double Harga, TotalHarga;

    public Buku(int KodePembelian, String NamaBahan, double Harga, int Kuantitas, double TotalHarga, String Tanggal, String Karyawan) {
        this.KodePembelian = KodePembelian;
        this.Karyawan = Karyawan;
        this.NamaBahan = NamaBahan;
        this.Kuantitas = Kuantitas;
        this.Tanggal = Tanggal;
        this.Harga = Harga;
        this.TotalHarga = TotalHarga;
    }

    public int getKodePembelian() {
        return KodePembelian;
    }

    public String getNamaBahan() {
        return NamaBahan;
    }

    public int Kuantitas() {
        return Kuantitas;
    }

    public String getKaryawan() {
        return Karyawan;
    }

    public double getHarga() {
        return Harga;
    }

    public String Tanggal() {
        return Tanggal;

    }

    public double getTotalHarga() {
        return TotalHarga;
    }
}

public class History_pembelian extends javax.swing.JFrame {

    public History_pembelian() {
        initComponents();
        makeWindowedFullscreen();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        loadKolom();
        jTable1.setModel(model);
        conn = conn = koneksi.bukaKoneksi();
        daftarBuku = new ArrayList<>();
        loadBuku();
        tampilBuku();
    }
    private DefaultTableModel model = new DefaultTableModel();
    private Connection conn;
    private ArrayList<Buku> daftarBuku;

    private void loadKolom() {
        model.addColumn("KodePembelian");
        model.addColumn("Nama Bahan");
        model.addColumn("Harga Beli");
        model.addColumn("Kuantitas");
        model.addColumn("Total");
        model.addColumn("Tanggal");
        model.addColumn("Nama Karyawan");
    }

    private void loadBuku() {
        if (conn != null) {
            daftarBuku = new ArrayList<>();
            String kueri = "SELECT b.KodePembelian, a.NamaBahan, a.HargaBeli, b.Kuantitas, b.Total, b.Tanggal, c.UsernameKasir FROM detailpembelian b JOIN stokbahan a ON a.KodeBahan = b.KodeBahan JOIN karyawan c ON c.KodeKaryawan = b.KodeKaryawan Order BY b.Tanggal DESC";
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int KodePembelian = rs.getInt("KodePembelian");
                    String NamaBahan = rs.getString("NamaBahan");
                    double Harga = rs.getDouble("HargaBeli");
                    int Kuantitas = rs.getInt("Kuantitas");
                    double TotalHarga = rs.getDouble("Total");
                    String Tanggal = rs.getString("Tanggal");
                    String Karyawan = rs.getString("UsernameKasir");

                    Buku buku = new Buku(KodePembelian, NamaBahan, Harga, Kuantitas, TotalHarga, Tanggal, Karyawan);
                    daftarBuku.add(buku);
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(History_pembelian.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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

    private void tampilBuku() {
        model.setRowCount(0);
        for (Buku b : daftarBuku) {
            model.addRow(new Object[]{b.getKodePembelian(), b.getNamaBahan(), b.Kuantitas() + " x", b.getHarga(), "Rp. " + b.getTotalHarga(), b.Tanggal(), b.getKaryawan()});
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Keyword_Cari = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        btn_produk = new javax.swing.JButton();
        cb_history = new javax.swing.JComboBox<>();
        btn_logout = new javax.swing.JButton();
        btn_bahan = new javax.swing.JButton();
        btn_kr = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jLabel4.setBackground(new java.awt.Color(102, 102, 102));
        jLabel4.setFont(new java.awt.Font("Segoe Print", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("History Pembelian");

        jTable1.setFont(new java.awt.Font("Segoe Print", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Category", "Price"
            }
        ));
        jTable1.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(jTable1);

        Keyword_Cari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Keyword_CariActionPerformed(evt);
            }
        });

        jButton2.setText("Cari");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Keyword_Cari, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(455, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(391, 391, 391))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Keyword_Cari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE))
        );

        btn_produk.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_produk.setText("Produk");
        btn_produk.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_produk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_produkActionPerformed(evt);
            }
        });

        cb_history.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cb_history.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "History", "History Beli Bahan", "History Penjualan" }));
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

        btn_bahan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_bahan.setText("Bahan");
        btn_bahan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_bahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bahanActionPerformed(evt);
            }
        });

        btn_kr.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_kr.setText("Karyawan");
        btn_kr.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_kr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_krActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btn_kr, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                        .addComponent(btn_produk, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cb_history, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_bahan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btn_logout, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(154, 154, 154)
                .addComponent(btn_kr)
                .addGap(33, 33, 33)
                .addComponent(btn_bahan)
                .addGap(34, 34, 34)
                .addComponent(btn_produk)
                .addGap(37, 37, 37)
                .addComponent(cb_history, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_logout)
                .addGap(53, 53, 53))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_produkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_produkActionPerformed
        ProdukManagement produk = new ProdukManagement();
        produk.setVisible(true);

        // Menutup Jframe dashboard saat ini
        this.dispose();
    }//GEN-LAST:event_btn_produkActionPerformed

    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_logoutActionPerformed
        this.dispose();

        Login_admin login = new Login_admin();
        login.setVisible(true);
    }//GEN-LAST:event_btn_logoutActionPerformed

    private void Keyword_CariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Keyword_CariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Keyword_CariActionPerformed

    private void cb_historyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_historyActionPerformed
        String selectedItem = (String) cb_history.getSelectedItem();

        if ("History Beli Bahan".equals(selectedItem)) {
            History_pembelian pembelian = new History_pembelian();
            pembelian.setVisible(true);
        } else if ("History Penjualan".equals(selectedItem)) {
            History_penjualan penjualan = new History_penjualan();
            penjualan.setVisible(true);
        }

        this.dispose();
    }//GEN-LAST:event_cb_historyActionPerformed

    private void btn_bahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_bahanActionPerformed
        Bahan_stock bahan = new Bahan_stock();
        bahan.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_bahanActionPerformed

    private void btn_krActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_krActionPerformed
        ManagemenAkun akun = new ManagemenAkun();
        akun.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btn_krActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        String keyword = Keyword_Cari.getText().toLowerCase();

        // Membuat RowSorter untuk model tabel
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable1.getModel());
        jTable1.setRowSorter(sorter);

        // Menerapkan filter berdasarkan kata kunci hanya pada kolom "Nama Produk"
        if (keyword.length() == 0) {
            // Jika kotak pencarian kosong, hapus semua filter
            sorter.setRowFilter(null);
        } else {
            // Jika ada kata kunci, terapkan filter hanya pada kolom "Nama Produk"
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 1)); // Indeks 2 adalah kolom "Nama Produk"
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(History_pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(History_pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(History_pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(History_pembelian.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new History_pembelian().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Keyword_Cari;
    private javax.swing.JButton btn_bahan;
    private javax.swing.JButton btn_kr;
    private javax.swing.JButton btn_logout;
    private javax.swing.JButton btn_produk;
    private javax.swing.JComboBox<String> cb_history;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
