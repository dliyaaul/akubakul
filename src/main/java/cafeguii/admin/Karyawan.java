/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafeguii.admin;

/**
 *
 * @author AXIOO PONGO
 */
public class Karyawan {
    private int KodeKaryawan;
    private String UsernameKasir;
    private String Password;
    private String Tingkatan;
    
    public Karyawan(int KodeKaryawan, String UsernameKasir, String Password, String Tingkatan) {
        this.KodeKaryawan = KodeKaryawan;
        this.UsernameKasir = UsernameKasir;
        this.Password = Password;
        this.Tingkatan = Tingkatan;
    }
    
    public int getKodeKaryawan() {
        return KodeKaryawan;
    }
    
    public String getUsernameKasir() {
        return UsernameKasir;
    }

    public String getPassword() {
        return Password;
    }
    
    public String getTingkatan() {
        return Tingkatan;
    }
}
