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
    private Integer KodeBahan;
    private String NamaBahan;
    private double HargaBeli;
    private Integer Stok;
    
    public Bahan(Integer KodeBahan, String NamaBahan, double HargaBeli, Integer Stok) {
        this.KodeBahan = KodeBahan;
        this.NamaBahan = NamaBahan;
        this.HargaBeli = HargaBeli;
        this.Stok = Stok;
    }
    
    public Integer getKodeBahan() {
        return KodeBahan;
    }
    
    public String getNamaBahan() {
        return NamaBahan;
    }

    public double getHargaBeli() {
        return HargaBeli;
    }
    
    public Integer getStok() {
        return Stok;
    }
}
