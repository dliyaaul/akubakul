/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeguii.admin;

/**
 *
 * @author Dliyaa'ul
 */
public class DetailBahan {
    private int IdDetailBahan;
    private String KodeBahan;
    private String KodeBahan2;
    private String KodeBahan3;
    private int Kuantitas;

    public DetailBahan(int IdDetailBahan, String KodeBahan, String KodeBahan2, String KodeBahan3, int Kuantitas) {
        this.IdDetailBahan = IdDetailBahan;
        this.KodeBahan = KodeBahan;
        this.KodeBahan2 = KodeBahan2;
        this.KodeBahan3 = KodeBahan3;
        this.Kuantitas = Kuantitas;
    }

    public int getIdDetailBahan() {
        return IdDetailBahan;
    }

    public String getKodeBahan() {
        return KodeBahan;
    }

    public String getKodeBahan2() {
        return KodeBahan2;
    }

    public String getKodeBahan3() {
        return KodeBahan3;
    }

    public int getKuantitas() {
        return Kuantitas;
    }
}
