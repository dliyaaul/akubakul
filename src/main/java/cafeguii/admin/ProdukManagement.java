package cafeguii.admin;

import cafeguii.koneksi.koneksi;
import cafeguii.login.Login_admin;
import cafeguii.produk.Produk;
import com.mycompany.akubakul.Dashboard;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Axioo Pongo
 */
class Produk2 {

    public static String getKuantitasBahan() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private String NamaProduk;
    private double HargaProduk;
    private String BahanKuantitas;
    private ImageIcon Gambar;
    private int CodeProduk;

    public Produk2(String NamaProduk, double HargaProduk, String imagePath, String BahanKuantitas, int CodeProduk) {
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

class Detail {

    private String NamaBahan;
    private int Kuantitas;
    private int KodeDetail;

    public Detail(int KodeDetail, String NamaBahan, int Kuantitas) {
        this.NamaBahan = NamaBahan;
        this.Kuantitas = Kuantitas;
        this.KodeDetail = KodeDetail;
    }

    public String getNamaBahan() {
        return NamaBahan;
    }

    public int getKuantitas() {
        return Kuantitas;
    }

    public int getKodeDetail() {
        return KodeDetail;
    }

}

public final class ProdukManagement extends javax.swing.JFrame {

    public ProdukManagement() {
        initComponents();
        makeWindowedFullscreen();
        btn_edit.setEnabled(false);
        btn_delete.setEnabled(false);
        TambahBahan.setEnabled(false);
        EditBahan.setEnabled(false);
        HapusBahan.setEnabled(false);
        loadKolom();

        Produktable.setModel(model);
        DetailBahan.setModel(bhn);
        conn = koneksi.bukaKoneksi();
        daftarProduk = new ArrayList<>();
        DetailBhn = new ArrayList<>();
        loadBahan();
        loadIsiTbel();
        addSpinnerEditorToTable(DetailBahan, 1);

        hideColumn(Produktable, 3);
        hideColumn(Produktable, 4);
        hideColumn(DetailBahan, 2);
        showcolumn1(Produktable, 2, 5);

        addButtonColumnToTable();
        Produktable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && Produktable.getSelectedRow() != -1) {
                // Ambil baris yang dipilih
                int barisTerpilih = Produktable.getSelectedRow();

                // Isi field input dengan data dari baris yang dipilih
                txt_nama.setText(model.getValueAt(barisTerpilih, 0).toString()); // Nama Bahan
                String hargaText = model.getValueAt(barisTerpilih, 1).toString();
                hargaText = hargaText.replace("Rp. ", "").trim(); // Hilangkan "Rp" dan spasi
                txt_harga.setText(hargaText); // Harga Beli
                loadDetailBahan(barisTerpilih);
            }
        });

        Produktable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                if (Produktable.getSelectedRow() == -1) {
                    btn_edit.setEnabled(false);
                    btn_add.setEnabled(true);
                    btn_delete.setEnabled(false);
                    TambahBahan.setEnabled(false);

                } else {
                    btn_edit.setEnabled(true);
                    btn_add.setEnabled(false);
                    btn_delete.setEnabled(true);
                    TambahBahan.setEnabled(true);

                }
            }
        });
        DetailBahan.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                if (DetailBahan.getSelectedRow() == -1) {
                    EditBahan.setEnabled(false);
                    HapusBahan.setEnabled(false);
                } else {
                    EditBahan.setEnabled(true);
                    HapusBahan.setEnabled(true);
                }
            }
        });

        Produktable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int barisTerpilih = Produktable.getSelectedRow();
                loadDetailBahan(barisTerpilih); // Load detail based on selected row
            }
        });

    }

    private void hideColumn(JTable table, int columnIndex) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
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

    private void showcolumn1(JTable table, int columnIndex, int preferredWidth) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(5); // atau ukuran min width yang diinginkan
        column.setMaxWidth(Integer.MAX_VALUE);
        column.setPreferredWidth(preferredWidth);
    }

    private DefaultTableModel model = new DefaultTableModel();
    private DefaultTableModel bhn = new DefaultTableModel();
    private Connection conn;
    private ArrayList<Produk2> daftarProduk;
    private ArrayList<Detail> DetailBhn;

    private void loadKolom() {
        model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) { // Kolom untuk tombol
                    return JButton.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 2;
            }
        };
        bhn = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return Integer.class; // Kolom "Jumlah" akan menggunakan JSpinner
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 1;
            }
        };
        model.addColumn("Nama");
        model.addColumn("Harga");
        model.addColumn("Bahan");
        model.addColumn("...");
        model.addColumn("id");

        bhn.addColumn("Nama Bahan");
        bhn.addColumn("Kuantitas");
        bhn.addColumn("Kode Detail Bahan");
    }

    private void loadDetailBahan(int barisTerpilih) {
        // Kosongkan tabel detail bahan jika tidak ada baris yang dipilih
        if (barisTerpilih == -1) {
            bhn.setRowCount(0);
            return;
        }

        // Dapatkan data dari baris yang dipilih di Produktable
        Object idProduk = model.getValueAt(barisTerpilih, 4); // Asumsi kolom kelima adalah ID produk
        bhn.setRowCount(0); // Kosongkan tabel detail bahan sebelum mengisinya
        System.out.println(idProduk);

        if (conn != null) {
            DetailBhn = new ArrayList<>();
            String query = "SELECT b.KodeDetail, a.NamaBahan, b.Kuantitas "
                    + "FROM `Bahan&Produk` b "
                    + "JOIN stokbahan a ON a.KodeBahan = b.KodeDetailBahan "
                    + "JOIN produk c ON c.CodeProduk = b.CodeProduk "
                    + "WHERE c.CodeProduk = ?";
            try {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setObject(1, idProduk);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int KodeDetail = rs.getInt("KodeDetail");
                    String NamaBahan = rs.getString("NamaBahan");
                    int Kuantitas = rs.getInt("Kuantitas");
                    Detail detail = new Detail(KodeDetail, NamaBahan, Kuantitas);
                    DetailBhn.add(detail);
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(Bahan_stock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Isi tabel detail bahan dengan data yang telah diambil
        for (Detail detail : DetailBhn) {
            bhn.addRow(new Object[]{detail.getNamaBahan(), detail.getKuantitas(), detail.getKodeDetail()});
        }
    }

        public void loadIsiTbel() {
        model.setRowCount(0);
        for (Produk2 b : daftarProduk) {
            model.addRow(new Object[]{b.getNamaProduk(), "Rp. " + b.getHargaProduk(), "Selengkapnya", b.getBahanKuantitas(), b.getCodeProduk()});
        }
    }

    private void loadBahan() {
        if (conn != null) {
            daftarProduk = new ArrayList<>();
            String kueri = "SELECT p.NamaProduk, p.HargaProduk, p.Gambar, "
                    + "GROUP_CONCAT(CONCAT(b.NamaBahan, ' (', COALESCE(bp.Kuantitas, 0), 'x)') SEPARATOR '\\n') AS BahanKuantitas, p.CodeProduk "
                    + "FROM produk p "
                    + "LEFT JOIN `bahan&produk` bp ON p.CodeProduk = bp.CodeProduk "
                    + "LEFT JOIN stokbahan b ON bp.KodeDetailBahan = b.KodeBahan "
                    + "GROUP BY p.CodeProduk";
            try {
                PreparedStatement ps = conn.prepareStatement(kueri);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String NamaProduk = rs.getString("NamaProduk");
                    double HargaProduk = rs.getDouble("HargaProduk");
                    String imagePath = rs.getString("Gambar");
                    String BahanKuantitas = rs.getString("BahanKuantitas");
                    int CodeProduk = rs.getInt("CodeProduk");
                    Produk2 produk = new Produk2(NamaProduk, HargaProduk, imagePath, BahanKuantitas, CodeProduk);
                    daftarProduk.add(produk);
                }
                rs.close();
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(Bahan_stock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void addButtonColumnToTable() {
        TableColumn column = Produktable.getColumnModel().getColumn(2);
        column.setCellRenderer(new ButtonRenderer());
        column.setCellEditor(new ButtonEditor(new JCheckBox()));
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
                JOptionPane.showMessageDialog(button, "Bahan " + model.getValueAt(Produktable.getSelectedRow(), 0) + ": \n" + model.getValueAt(Produktable.getSelectedRow(), 3), "Detail Bahan", JOptionPane.INFORMATION_MESSAGE);
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

    public void reset() {
        txt_nama.setText("");
        txt_harga.setText("");
        btn_add.setText("Add");

        Produktable.clearSelection(); // Mengosongkan seleksi tabel
    }

    public void reset2() {

        int barispilihan = Produktable.getSelectedRow();
        loadDetailBahan(barispilihan);
        DetailBahan.clearSelection();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cb_history = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txt_nama = new java.awt.TextField();
        txt_harga = new java.awt.TextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DetailBahan = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        btn_add = new javax.swing.JButton();
        btn_edit = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_reset1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        Produktable = new javax.swing.JTable();
        TambahBahan = new javax.swing.JButton();
        EditBahan = new javax.swing.JButton();
        HapusBahan = new javax.swing.JToggleButton();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btn_produk = new javax.swing.JButton();
        cb_history1 = new javax.swing.JComboBox<>();
        btn_logout = new javax.swing.JButton();
        BTNBAHAN = new javax.swing.JButton();
        Karyawan = new javax.swing.JButton();

        cb_history.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cb_history.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "History", "History Beli Bahan", "History Penjualan" }));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(1448, 784));

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));

        jPanel2.setPreferredSize(new java.awt.Dimension(1173, 700));

        jLabel4.setBackground(new java.awt.Color(102, 102, 102));
        jLabel4.setFont(new java.awt.Font("Segoe Print", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setText("Managemen Produk");

        jLabel5.setBackground(new java.awt.Color(102, 102, 102));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setText("Harga Produk");

        txt_nama.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N

        txt_harga.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N

        jLabel17.setBackground(new java.awt.Color(102, 102, 102));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setText("Nama Produk");

        jLabel8.setBackground(new java.awt.Color(102, 102, 102));
        jLabel8.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setText("List Produk :");

        jLabel9.setBackground(new java.awt.Color(102, 102, 102));
        jLabel9.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setText("Detail Bahan :");

        DetailBahan.setFont(new java.awt.Font("Segoe Print", 0, 12)); // NOI18N
        DetailBahan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(DetailBahan);

        jLabel11.setBackground(new java.awt.Color(102, 102, 102));
        jLabel11.setFont(new java.awt.Font("Segoe Print", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Detail Bahan :");

        btn_add.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_add.setForeground(new java.awt.Color(102, 102, 102));
        btn_add.setText("Add");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });

        btn_edit.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_edit.setForeground(new java.awt.Color(102, 102, 102));
        btn_edit.setText("Edit");
        btn_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editActionPerformed(evt);
            }
        });

        btn_delete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_delete.setForeground(new java.awt.Color(102, 102, 102));
        btn_delete.setText("Delete");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        btn_reset1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_reset1.setForeground(new java.awt.Color(102, 102, 102));
        btn_reset1.setText("Reset");
        btn_reset1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reset1ActionPerformed(evt);
            }
        });

        Produktable.setFont(new java.awt.Font("Segoe Print", 0, 12)); // NOI18N
        Produktable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(Produktable);

        TambahBahan.setText("Tambahkan");
        TambahBahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TambahBahanActionPerformed(evt);
            }
        });

        EditBahan.setText("Edit");
        EditBahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditBahanActionPerformed(evt);
            }
        });

        HapusBahan.setText("Hapus");
        HapusBahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HapusBahanActionPerformed(evt);
            }
        });

        jButton1.setText("Cari");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(362, 362, 362)
                                    .addComponent(jLabel4))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(btn_add, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_delete)
                                        .addGap(114, 114, 114)
                                        .addComponent(btn_reset1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel17))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(388, 388, 388)
                        .addComponent(jLabel9))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(TambahBahan)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(EditBahan)
                            .addGap(96, 96, 96)
                            .addComponent(HapusBahan, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(282, 282, 282))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_nama, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_harga, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_add)
                    .addComponent(btn_edit)
                    .addComponent(btn_delete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_reset1))
                .addGap(58, 58, 58)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TambahBahan)
                            .addComponent(EditBahan)
                            .addComponent(HapusBahan)))))
        );

        btn_produk.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btn_produk.setText("Produk");
        btn_produk.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_produk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_produkActionPerformed(evt);
            }
        });

        cb_history1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cb_history1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "History", "History Beli Bahan", "History Penjualan" }));
        cb_history1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cb_history1ActionPerformed(evt);
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

        BTNBAHAN.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        BTNBAHAN.setText("Bahan");
        BTNBAHAN.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        BTNBAHAN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTNBAHANActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cb_history1, 0, 200, Short.MAX_VALUE)
                    .addComponent(btn_produk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BTNBAHAN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Karyawan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1212, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(Karyawan)
                .addGap(32, 32, 32)
                .addComponent(BTNBAHAN)
                .addGap(28, 28, 28)
                .addComponent(btn_produk)
                .addGap(27, 27, 27)
                .addComponent(cb_history1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_logout)
                .addGap(97, 97, 97))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void cb_history1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cb_history1ActionPerformed
        String selectedItem = (String) cb_history1.getSelectedItem();

        if ("History Beli Bahan".equals(selectedItem)) {
            History_pembelian pembelian = new History_pembelian();
            pembelian.setVisible(true);
        } else if ("History Penjualan".equals(selectedItem)) {
            History_penjualan penjualan = new History_penjualan();
            penjualan.setVisible(true);
        }

        this.dispose();
    }//GEN-LAST:event_cb_history1ActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed
        String NamaBahan = txt_nama.getText();
        String hargaText = txt_harga.getText(); // Ambil teks dari txt_harga

        // Periksa apakah NamaBahan, hargaText, atau Stok kosong sebelum mencoba melakukan proses tambah data
        if (NamaBahan.isEmpty() || hargaText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi field yang kosong atau masukkan nilai yang valid");
        } else {
            double HargaJual = Double.parseDouble(hargaText); // Konversi hargaText menjadi double

            try (Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root", "Opoae_123")) {

                String kueri = "INSERT INTO produk (NamaProduk, HargaProduk, Gambar) VALUES (?,?,?)";
                PreparedStatement ps = koneksi.prepareStatement(kueri);
                ps.setString(1, NamaBahan);
                ps.setDouble(2, HargaJual);
                ps.setString(3, "");
                int hasil = ps.executeUpdate();

                if (hasil > 0) {
                    JOptionPane.showMessageDialog(this, "Input Berhasil");

                } else {
                    JOptionPane.showMessageDialog(this, "Input Gagal");
                }

                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(Bahan_stock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        loadBahan();
        loadIsiTbel();
        reset();
    }//GEN-LAST:event_btn_addActionPerformed

    private void btn_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editActionPerformed
        // Ambil baris yang dipilih di tabel
        int barisTerpilih = Produktable.getSelectedRow();

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
        int KodeBahan = (int) Produktable.getValueAt(barisTerpilih, 4);

        // Update data di database
        try (Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root", "Opoae_123")) {
            String kueri = "UPDATE produk SET NamaProduk=?, HargaProduk=? WHERE CodeProduk=?";
            PreparedStatement ps = koneksi.prepareStatement(kueri);
            ps.setString(1, NamaBahan);
            ps.setDouble(2, HargaBeli);
            ps.setInt(3, KodeBahan);
            int hasil = ps.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(this, "Update Berhasil");
            } else {
                JOptionPane.showMessageDialog(this, "Update Gagal");
            }

            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(Bahan_stock.class.getName()).log(Level.SEVERE, null, ex);
        }

        reset();
        loadBahan();
        loadIsiTbel();
    }//GEN-LAST:event_btn_editActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        // Ambil baris yang dipilih di tabel
        int barisTerpilih = Produktable.getSelectedRow();

        // Periksa apakah ada baris yang dipilih
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus terlebih dahulu.");
            return;
        }

        // Ambil KodeBahan dari tabel berdasarkan baris yang dipilih
        int KodeBahan = (int) Produktable.getValueAt(barisTerpilih, 4);

        // Tampilkan dialog konfirmasi
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin menghapus data ini?",
                "Konfirmasi Hapus Data", JOptionPane.YES_NO_OPTION);

        // Jika user memilih "Ya" (YES_OPTION)
        if (konfirmasi == JOptionPane.YES_OPTION) {
            // Hapus data di database
            try (Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root", "Opoae_123")) {
                String kueri = "DELETE FROM produk WHERE CodeProduk=?";
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
                JOptionPane.showMessageDialog(this, "Tidak dapat menghapus bahan. Hapus produk yang menggunakan bahan ini terlebih dahulu !!!!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                Logger.getLogger(Bahan_stock.class.getName()).log(Level.SEVERE, null, ex);
            }

            reset();
            loadBahan();
            loadIsiTbel();
        }
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_reset1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reset1ActionPerformed
        reset();
    }//GEN-LAST:event_btn_reset1ActionPerformed

    private void TambahBahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TambahBahanActionPerformed
        // Buat objek JDialog
        JDialog dialog = new JDialog(this, "Pilih Bahan Untuk Produk !!!!", true);
        dialog.setLayout(new BorderLayout());
        int barisTerpilih = Produktable.getSelectedRow();
        int KodeProduk = (int) Produktable.getValueAt(barisTerpilih, 4);

        // Buat model untuk tabel
        DefaultTableModel model = new DefaultTableModel();
        model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return Integer.class; // Kolom "Jumlah" akan menggunakan JSpinner
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 3;
            }
        };
        // Tambahkan kolom ke model
        model.addColumn("Nama Bahan");
        model.addColumn("Jumlah");
        model.addColumn("KodeBahan");
        model.addColumn("Kuantitas");
        // Tambahkan kolom lainnya sesuai kebutuhan

        // Koneksi ke database dan ambil data
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root", "Opoae_123")) {
            // Query SQL untuk mengambil data dari tabel
            String sql = "SELECT NamaBahan, Stok, KodeBahan FROM stokbahan";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Tambahkan data dari database ke dalam model
            while (resultSet.next()) {
                Object[] rowData = {
                    resultSet.getString("NamaBahan"),
                    resultSet.getInt("Stok"),
                    resultSet.getInt("KodeBahan"),
                    0
                // Tambahkan data kolom lainnya sesuai kebutuhan
                };
                model.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengambil data dari database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        reset2();
        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        dialog.add(scrollPane, BorderLayout.CENTER);
        addSpinnerEditorToTable(table, 3);
        hideColumn(table, 2);

        JButton tambahButton = new JButton("Tambah");
        tambahButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int kuantitas = (int) table.getValueAt(selectedRow, 3);
                if (kuantitas == 0) {
                    JOptionPane.showMessageDialog(dialog, "Isi kuantitas terlebih dahulu! Atau baris belum dipilih sih, soalnya lagi pegang spinner !?", "Peringatan", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Kode untuk menginsert data ke tabel lain
                    insertDataToDatabase(table, selectedRow, KodeProduk);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Pilih baris terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.add(tambahButton, BorderLayout.SOUTH);
        // Atur ukuran dan tampilkan JDialog
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null); // Posisikan di tengah layar
        dialog.setVisible(true);
        loadBahan();
        loadIsiTbel();
    }//GEN-LAST:event_TambahBahanActionPerformed

    private void BTNBAHANActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTNBAHANActionPerformed
        Bahan_stock bahan = new Bahan_stock();
        bahan.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BTNBAHANActionPerformed

    private void HapusBahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HapusBahanActionPerformed
        // Ambil baris yang dipilih di tabel
        int barisTerpilih = DetailBahan.getSelectedRow();

        // Periksa apakah ada baris yang dipilih
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus terlebih dahulu.");
            return;
        }

        // Ambil KodeBahan dari tabel berdasarkan baris yang dipilih
        int KodeBahan = (int) DetailBahan.getValueAt(barisTerpilih, 2);

        // Tampilkan dialog konfirmasi
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Anda yakin ingin menghapus data ini?",
                "Konfirmasi Hapus Data", JOptionPane.YES_NO_OPTION);

        // Jika user memilih "Ya" (YES_OPTION)
        if (konfirmasi == JOptionPane.YES_OPTION) {
            // Hapus data di database
            try (Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root", "Opoae_123")) {
                String kueri = "DELETE FROM `bahan&produk` WHERE KodeDetail = ?";
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
                JOptionPane.showMessageDialog(this, "Tidak dapat menghapus bahan. Hapus produk yang menggunakan bahan ini terlebih dahulu !!!!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                Logger.getLogger(Bahan_stock.class.getName()).log(Level.SEVERE, null, ex);
            }

            reset2();
            loadBahan();
            loadIsiTbel();
        }
    }//GEN-LAST:event_HapusBahanActionPerformed

    private void EditBahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditBahanActionPerformed
        // Ambil baris yang dipilih di tabel
        int barisTerpilih = DetailBahan.getSelectedRow();
        int kuantitas = 0;
        // Periksa apakah ada baris yang dipilih
        if (barisTerpilih == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit terlebih dahulu.");
            return;
        }
        TableCellEditor editor = DetailBahan.getCellEditor(barisTerpilih, 1);

        if (editor instanceof ProdukManagement.SpinnerEditor) {
            ProdukManagement.SpinnerEditor spinnerEditor = (ProdukManagement.SpinnerEditor) editor;
            Object spinnerValue = spinnerEditor.getCellEditorValue();

            if (spinnerValue instanceof Integer) {
                kuantitas = (int) spinnerValue;
            }
        }

        int KodeBahan = (int) DetailBahan.getValueAt(barisTerpilih, 2);

        // Update data di database
        try (Connection koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root", "Opoae_123")) {
            String kueri = "UPDATE `bahan&produk` SET Kuantitas=? WHERE KodeDetail=?";
            PreparedStatement ps = koneksi.prepareStatement(kueri);
            ps.setInt(1, kuantitas);
            ps.setInt(2, KodeBahan);
            int hasil = ps.executeUpdate();

            if (hasil > 0) {
                JOptionPane.showMessageDialog(this, "Update Berhasil");
            } else {
                JOptionPane.showMessageDialog(this, "Update Gagal");
            }

            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(Bahan_stock.class.getName()).log(Level.SEVERE, null, ex);
        }
        reset2();
        loadBahan();
        loadIsiTbel();
    }//GEN-LAST:event_EditBahanActionPerformed

    private void KaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KaryawanActionPerformed
        ManagemenAkun akun = new ManagemenAkun();
        akun.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_KaryawanActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String keyword = jTextField1.getText().toLowerCase();

        // Membuat RowSorter untuk model tabel
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(Produktable.getModel());
        Produktable.setRowSorter(sorter);

        // Menerapkan filter berdasarkan kata kunci hanya pada kolom "Nama Produk"
        if (keyword.length() == 0) {
            // Jika kotak pencarian kosong, hapus semua filter
            sorter.setRowFilter(null);
        } else {
            // Jika ada kata kunci, terapkan filter hanya pada kolom "Nama Produk"
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword, 1)); // Indeks 2 adalah kolom "Nama Produk"
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
    private void addSpinnerEditorToTable(JTable table, int columnIndex) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setCellEditor(new SpinnerEditor());
    }

    private class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {

        private final JSpinner spinner;

        public SpinnerEditor() {
            spinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            spinner.setValue(value);
            return spinner;
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }
    }

    private void insertDataToDatabase(JTable table, int selectedRow, int KodeProduk) {
        int kodeBahan = (int) table.getValueAt(selectedRow, 2);
        int kuantitas = (int) table.getValueAt(selectedRow, 3);

        // Koneksi ke database dan insert data
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/akubakulcs", "root", "Opoae_123")) {
            String sql = "INSERT INTO `Bahan&Produk` (KodeDetailBahan, Kuantitas, CodeProduk) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, kodeBahan);
            statement.setInt(2, kuantitas);
            statement.setInt(3, KodeProduk);
            statement.executeUpdate();
            reset2();
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan ke database.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menginsert data ke database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Produk.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ProdukManagement().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTNBAHAN;
    private javax.swing.JTable DetailBahan;
    private javax.swing.JButton EditBahan;
    private javax.swing.JToggleButton HapusBahan;
    private javax.swing.JButton Karyawan;
    private javax.swing.JTable Produktable;
    private javax.swing.JButton TambahBahan;
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_edit;
    private javax.swing.JButton btn_logout;
    private javax.swing.JButton btn_produk;
    private javax.swing.JButton btn_reset1;
    private javax.swing.JComboBox<String> cb_history;
    private javax.swing.JComboBox<String> cb_history1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private java.awt.TextField txt_harga;
    private java.awt.TextField txt_nama;
    // End of variables declaration//GEN-END:variables
}
