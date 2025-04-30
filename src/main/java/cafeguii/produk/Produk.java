/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeguii.produk;

import javax.swing.ImageIcon;

/**
 *
 * @author Dliyaa'ul
 */
public class Produk {

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
