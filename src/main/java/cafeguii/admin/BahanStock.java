package cafeguii.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import cafeguii.koneksi.DBHelper;
import cafeguii.koneksi.koneksi;
import cafeguii.login.Login_admin;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.sql.DriverManager;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Axioo Pongo
 */
public class BahanStock extends javax.swing.JFrame {

    /**
     * Creates new form item_stock
     */
    public BahanStock() {
        initComponents();
        makeWindowedFullscreen();

        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        TambahBahan.setEnabled(false);
        loadKolom();

        table_bahan.setModel(model);
        conn = koneksi.bukaKoneksi();
        daftarBahan = new ArrayList<>();
        loadBahan();
        tampilBahan();
        hideColumn(table_bahan, 0);

        // Pastikan untuk menambahkan event handler ini di bagian inisialisasi JFrame
        // Anda
        table_bahan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table_bahan.getSelectedRow() != -1) {
                // Ambil baris yang dipilih
                int barisTerpilih = table_bahan.getSelectedRow();

                // Isi field input dengan data dari baris yang dipilih
                txt_hidden.setText(model.getValueAt(barisTerpilih, 0).toString());
                txt_nama.setText(model.getValueAt(barisTerpilih, 1).toString()); // Nama Bahan
                String hargaText = model.getValueAt(barisTerpilih, 2).toString();
                hargaText = hargaText.replace("Rp. ", "").trim(); // Hilangkan "Rp" dan spasi
                txt_harga.setText(hargaText); // Harga Beli

            }
        });

        table_bahan.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                if (table_bahan.getSelectedRow() == -1) {
                    btnEdit.setEnabled(false);
                    btnAdd.setEnabled(true);
                    btnDelete.setEnabled(false);
                    TambahBahan.setEnabled(false);
                } else {
                    btnEdit.setEnabled(true);
                    btnAdd.setEnabled(false);
                    btnDelete.setEnabled(true);
                    TambahBahan.setEnabled(true);

                }
            }
        });

    }

    private DefaultTableModel model = new DefaultTableModel();
    private final transient Connection conn;
    private transient ArrayList<Bahan> daftarBahan;

    private static final String SEGOE_PRINT = "Segoe Print";
    private static final String SEGOE_UI = "Segoe UI";
    private static final String YU_GOTHIC_UI = "Yu Gothic UI";

    private void makeWindowedFullscreen() {
        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Set the size of the frame to the screen size
        setSize(screenSize);
        // Set the frame to maximized state
        setExtendedState(Frame.MAXIMIZED_BOTH);
        // Ensure the frame is visible
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txt_harga = new java.awt.TextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        txt_nama = new java.awt.TextField();
        jLabel10 = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_bahan = new javax.swing.JTable();
        btnReset1 = new javax.swing.JButton();
        txt_hidden = new java.awt.TextField();
        TambahBahan = new javax.swing.JButton();
        btn_produk = new javax.swing.JButton();
        cb_history1 = new javax.swing.JComboBox<>();
        btn_logout = new javax.swing.JButton();
        btn_bahan = new javax.swing.JButton();
        Karyawan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(102, 102, 102));

        jLabel4.setBackground(new java.awt.Color(102, 102, 102));
        jLabel4.setFont(new java.awt.Font(SEGOE_PRINT, 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Bahan Management ");

        txt_harga.setFont(new java.awt.Font(YU_GOTHIC_UI, 0, 14)); // NOI18N
        txt_harga.addActionListener(this::txt_hargaActionPerformed);

        jLabel6.setBackground(new java.awt.Color(102, 102, 102));
        jLabel6.setFont(new java.awt.Font(SEGOE_UI, 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setText("Nama Bahan");

        jLabel8.setBackground(new java.awt.Color(102, 102, 102));
        jLabel8.setFont(new java.awt.Font(SEGOE_PRINT, 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("List Bahan :");

        btnAdd.setFont(new java.awt.Font(SEGOE_UI, 1, 12)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(102, 102, 102));
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font(SEGOE_UI, 1, 12)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(102, 102, 102));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        txt_nama.setFont(new java.awt.Font(YU_GOTHIC_UI, 0, 14)); // NOI18N
        txt_nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namaActionPerformed(evt);
            }
        });

        jLabel10.setBackground(new java.awt.Color(102, 102, 102));
        jLabel10.setFont(new java.awt.Font(SEGOE_UI, 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setText("Harga Beli");

        btnDelete.setFont(new java.awt.Font(SEGOE_UI, 1, 12)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(102, 102, 102));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jButton1.setText("Cari");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        table_bahan.setFont(new java.awt.Font(SEGOE_PRINT, 0, 12)); // NOI18N
        table_bahan.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }));
        jScrollPane2.setViewportView(table_bahan);

        btnReset1.setFont(new java.awt.Font(SEGOE_UI, 1, 12)); // NOI18N
        btnReset1.setForeground(new java.awt.Color(102, 102, 102));
        btnReset1.setText("Reset");
        btnReset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReset1ActionPerformed(evt);
            }
        });

        txt_hidden.setFont(new java.awt.Font(YU_GOTHIC_UI, 0, 14)); // NOI18N
        txt_hidden.setVisible(false);
        txt_hidden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_hiddenActionPerformed(evt);
            }
        });

        TambahBahan.setText("Tambahkan");
        TambahBahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TambahBahanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap(390, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addGap(452, 452, 452))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 148,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(TambahBahan)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 212,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton1))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel6))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel10)
                                                        .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                143, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txt_hidden, javax.swing.GroupLayout.PREFERRED_SIZE, 142,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(btnAdd)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnEdit)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnDelete)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnReset1)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
                        .addComponent(jScrollPane2));
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61,
                                        Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel10))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel2Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addComponent(txt_harga, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                26, Short.MAX_VALUE)
                                                        .addComponent(txt_nama,
                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(25, 25, 25))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(txt_hidden, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                        Short.MAX_VALUE)
                                                .addGap(43, 43, 43)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnReset1, javax.swing.GroupLayout.PREFERRED_SIZE, 34,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButton1)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(TambahBahan))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 464,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));

        btn_produk.setFont(new java.awt.Font(SEGOE_UI, 1, 18)); // NOI18N
        btn_produk.setText("Produk");
        btn_produk.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_produk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_produkActionPerformed(evt);
            }
        });

        cb_history1.setFont(new java.awt.Font(SEGOE_UI, 1, 18)); // NOI18N
        cb_history1.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "History", "History Beli Bahan", "History Penjualan" }));
        cb_history1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_history1ActionPerformed(evt);
            }
        });

        btn_logout.setFont(new java.awt.Font(SEGOE_UI, 1, 18)); // NOI18N
        btn_logout.setText("Logout");
        btn_logout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logoutActionPerformed(evt);
            }
        });

        btn_bahan.setFont(new java.awt.Font(SEGOE_UI, 1, 18)); // NOI18N
        btn_bahan.setText("Bahan");
        btn_bahan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_bahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_bahanActionPerformed(evt);
            }
        });

        Karyawan.setFont(new java.awt.Font(SEGOE_UI, 1, 18)); // NOI18N
        Karyawan.setText("Karyawan");
        Karyawan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        Karyawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KaryawanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btn_produk, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cb_history1, 0, 216, Short.MAX_VALUE)
                                        .addComponent(btn_logout, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_bahan, javax.swing.GroupLayout.Alignment.TRAILING,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Karyawan, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(156, 156, 156)
                                .addComponent(Karyawan)
                                .addGap(32, 32, 32)
                                .addComponent(btn_bahan)
                                .addGap(29, 29, 29)
                                .addComponent(btn_produk)
                                .addGap(27, 27, 27)
                                .addComponent(cb_history1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_logout)
                                .addGap(25, 25, 25))
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING,
                                javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void hideColumn(JTable table, int columnIndex) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
    }

    private void loadKolom() {
        model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == -1;
            }
        };
        model.addColumn("Kode Bahan");
        model.addColumn("Nama Bahan");
        model.addColumn("Harga Beli");
        model.addColumn("Stok");
    }

    private void loadBahan() {
        if (conn != null) {
            daftarBahan = new ArrayList<>();
            String kueri = "SELECT KodeBahan, NamaBahan, HargaBeli, Stok FROM stokbahan";
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int kodeBahan = rs.getInt("KodeBahan");
                    String namaBahan = rs.getString("NamaBahan");
                    double hargaBeli = rs.getDouble("HargaBeli");
                    int stok = rs.getInt("Stok");
                    Bahan bahan = new Bahan(kodeBahan, namaBahan, hargaBeli, stok);
                    daftarBahan.add(bahan);
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(BahanStock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void tampilBahan() {
        model.setRowCount(0);
        for (Bahan b : daftarBahan) {
            model.addRow(new Object[] { b.getKodeBahan(), b.getNamaBahan(), "Rp. " + b.getHargaBeli(), b.getStok() });
        }
    }

    public void reset() {
        txt_nama.setText("");
        txt_harga.setText("");
        btnAdd.setText("Add");

        table_bahan.clearSelection(); // Mengosongkan seleksi tabel
    }

    void cariBahanbyKeyword(String keyword) {
        if (conn != null) {
            daftarBahan = new ArrayList<>();
            String kueri = "SELECT KodeBahan, NamaBahan, HargaBeli, Stok FROM stokbahan WHERE NamaBahan LIKE ?";
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                ps.setString(1, "%" + keyword + "");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int kodeBahan = rs.getInt("KodeBahan");
                    String namaBahan = rs.getString("NamaBahan");
                    double hargaBeli = rs.getDouble("HargaBeli");
                    int stok = rs.getInt("Stok");
                    Bahan bahan = new Bahan(kodeBahan, namaBahan, hargaBeli, stok);
                    daftarBahan.add(bahan);
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(BahanStock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnAddActionPerformed
        String namaBahan = txt_nama.getText();
        String hargaText = txt_harga.getText(); // Ambil teks dari txt_harga

        // Periksa apakah NamaBahan, hargaText, atau Stok kosong sebelum mencoba
        // melakukan proses tambah data
        if (namaBahan.isEmpty() || hargaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi field yang kosong atau masukkan nilai yang valid");
        } else {
            double hargaBeli = Double.parseDouble(hargaText); // Konversi hargaText menjadi double

            try (Connection koneksi = DBHelper.getConnection()) {
                int kodeBahan = generateUniqueCode();

                String kueri = "INSERT INTO stokbahan (KodeBahan, NamaBahan, HargaBeli, Stok) VALUES (?,?,?,?)";
                PreparedStatement ps = koneksi.prepareStatement(kueri);
                ps.setInt(1, kodeBahan); // Menggunakan nilai unik untuk 'KodeBahan'
                ps.setString(2, namaBahan);
                ps.setDouble(3, hargaBeli);
                ps.setInt(4, 0);
                int hasil = ps.executeUpdate();

                if (hasil > 0) {
                    JOptionPane.showMessageDialog(this, "Input Berhasil");
                } else {
                    JOptionPane.showMessageDialog(this, "Input Gagal");
                }

                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(BahanStock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadBahan();
        tampilBahan();
        reset();
    }// GEN-LAST:event_btnAddActionPerformed

    // Metode untuk menghasilkan nilai unik untuk KodeBahan
    private int generateUniqueCode() {
        // Anda dapat mengimplementasikan logika Anda untuk menghasilkan nilai unik di
        // sini
        // Misalnya, Anda dapat melakukan query ke database untuk mendapatkan kode
        // terakhir
        // kemudian menambahkan 1 ke kode tersebut atau menggunakan UUID
        // Di sini, saya akan mengembalikan nilai acak sebagai contoh sederhana
        Random random = new Random();
        return random.nextInt(1000); // Mengembalikan nilai acak antara 0 dan 999
    }

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnEditActionPerformed
        // Ambil baris yang dipilih di tabel
        int barisTerpilih = table_bahan.getSelectedRow();

        // Periksa apakah ada baris yang dipilih
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit terlebih dahulu.");
            return;
        }

        // Ambil data dari komponen input
        String NamaBahan = txt_nama.getText().trim();
        String hargaText = txt_harga.getText().trim(); // Ambil teks dari txt_harga
        if (NamaBahan.isEmpty() || hargaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi field yang kosong atau masukkan nilai yang valid.");
            return;
        }

        // Konversi hargaText menjadi double
        double HargaBeli;
        try {
            HargaBeli = Double.parseDouble(hargaText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Masukkan nilai harga yang valid.");
            return;
        }

        // Ambil KodeBahan dari tabel berdasarkan baris yang dipilih
        int KodeBahan = (int) table_bahan.getValueAt(barisTerpilih, 0);

        // Update data di database
        try (Connection koneksi = DBHelper.getConnection()) {
            String kueri = "UPDATE stokbahan SET NamaBahan=?, HargaBeli=?, Stok=? WHERE KodeBahan=?";
            PreparedStatement ps = koneksi.prepareStatement(kueri);
            ps.setString(1, NamaBahan);
            ps.setDouble(2, HargaBeli);
            ps.setInt(3, 0);
            ps.setInt(4, KodeBahan);
            int hasil = ps.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(this, "Update Berhasil");
            } else {
                JOptionPane.showMessageDialog(this, "Update Gagal");
            }

            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(BahanStock.class.getName()).log(Level.SEVERE, null, ex);
        }

        reset();
        loadBahan();
        tampilBahan();
    }// GEN-LAST:event_btnEditActionPerformed

    private void txt_namaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txt_namaActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txt_namaActionPerformed

    private void btn_produkActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_produkActionPerformed
        ProdukManagement produk = new ProdukManagement();
        produk.setVisible(true);

        // Menutup Jframe dashboard saat ini
        this.dispose();
    }// GEN-LAST:event_btn_produkActionPerformed

    private void cb_history1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cb_history1ActionPerformed
        String selectedItem = (String) cb_history1.getSelectedItem();

        if ("History Beli Bahan".equals(selectedItem)) {
            History_pembelian pembelian = new History_pembelian();
            pembelian.setVisible(true);
            this.dispose();
        } else if ("History Penjualan".equals(selectedItem)) {
            History_penjualan penjualan = new History_penjualan();
            penjualan.setVisible(true);
            this.dispose();
        }

        this.dispose();
    }// GEN-LAST:event_cb_history1ActionPerformed

    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_logoutActionPerformed
        this.dispose();

        Login_admin login = new Login_admin();
        login.setVisible(true);
    }// GEN-LAST:event_btn_logoutActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDeleteActionPerformed
        // Ambil baris yang dipilih di tabel
        int barisTerpilih = table_bahan.getSelectedRow();

        // Periksa apakah ada baris yang dipilih
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus terlebih dahulu.");
            return;
        }

        // Ambil KodeBahan dari tabel berdasarkan baris yang dipilih
        int KodeBahan = (int) table_bahan.getValueAt(barisTerpilih, 0);

        // Tampilkan dialog konfirmasi
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin menghapus data ini?",
                "Konfirmasi Hapus Data", JOptionPane.YES_NO_OPTION);

        // Jika user memilih "Ya" (YES_OPTION)
        if (konfirmasi == JOptionPane.YES_OPTION) {
            // Hapus data di database
            try (Connection koneksi = DBHelper.getConnection()) {
                String kueri = "DELETE FROM stokbahan WHERE KodeBahan=?";
                PreparedStatement ps = koneksi.prepareStatement(kueri);
                ps.setInt(1, KodeBahan);
                int hasil = ps.executeUpdate();

                if (hasil > 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus");
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus data");
                }

                ps.close();
            } catch (SQLIntegrityConstraintViolationException e) {
                // Tangkap pengecualian jika ada kesalahan constraint integritas referensial
                JOptionPane.showMessageDialog(this,
                        "Tidak dapat menghapus bahan. Hapus produk yang menggunakan bahan ini terlebih dahulu !!!!",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                Logger.getLogger(BahanStock.class.getName()).log(Level.SEVERE, null, ex);
            }

            reset();
            loadBahan();
            tampilBahan();
        }
    }// GEN-LAST:event_btnDeleteActionPerformed

    private void txt_hargaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txt_hargaActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txt_hargaActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
        String keyword = jTextField1.getText().toLowerCase();

        // Membuat RowSorter untuk model tabel
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table_bahan.getModel());
        table_bahan.setRowSorter(sorter);

        // Menerapkan filter berdasarkan kata kunci hanya pada kolom "Nama Produk"
        if (keyword.length() == 0) {
            // Jika kotak pencarian kosong, hapus semua filter
            sorter.setRowFilter(null);
        } else {
            // Jika ada kata kunci, terapkan filter hanya pada kolom "Nama Produk"
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 1)); // Indeks 2 adalah kolom "Nama Produk"
        } // TODO add your handling code here:
    }// GEN-LAST:event_jButton1ActionPerformed

    private void btnReset1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnReset1ActionPerformed
        reset();
    }// GEN-LAST:event_btnReset1ActionPerformed

    private void txt_hiddenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txt_hiddenActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txt_hiddenActionPerformed

    private void btn_bahanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_bahanActionPerformed
        BahanStock bahan = new BahanStock();
        bahan.setVisible(true);
        this.dispose();
    }// GEN-LAST:event_btn_bahanActionPerformed

    private void KaryawanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_KaryawanActionPerformed
        ManagemenAkun akun = new ManagemenAkun();
        akun.setVisible(true);
        this.dispose();
    }// GEN-LAST:event_KaryawanActionPerformed

    private void TambahBahanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_TambahBahanActionPerformed
        int newStock = 0;
        int barisTerpilih = table_bahan.getSelectedRow();
        int KodeBahan = 0;
        int stokditambahkan = 0;
        double totalharga = 0;
        int Karyawan = -99; // Default value, assuming -99 is not a valid ID

        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih Baris Pada Tabel terlebih dahulu.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show input dialog to get the new stock value
        String input = JOptionPane.showInputDialog(this, "Masukkan Tambahan Stok :");

        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Input tidak boleh kosong. Masukkan angka yang valid.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            stokditambahkan = Integer.parseInt(input);
            KodeBahan = (int) table_bahan.getValueAt(barisTerpilih, 0);
            int currentStock = (int) table_bahan.getValueAt(barisTerpilih, 3);
            newStock = currentStock + stokditambahkan;

            String hargaString = (String) table_bahan.getValueAt(barisTerpilih, 2);
            hargaString = hargaString.replace("Rp. ", "");
            totalharga = Double.parseDouble(hargaString) * stokditambahkan;

            JOptionPane.showMessageDialog(this, "Stock updated successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid integer.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Retrieve Karyawan ID
        String KaryawanQuery = "SELECT ID FROM karyawangetter"; // Assuming "ID" is the column name
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root",
                "Opoae_123")) {
            try (PreparedStatement ps = conn.prepareStatement(KaryawanQuery)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Karyawan = rs.getInt("ID");
                } else {
                    JOptionPane.showMessageDialog(this, "No data found in karyawangetter table.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to execute query: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update Stok Bahan
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root",
                "Opoae_123")) {
            String sql = "UPDATE stokbahan SET Stok = ? WHERE KodeBahan = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, newStock);
                statement.setInt(2, KodeBahan);
                statement.executeUpdate();
            }

            // Insert into detailpembelian
            String insertSql = "INSERT INTO detailpembelian (KodeBahan, Kuantitas, Total, KodeKaryawan) VALUES (?,?,?,?)";
            try (PreparedStatement insertStatement = conn.prepareStatement(insertSql)) {
                insertStatement.setInt(1, KodeBahan);
                insertStatement.setInt(2, stokditambahkan);
                insertStatement.setDouble(3, totalharga);
                insertStatement.setInt(4, Karyawan);
                insertStatement.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan ke database.", "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menginsert data ke database: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Load and display data in table
        loadBahan();
        tampilBahan();
        reset();
    }// GEN-LAST:event_TambahBahanActionPerformed

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
            java.util.logging.Logger.getLogger(BahanStock.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BahanStock.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BahanStock.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BahanStock.class.getName()).log(java.util.logging.Level.SEVERE, null,
                    ex);
        }
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>
        // </editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BahanStock().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Karyawan;
    private javax.swing.JButton TambahBahan;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btn_bahan;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btn_logout;
    private javax.swing.JButton btn_produk;
    private javax.swing.JButton btnReset1;
    private javax.swing.JComboBox<String> cb_history1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable table_bahan;
    private java.awt.TextField txt_harga;
    private java.awt.TextField txt_hidden;
    private java.awt.TextField txt_nama;
    // End of variables declaration//GEN-END:variables
}
