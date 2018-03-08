/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainapp;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author user
 */
public class dbAccess {
    
    public GlobalVariabel gv = new GlobalVariabel();
    
    private Connection internalconnect() {
        // SQLite connection string
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString();
        String url = "jdbc:sqlite:" + path + "\\db\\datasiswa.sqlite";
        System.out.println(url);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Success");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public void showDataSiswa(){
        String sql = "SELECT * FROM _data_siswa";
        
        int row = 1;
        String NISN;
        String namaSiswa;
        String kelasJurusan;
        
        DefaultTableModel tabelSiswa = (DefaultTableModel) MainFrame.tabelSiswa.getModel();
        
        int rowCount = tabelSiswa.getRowCount();
            
        for (int i = rowCount - 1; i >= 0; i--) {
            tabelSiswa.removeRow(i);
        }
        
        try (Connection conn = this.internalconnect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                NISN = rs.getString("NISN");
                namaSiswa = rs.getString("NamaSiswa");
                kelasJurusan = rs.getString("Kelas") + " / " + rs.getString("Jurusan");
                tabelSiswa.addRow(new Object[]{row, NISN, namaSiswa, kelasJurusan});
                row++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void selectDataSiswa(){
        String sql = "SELECT * FROM _data_siswa WHERE NISN='" + gv.NISNSiswa + "'";
        System.out.println(sql);
        try (Connection conn = this.internalconnect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                gv.IDSiswa = rs.getString("IDSiswa");
                gv.NamaSiswa = rs.getString("NamaSiswa");
                gv.KelasSiswa = rs.getString("Kelas");
                gv.JurusanSiswa = rs.getString("Jurusan");    
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public int checkData(String NISN){
        int alpha = 0;
        String sql = "SELECT * FROM _data_siswa WHERE NISN='" + NISN + "'";
        System.out.println(sql);
        try (Connection conn = this.internalconnect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            while (rs.next()) {
                    alpha++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return alpha;
    }
    
    public void createDataSiswa(){
        String ID = String.valueOf(System.currentTimeMillis());
        String NISN = gv.NISNSiswa;
        String NamaSiswa = gv.NamaSiswa;
        String KelasSiswa = gv.KelasSiswa;
        String JurusanSiswa = gv.JurusanSiswa;
        String SQL = "INSERT INTO _data_siswa (IDSiswa, NISN, NamaSiswa, Kelas, Jurusan) VALUES ('"+ID+"', '"+NISN+"', '"+NamaSiswa+"', '"+KelasSiswa+"', '"+JurusanSiswa+"')";
        
        try (Connection conn = this.internalconnect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.executeUpdate();
         }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
            JOptionPane.showMessageDialog(null, "Data siswa berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            showDataSiswa();
    }    
    
    public void updateDataSiswa(){
        String ID = gv.IDSiswa;
        String NISN = gv.NISNSiswa;
        String NamaSiswa = gv.NamaSiswa;
        String KelasSiswa = gv.KelasSiswa;
        String JurusanSiswa = gv.JurusanSiswa;
        
        String SQL = "UPDATE _data_siswa SET NISN='"+NISN+"', NamaSiswa='"+NamaSiswa+"', Kelas='"+KelasSiswa+"', Jurusan='"+JurusanSiswa+"' WHERE IDSiswa='"+ID+"'";
        
        try (Connection conn = this.internalconnect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.executeUpdate();
         }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
     showDataSiswa();   
     JOptionPane.showMessageDialog(null, "Berhasil menyimpan perubahan data siswa!","Sukses",JOptionPane.INFORMATION_MESSAGE);
     MainFrame.btnHapusData.setEnabled(false);
     MainFrame.btnUbahData.setEnabled(false);
    }
    
    public void deleteDataSiswa(){
        String ID = gv.IDSiswa;
        String SQL = "DELETE FROM _data_siswa WHERE IDSiswa='"+ID+"'";
        try (Connection conn = this.internalconnect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.executeUpdate();
         }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
     showDataSiswa();   
     JOptionPane.showMessageDialog(null, "Berhasil menghapus data siswa!","Sukses",JOptionPane.INFORMATION_MESSAGE);
     MainFrame.btnHapusData.setEnabled(false);
     MainFrame.btnUbahData.setEnabled(false);
    }
    
    public void resetDataSiswa(){
        String SQL = "DELETE FROM _data_siswa";
        try (Connection conn = this.internalconnect();
            PreparedStatement pstmt = conn.prepareStatement(SQL)){
            pstmt.executeUpdate();
         }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
     showDataSiswa();   
     JOptionPane.showMessageDialog(null, "Berhasil menyetel ulang data siswa!","Sukses",JOptionPane.INFORMATION_MESSAGE);
     MainFrame.btnHapusData.setEnabled(false);
     MainFrame.btnUbahData.setEnabled(false);
    }
    
    public void exportDataSiswa() throws Exception{
        final int DEFAULT_BUFFER_SIZE = 100000; 
        
        String sql = "SELECT * FROM _data_siswa";
        try (Connection conn = this.internalconnect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
                resultSetToExcel exporter = new resultSetToExcel(rs, "Export Laporan");
                String filename = JOptionPane.showInputDialog("Nama file hasil export: ", "Nama File Hasil Export");
                JFileChooser savefile = new JFileChooser();
                savefile.setSelectedFile(new File(filename+".xls"));
                int sf = savefile.showSaveDialog(null);
                if(sf == JFileChooser.APPROVE_OPTION){
                    try {
                        File file = savefile.getSelectedFile();
                        exporter.generate(file);
                        JOptionPane.showMessageDialog(null, "File export berhasil disimpan.","Berhasil Export",JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException e) {
                        }
                            }else if(sf == JFileChooser.CANCEL_OPTION){
                                JOptionPane.showMessageDialog(null, "Export gagal!", "Error", JOptionPane.ERROR_MESSAGE);
                             }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
