/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeguii.admin;

/**
 *
 * @author Dliyaa'ul
 */
public class Bahan {
    Integer kodeBahan;
    String namaBahan;
    double hargaBeli;
    Integer stok;

    public Bahan(Integer kodeBahan, String namaBahan, double hargaBeli, Integer stok) {
        this.kodeBahan = kodeBahan;
        this.namaBahan = namaBahan;
        this.hargaBeli = hargaBeli;
        this.stok = stok;
    }

    public Integer getKodeBahan() {
        return kodeBahan;
    }

    public String getNamaBahan() {
        return namaBahan;
    }

    public double getHargaBeli() {
        return hargaBeli;
    }

    public Integer getStok() {
        return stok;
    }
}
